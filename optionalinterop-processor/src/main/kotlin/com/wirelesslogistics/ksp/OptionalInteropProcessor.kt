package com.wirelesslogistics.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo

class OptionalInteropProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("com.wirelesslogistics.ksp.OptionalInterop")
            .filterIsInstance<KSClassDeclaration>()

        for (symbol in symbols) {
            generateOptionalHelpers(symbol)
        }

        return emptyList()
    }

    private fun generateOptionalHelpers(classDecl: KSClassDeclaration) {
        val packageName = classDecl.packageName.asString()
        val className = classDecl.simpleName.asString()

        val typeParams = classDecl.typeParameters.map { TypeVariableName(it.name.asString()) }
        val baseClassName = ClassName(packageName, className)
        val classType = if (typeParams.isEmpty()) {
            baseClassName
        } else {
            baseClassName.parameterizedBy(typeParams)
        }

        val fileSpecBuilder = FileSpec.builder(packageName, "${className}OptionalInterop")

        val optionalClass = ClassName("java.util", "Optional")

        val allProps = classDecl.getAllProperties().toList()

        val constructorParams = mutableListOf<ParameterSpec>()
        val constructorArgs = mutableListOf<String>()

        for (prop in allProps) {
            val name = prop.simpleName.asString()
            val resolvedType = prop.type.resolve()
            val typeName = resolvedType.toPoetTypeName()

            if (resolvedType.isMarkedNullable) {
                constructorParams += ParameterSpec.builder(name, optionalClass.parameterizedBy(typeName)).build()
                constructorArgs += "$name.orElse(null)"
            } else {
                constructorParams += ParameterSpec.builder(name, typeName).build()
                constructorArgs += name
            }
        }

        val fromOptionals = FunSpec.builder("fromOptionals")
            .receiver(baseClassName.nestedClass("Companion"))
            .returns(classType)
            .addTypeVariables(typeParams)
            .addParameters(constructorParams)
            .addStatement("return %T(%L)", classType, constructorArgs.joinToString())

        fileSpecBuilder.addFunction(fromOptionals.build())

        for (prop in allProps) {
            if (!prop.type.resolve().isMarkedNullable) continue

            val name = prop.simpleName.asString()
            val typeName = prop.type.resolve().toPoetTypeName()
            val propertyName = "${name}Optional"

            fileSpecBuilder.addProperty(
                PropertySpec.builder(propertyName, optionalClass.parameterizedBy(typeName))
                    .receiver(classType)
                    .addTypeVariables(typeParams)
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement("return Optional.ofNullable(this.%L)", name)
                            .build()
                    )
                    .build()
            )
        }

        val file = fileSpecBuilder.build()
        file.writeTo(codeGenerator, Dependencies(false))
    }

    private fun KSType.toPoetTypeName(): TypeName {
        val baseDecl = declaration.qualifiedName?.asString()
        val rawType = if (baseDecl != null) ClassName.bestGuess(baseDecl) else toTypeName()

        return if (arguments.isNotEmpty()) {
            val resolvedArgs = arguments.mapNotNull { it.type?.resolve()?.toPoetTypeName() }
            if (rawType is ClassName) rawType.parameterizedBy(resolvedArgs) else rawType
        } else {
            rawType
        }
    }
}

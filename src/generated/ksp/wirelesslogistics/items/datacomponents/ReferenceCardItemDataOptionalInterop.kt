package wirelesslogistics.items.datacomponents

import java.util.Optional
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level

public fun ReferenceCardItemData.Companion.fromOptionals(
  referencedBlockType: Optional<ResourceLocation>,
  referencedBlockPos: Optional<BlockPos>,
  dimension: Optional<ResourceKey<Level>>,
): ReferenceCardItemData = ReferenceCardItemData(referencedBlockType.orElse(null),
    referencedBlockPos.orElse(null), dimension.orElse(null))

public val ReferenceCardItemData.referencedBlockTypeOptional: Optional<ResourceLocation>
  get() = Optional.ofNullable(this.referencedBlockType)

public val ReferenceCardItemData.referencedBlockPosOptional: Optional<BlockPos>
  get() = Optional.ofNullable(this.referencedBlockPos)

public val ReferenceCardItemData.dimensionOptional: Optional<ResourceKey<Level>>
  get() = Optional.ofNullable(this.dimension)

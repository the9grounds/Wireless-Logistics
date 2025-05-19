package wirelesslogistics.core.handler

import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.IEnergyStorage
import wirelesslogistics.blockentity.NodeBlockEntity
import wirelesslogistics.core.Handlers

class PowerHandler(side: Direction, blockEntity: NodeBlockEntity): AbstractHandler(side, blockEntity), IEnergyStorage {

    // Per tick
    var throughPut = 1000L
    val internalStorageMax: Long
    get() = throughPut * 100L
    var internalStorage = 0L
    var cachedAcceptor: BlockEntity? = null

    override fun tick() {
        if (!isConnectable) {
            return
        }

        val capability = cachedAcceptor!!.level?.getCapability(Capabilities.EnergyStorage.BLOCK, cachedAcceptor!!.blockPos, side.opposite)
            ?: return

        if (!capability.canReceive()) {
            return
        }

        val amountToReceive = capability.receiveEnergy(Int.MAX_VALUE, true)

        if (amountToReceive > 0) {
            val amountReceived = extractEnergy(amountToReceive, false)

            if (amountReceived > 0) {
                capability.receiveEnergy(amountReceived, false)
            }
        }
    }

    init {
        val acceptor = blockEntity.level?.getBlockEntity(blockEntity.blockPos.relative(side))

        if (acceptor != null && acceptor.level?.getCapability(Capabilities.EnergyStorage.BLOCK, acceptor.blockPos, side) !== null) {
            cachedAcceptor = acceptor
        }
    }

    val freeStorage: Long
        get() = internalStorageMax - internalStorage

    override fun onWorldChange() {
        val acceptor = blockEntity.level?.getBlockEntity(blockEntity.blockPos.relative(side))

        var cachedAcceptor: BlockEntity? = null

        if (acceptor != null && acceptor.level?.getCapability(Capabilities.EnergyStorage.BLOCK, acceptor.blockPos, side) !== null) {
            cachedAcceptor = acceptor
        }

        this.cachedAcceptor = cachedAcceptor
    }

    override val isConnectable: Boolean
        get() = cachedAcceptor !== null

    override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int {
        val received = (if (freeStorage > toReceive) toReceive else freeStorage).toLong()
        if (!simulate) {
            internalStorage += received
        }

        return received.toInt()
    }

    override fun extractEnergy(toReceive: Int, simulate: Boolean): Int {
        val toReceiveLong = toReceive.toLong()
        val amountSatisfiedByInternalBuffer = (if (internalStorage > toReceiveLong) toReceiveLong else internalStorage).coerceIn(0L, throughPut)

        var leftOver = toReceive - amountSatisfiedByInternalBuffer

        if (!simulate) {
            internalStorage -= amountSatisfiedByInternalBuffer
        }

        if (leftOver > 0) {
            run {
                blockEntity.connectedEntities.forEach {
                    it.sideToHandlers.forEach { (t, u) ->
                        val handler = u[Handlers.Power]

                        if (handler is PowerHandler) {
                            val satisfied = handler.extractFromInternalStorage(leftOver, simulate)

                            leftOver -= satisfied

                            if (leftOver === 0L) {
                                return@run
                            }
                        }
                    }
                }
            }
        }

        return toReceive - leftOver.toInt()
    }

    fun extractFromInternalStorage(amount: Long, simulate: Boolean): Long {
        val satisfied = (if (internalStorage > amount) internalStorage - amount else internalStorage).coerceIn(0L, throughPut)

        if (!simulate) {
            internalStorage -= satisfied
        }

        return satisfied
    }

    override fun getEnergyStored(): Int {
        return internalStorage.toInt()
    }

    override fun getMaxEnergyStored(): Int {
        return internalStorageMax.toInt()
    }

    override fun canExtract(): Boolean {
        return internalStorage > 0 || blockEntity.isConnected()
    }

    override fun canReceive(): Boolean {
        return internalStorage < internalStorageMax || this.blockEntity.isConnected()
    }
}
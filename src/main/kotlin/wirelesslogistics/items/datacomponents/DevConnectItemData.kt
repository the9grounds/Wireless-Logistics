package wirelesslogistics.items.datacomponents

import net.minecraft.core.BlockPos
import wirelesslogistics.core.enums.DevConnectItemMode
import java.util.*

class DevConnectItemData(var mode: DevConnectItemMode = DevConnectItemMode.LINK, var linkedBlockPos: BlockPos? = null) {

    override fun hashCode(): Int {
        return Objects.hash(mode, linkedBlockPos)
    }

    override fun equals(other: Any?): Boolean {
        return if (other === this) {
            true
        } else {
            other is DevConnectItemData && other.mode == mode && other.linkedBlockPos == linkedBlockPos
        }
    }

    val linkedBlockPosLong: Long
        get() = linkedBlockPos?.asLong() ?: 0L
    val modeInt
        get() = mode.ordinal

    companion object {
        fun fromCodec(linkedBlockPosLong: Long, mode: Int): DevConnectItemData {
            return DevConnectItemData(DevConnectItemMode.entries[mode],
                if (linkedBlockPosLong == 0L) null else BlockPos.of(linkedBlockPosLong))
        }
    }
}

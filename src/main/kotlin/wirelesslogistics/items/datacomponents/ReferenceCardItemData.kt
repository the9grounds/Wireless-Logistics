package wirelesslogistics.items.datacomponents

import com.wirelesslogistics.ksp.OptionalInterop
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.Level
import java.util.*

@OptionalInterop
data class ReferenceCardItemData(var referencedBlockType: ResourceLocation? = null, var referencedBlockPos: BlockPos? = null, var dimension: ResourceKey<Level>? = null) {
    companion object;
    override fun hashCode(): Int {
        return Objects.hash(referencedBlockTypeOptional, referencedBlockPosOptional, dimensionOptional)
    }

    override fun equals(other: Any?): Boolean {
        return if (other === this) {
            true
        } else {
            other is ReferenceCardItemData && referencedBlockType == other.referencedBlockType && referencedBlockPos == other.referencedBlockPos && dimension == other.dimension
        }
    }

    fun clear() {
        referencedBlockPos = null
        referencedBlockType = null
        dimension = null
    }
}

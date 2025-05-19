package wirelesslogistics.core

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import wirelesslogistics.items.datacomponents.DevConnectItemData

object Codecs {
    val DEV_CONNECT_ITEM_CODEC = RecordCodecBuilder.create {
        it.group(
            Codec.LONG.fieldOf("linkedBlockPos").forGetter(DevConnectItemData::linkedBlockPosLong),
            Codec.INT.fieldOf("mode").forGetter(DevConnectItemData::modeInt)
        ).apply(it, DevConnectItemData::fromCodec)
    }

    val DEV_CONNECT_ITEM_STEAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_LONG, DevConnectItemData::linkedBlockPosLong,
        ByteBufCodecs.INT, DevConnectItemData::modeInt,
        DevConnectItemData::fromCodec
    )
}
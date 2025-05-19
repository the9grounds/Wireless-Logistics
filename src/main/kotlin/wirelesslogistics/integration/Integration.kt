package wirelesslogistics.integration

import wirelesslogistics.Logger

object Integration {

    fun init() {
        for (mod in Mods.values()) {
            if (mod.isEnabled) {
                Logger.info("Integration for ${mod.modName} has been loaded")
            }
        }
    }
}
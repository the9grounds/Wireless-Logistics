package com.the9grounds.wirelesslogistics.integration

import com.the9grounds.wirelesslogistics.Logger

object Integration {

    fun init() {
        for (mod in Mods.values()) {
            if (mod.isEnabled) {
                Logger.info("Integration for ${mod.modName} has been loaded")
            }
        }
    }
}
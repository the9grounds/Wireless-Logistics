package com.the9grounds.wirelesslogistics.debug

import com.the9grounds.wirelesslogistics.debug.commands.ICommand
import com.the9grounds.wirelesslogistics.debug.commands.ChangeChannelCommand
import com.the9grounds.wirelesslogistics.debug.commands.ConnectToOtherBlockEntityCommand

enum class Commands(val level: Int, val command: ICommand) {
    ChangeChannel(4, ChangeChannelCommand),
    ConnectBlock(4, ConnectToOtherBlockEntityCommand)
}
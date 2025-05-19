package wirelesslogistics.debug

import wirelesslogistics.debug.commands.ICommand
import wirelesslogistics.debug.commands.ChangeChannelCommand
import wirelesslogistics.debug.commands.ConnectToOtherBlockEntityCommand

enum class Commands(val level: Int, val command: ICommand) {
    ChangeChannel(4, ChangeChannelCommand),
    ConnectBlock(4, ConnectToOtherBlockEntityCommand)
}
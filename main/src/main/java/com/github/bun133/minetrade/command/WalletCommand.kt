package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.minetrade.Minetrade
import dev.kotx.flylib.command.Command
import net.kyori.adventure.text.Component.text


class WalletCommand : Command("wallet") {
    init {
        description("Show your wallet")
        usage {
            selectionArgument("operation", "list")
            executes {
                val plugin = this.plugin as Minetrade
                when (typedArgs[0] as String) {
                    "list" -> {
                        plugin.walletManager.wallets.map { it.key.name() + text(":$${it.value.balance}") }
                            .forEach { sender.sendMessage(it) }
                    }
                }
            }
        }
    }
}
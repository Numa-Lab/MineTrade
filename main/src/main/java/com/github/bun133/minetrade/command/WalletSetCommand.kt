package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player

class WalletSetCommand : Command("set") {
    init {
        description("Set your wallet")
        usage {
            entityArgument("player", enableEntities = false)
            integerArgument("amount")
            executes {
                val plugin = this.plugin as Minetrade
                val plist = typedArgs[0] as? List<*>
                if (plist == null) {
                    sender.sendMessage(text("プレイヤーが見つかりませんでした"))
                    return@executes
                }

                val playerList = plist.filterIsInstance<Player>()
                if (playerList.isEmpty()) {
                    sender.sendMessage(text("プレイヤーが見つかりませんでした"))
                    return@executes
                }
                val amount = typedArgs[1] as Int

                playerList.forEach { p ->
                    val wallet = plugin.walletManager.getWallet(p)
                    wallet.set(amount)
                    p.sendMessage(text("残高が${amount}に設定されました"))
                }
                sender.sendMessage(text("${playerList.size}人の残高を${amount}に設定しました"))
            }
        }
    }
}
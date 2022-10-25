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
                val plist= typedArgs[0] as? List<*>
                if(plist==null){
                    sender.sendMessage(text("プレイヤーが見つかりませんでした"))
                    return@executes
                }
                val p = plist.firstOrNull() as? Player
                if (p == null) {
                    sender.sendMessage("プレイヤーが見つかりませんでした")
                    return@executes
                }
                val amount = typedArgs[1] as Int
                val wallet = plugin.walletManager.getWallet(p)
                wallet.set(amount)
                sender.sendMessage(text("残高を${amount}に設定しました"))
                p.sendMessage(text("残高が${amount}に設定されました"))
            }
        }
    }
}
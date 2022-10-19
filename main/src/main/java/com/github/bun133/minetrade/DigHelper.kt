package com.github.bun133.minetrade

import com.github.bun133.bukkitfly.component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class DigHelper(val plugin: Minetrade) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onDig(e: BlockBreakEvent) {
        if (plugin.market == null) return    // Game is not started
        val wallet = plugin.walletManager.getWallet(e.player)

        val entry = plugin.market!!.getEntryFor(e.block)
        if (entry != null) {
            if (entry.canBuy(wallet)) {
                entry.buy(wallet, e.player.inventory, false)
                e.isDropItems = false
            } else {
                // Cant buy this entry because of lack of money
                e.player.sendMessage(
                    text(
                        "お金が足りません(${entry.buyPrice() - wallet.balance}不足しています)",
                        NamedTextColor.RED
                    )
                )
                e.isCancelled = true
            }
        }
    }
}
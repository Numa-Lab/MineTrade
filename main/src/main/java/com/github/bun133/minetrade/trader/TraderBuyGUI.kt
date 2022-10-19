package com.github.bun133.minetrade.trader

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.guifly.gui.GUI
import com.github.bun133.guifly.gui.type.InventoryType
import com.github.bun133.guifly.infinite.InfiniteGUIBuilder
import com.github.bun133.guifly.item
import com.github.bun133.guifly.title
import com.github.bun133.minetrade.market.MarketEntry
import com.github.bun133.minetrade.market.Wallet
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class TraderBuyGUI {
    companion object {
        fun genGUI(player: Player, wallet: Wallet, entries: List<MarketEntry>, plugin: JavaPlugin): GUI {
            val gui = InfiniteGUIBuilder()
            gui.title(text("購入"))
            gui.setType(InventoryType.CHEST_6)
            gui.setMarkedNotInsertable()

            setItems(player, wallet, entries, gui)

            return gui.build(plugin)
        }

        private fun setItems(
            player: Player,
            wallet: Wallet,
            entries: List<MarketEntry>,
            gui: InfiniteGUIBuilder
        ) {

            println("entries: ${entries.size}")
            entries.forEachIndexed { index, marketEntry ->
                val x = index % 8 + 1
                val y = index / 8 + 1

                println("marketEntry: ${marketEntry.item.material.value().name}, x: $x, y: $y")

                gui.item(x, y) {
                    stack(marketEntry.item.value())
                    click {
                        // TODO ゲームバランスの調整: 価格の上昇(直接採掘との差をつける)
                        onBuy(player, wallet, marketEntry, false)
                    }
                    shiftClick {
                        onBuy(player, wallet, marketEntry, true)
                    }
                    markAsUnMovable()
                }
            }
        }
    }
}
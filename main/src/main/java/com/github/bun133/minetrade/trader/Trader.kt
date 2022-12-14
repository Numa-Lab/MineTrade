package com.github.bun133.minetrade.trader

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.market.Market
import com.github.bun133.minetrade.market.MarketEntry
import com.github.bun133.minetrade.market.Wallet
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.metadata.MetadataValueAdapter
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

private class TraderMeta(owningPlugin: Plugin) : MetadataValueAdapter(owningPlugin) {
    override fun value() = true
    override fun invalidate() {
        // Do nothing
    }

    companion object {
        const val KEY = "minetrade_trader"
    }
}

fun spawnTraderAtLocation(location: Location, market: Market, plugin: Plugin, name: String) {
    val villager: Villager = location.world.spawnEntity(location, org.bukkit.entity.EntityType.VILLAGER) as Villager
    villager.setAI(false)
    villager.isSilent = true
    villager.canPickupItems = false
    villager.setAdult()
    villager.profession = Villager.Profession.NONE
    villager.villagerType = Villager.Type.PLAINS
    villager.customName(text(name))
    villager.health = 1.0 // ワンパンで倒せるように

    villager.setMetadata(TraderMeta.KEY, TraderMeta(plugin))    // Mark as Trader

    TraderEventHelper.addTrader(villager, market)   // Register Trader
}

fun isTrader(villager: Villager): Boolean {
    return villager.hasMetadata(TraderMeta.KEY)
}

class TraderEventHelper(val plugin: Minetrade) : Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    companion object {
        private val markets = mutableMapOf<Villager, Market>()
        fun addTrader(villager: Villager, market: Market) {
            markets[villager] = market
        }

        private fun getMarket(villager: Villager): Market? {
            return markets[villager]?.takeIf { it.isEnabled }   // If Market is Disabled, return null
        }
    }

    @EventHandler
    fun onVillagerInteract(event: PlayerInteractEntityEvent) {
        if (event.rightClicked is Villager) {
            val villager = event.rightClicked as Villager
            if (isTrader(villager)) {
                event.isCancelled = true
                val handedItem = event.player.inventory.itemInMainHand
                val market = getMarket(villager)
                if (market == null) {
                    // TODO: Error
                    event.player.sendMessage(text("エラーが発生しました", NamedTextColor.RED))
                    return
                } else {
                    val wallet = plugin.walletManager.getWallet(event.player)
                    if (handedItem.type.isEmpty) {
//                        openBuyGUI(event.player, wallet, market.entries().toList(), plugin)
                        event.player.sendMessage(text("売りたいアイテムを手にもってクリックしてください", NamedTextColor.RED))
                    } else {
                        val entry: MarketEntry? = market.getEntryFor(handedItem)
                        if (entry == null) {
                            event.player.sendMessage(text("このアイテムは売れません", NamedTextColor.RED))
                        } else {
                            val (b, i) = onSell(event.player, wallet, entry)
                            if (b) {
                                // Success
                                event.player.sendMessage(text("${i}で売却しました", NamedTextColor.GREEN))
                            } else {
                                // Failed
                                event.player.sendMessage(text("売却に失敗しました", NamedTextColor.RED))
                            }
                        }
                    }
                }
            }
        }
    }
}


// Not Used
private fun openBuyGUI(player: Player, wallet: Wallet, entries: List<MarketEntry>, plugin: JavaPlugin) {
    TraderBuyGUI.genGUI(player, wallet, entries, plugin).open(player)
}

/**
 * Buy item from market
 * @param muchAsPossible If true, buy as much as possible
 * @return (whether the item is bought, the amount of money consumed)
 */
fun onBuy(player: Player, wallet: Wallet, entry: MarketEntry, muchAsPossible: Boolean): Pair<Boolean, Int> {
    return entry.buy(wallet, player.inventory, muchAsPossible)
}

/**
 * Sell the item
 * @return (whether the item is sold, the amount of money)
 */
private fun onSell(player: Player, wallet: Wallet, entry: MarketEntry): Pair<Boolean, Int> {
    // TODO Sell Item
    return entry.sell(wallet, player.inventory)
}
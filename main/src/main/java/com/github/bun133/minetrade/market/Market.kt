package com.github.bun133.minetrade.market

import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.config.MineTradeEntryConfig
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class Market(private val conf: MineTradeConfig, val plugin: Minetrade) {
    private val map = mutableMapOf<MineTradeEntryConfig, MarketEntry>()
    private fun configEntries() = conf.tradings
    fun entries() = map.values

    var isEnabled = true
        private set

    fun markAsDisabled() {
        isEnabled = false
    }


    init {
        // Init All Trading Entries
        configEntries().forEach {
            map[it] = MarketEntry(it.block, it.item, it.basePrice, plugin)
        }
    }

    fun getEntryFor(block: Block): MarketEntry? {
        val e = configEntries().firstOrNull { it.block.value() == block.type } ?: return null
        return map.getOrPut(e) { MarketEntry(e.block, e.item, e.basePrice, plugin) }
    }

    fun getEntryFor(item: ItemStack): MarketEntry? {
        val e =
            configEntries().firstOrNull { it.item.value().type == item.type && it.item.value().amount <= item.amount }
                ?: return null
        return map.getOrPut(e) { MarketEntry(e.block, e.item, e.basePrice, plugin) }
    }
}
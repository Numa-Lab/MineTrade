package com.github.bun133.minetrade.market

import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.config.MineTradeEntryConfig
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class Market(private val conf: MineTradeConfig) {
    private val map = mutableMapOf<MineTradeEntryConfig, MarketEntry>()
    private fun entries() = conf.tradings

    fun getEntryFor(block: Block): MarketEntry? {
        val e = entries().firstOrNull { it.block.value() == block.type } ?: return null
        return map.getOrPut(e) { MarketEntry(e.block, e.item, e.basePrice) }
    }

    fun getEntryFor(item: ItemStack): MarketEntry? {
        val e = entries().firstOrNull { it.item.value().type == item.type && it.item.value().amount <= item.amount }
            ?: return null
        return map.getOrPut(e) { MarketEntry(e.block, e.item, e.basePrice) }
    }
}
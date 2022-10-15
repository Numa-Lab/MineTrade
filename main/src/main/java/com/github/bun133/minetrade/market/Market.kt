package com.github.bun133.minetrade.market

import com.github.bun133.minetrade.config.MineTradeEntryConfig
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class Market(entries: List<MineTradeEntryConfig>) {
    private val entries = mutableMapOf<MineTradeEntryConfig, MarketEntry>()

    init {
        initializeWith(entries)
    }


    fun initializeWith(e: List<MineTradeEntryConfig>) {
        // TODO Merge
        e.forEach {
            entries[it] = MarketEntry(it.block, it.item, it.basePrice)
        }
    }

    fun getEntryFor(block: Block): MarketEntry? {
        return entries.filter { it.value.blockType.value() == block.type && it.value.item.amount.value() == 1 }.values.firstOrNull()
    }

    @Deprecated("Use getEntryFor(Block)")
    fun getEntryFor(item: ItemStack): MarketEntry? {
        return entries.filter { it.key.item.material.value() == item.type && it.key.item.amount.value() <= item.amount }.values.firstOrNull()
    }
}
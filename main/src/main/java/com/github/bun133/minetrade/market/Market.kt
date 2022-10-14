package com.github.bun133.minetrade.market

import com.github.bun133.minetrade.config.MineTradeEntryConfig
import org.bukkit.inventory.ItemStack

class Market(entries: List<MineTradeEntryConfig>) {
    private val entries = mutableListOf<MineTradeEntryConfig>()
    init {
        initializeWith(entries)
    }


    fun initializeWith(e: List<MineTradeEntryConfig>) {
        entries.addAll(e)   // TODO Merge
    }

    fun getEntryFor(item: ItemStack): MarketEntry? {
        return entries.firstOrNull { it.item.material.value() == item.type && it.item.amount.value() <= item.amount }
            ?.let {
                MarketEntry(it.item.value(), it.basePrice.value())
            }
    }
}
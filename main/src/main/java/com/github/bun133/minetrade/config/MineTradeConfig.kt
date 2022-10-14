package com.github.bun133.minetrade.config

import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.IntegerValue
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class MineTradeConfig(plugin: Plugin) : BaseConfig(plugin) {
    val tradings = mutableListOf<MineTradeEntryConfig>()
}

class MineTradeEntryConfig {
    val item = ItemValue(ItemStack(Material.STONE))
    val basePrice = IntegerValue(1)

    fun prettyPrint(): String {
        return "${item.value().type.name} x ${item.value().amount} : ${basePrice.value()}$"
    }
}

fun fromItemStack(stack: ItemStack, basePrice: Int): MineTradeEntryConfig {
    return MineTradeEntryConfig().apply {
        item.value(stack)
        this.basePrice.value(basePrice)
    }
}
package com.github.bun133.minetrade.config

import net.kunmc.lab.configlib.BaseConfig
import net.kunmc.lab.configlib.value.IntegerValue
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class MineTradeConfig(plugin: Plugin) : BaseConfig(plugin) {
    val tradings = listOf<MineTradeEntryConfig>()
}

class MineTradeEntryConfig {
    val item = ItemValue(ItemStack(Material.STONE))
    val basePrice = IntegerValue(1)
}
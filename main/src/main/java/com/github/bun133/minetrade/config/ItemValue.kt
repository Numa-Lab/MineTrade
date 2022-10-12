package com.github.bun133.minetrade.config

import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.MaterialValue
import org.bukkit.inventory.ItemStack

class ItemValue(item: ItemStack) {
    var material = MaterialValue(item.type)
    var amount = IntegerValue(item.amount)

    fun value(stack: ItemStack) {
        material.value(stack.type)
        amount.value(stack.amount)
    }

    fun value() = ItemStack(material.value(), amount.value())
}
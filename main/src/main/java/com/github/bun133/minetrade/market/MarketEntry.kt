package com.github.bun133.minetrade.market

import com.github.bun133.bukkitfly.stack.addOrDrop
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class MarketEntry(val item: ItemStack, var baseValue: Int) {
    private fun buyPrice(wallet: Wallet): Int {
        // TODO
        return baseValue
    }

    private fun sellPrice(wallet: Wallet): Int {
        // TODO
        return baseValue
    }

    fun canBuy(wallet: Wallet) = wallet.has(buyPrice(wallet))

    fun buy(wallet: Wallet, toAddInventory: PlayerInventory) {
        if (canBuy(wallet)) {
            wallet.remove(buyPrice(wallet))
            toAddInventory.addOrDrop(item.clone())
        } else {
            // Should not happen
        }
    }

    /**
     * Sell the item
     * @return (whether the item is sold, the amount of money)
     */
    fun sell(wallet: Wallet, fromInventory: PlayerInventory): Pair<Boolean, Int> {
        if (fromInventory.containsAtLeast(item, item.amount)) {
            val price = sellPrice(wallet)
            wallet.add(price)
            fromInventory.removeItem(item)
            return Pair(true, price)
        }

        return Pair(false, 0)
    }
}
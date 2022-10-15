package com.github.bun133.minetrade.market

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.config.ItemValue
import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.MaterialValue
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

/**
 * MarketEntry is a class that represents an entry of market.
 * @param item ItemStack that represents the item.
 * @param baseValue base price of the item.
 * @param blockType Material that represents the block of [item]. if [item] is not a block, this should be AIR. when this field is set, player who owns wallet cant break the block, instead that, the player can buy the [item] for [sellPrice].
 */
class MarketEntry(
    val blockType: MaterialValue = MaterialValue(Material.AIR),
    val item: ItemValue,
    var baseValue: IntegerValue
) {
    private fun buyPrice(wallet: Wallet): Int {
        // TODO
        return baseValue.value()
    }

    private fun sellPrice(wallet: Wallet): Int {
        // TODO
        return baseValue.value()
    }

    fun canBuy(wallet: Wallet) = wallet.has(buyPrice(wallet))

    fun buy(wallet: Wallet, toAddInventory: PlayerInventory) {
        if (canBuy(wallet)) {
            wallet.remove(buyPrice(wallet))
            toAddInventory.addOrDrop(item.value())
            if (wallet.owner.isPlayerWallet()) {
                wallet.owner.sendMessage(
                    text(
                        "${item.value().amount}個の${item.value().type}を購入しました",
                        NamedTextColor.GREEN
                    )
                )
            } else {
                if (toAddInventory.holder is Player) {
                    wallet.owner.sendMessage(
                        (toAddInventory.holder!! as Player).displayName() +
                                text(
                                    " ${item.value().amount}個の${item.value().type}を購入しました",
                                    NamedTextColor.GREEN
                                )
                    )
                }
            }
        } else {
            // Should not happen
        }
    }

    /**
     * Sell the item
     * @return (whether the item is sold, the amount of money)
     */
    fun sell(wallet: Wallet, fromInventory: PlayerInventory): Pair<Boolean, Int> {
        val item = item.value()
        if (fromInventory.containsAtLeast(item, item.amount)) {
            val price = sellPrice(wallet)
            wallet.add(price)
            fromInventory.removeItem(item)
            return Pair(true, price)
        }

        return Pair(false, 0)
    }
}
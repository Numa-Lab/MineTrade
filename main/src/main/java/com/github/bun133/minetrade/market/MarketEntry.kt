package com.github.bun133.minetrade.market

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.config.ItemValue
import net.kunmc.lab.configlib.value.BooleanValue
import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.MaterialValue
import net.kyori.adventure.text.format.NamedTextColor
import org.apache.commons.lang.math.DoubleRange
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.java.JavaPlugin

/**
 * MarketEntry is a class that represents an entry of market.
 * @param item ItemStack that represents the item.
 * @param baseValue base price of the item.
 * @param blockType Material that represents the block of [item]. if [item] is not a block, this should be AIR. when this field is set, player who owns wallet cant break the block, instead that, the player can buy the [item] for [sellPrice].
 * @param isRealMode if the real value of [isRealMode] is true, the [sellPrice] and [buyPrice] will be calculated by [baseValue],[buyCount] and [sellCount]. if the real value of [isRealMode] is false, the [sellPrice] and [buyPrice] will be constant value * random.
 */
class MarketEntry(
    val blockType: MaterialValue = MaterialValue(Material.AIR),
    val item: ItemValue,
    var baseValue: IntegerValue,
    val isRealMode: BooleanValue,
    intervalTick:Int,
    plugin:JavaPlugin
) {
    private var buyCount = 0
    private var sellCount = 0
    private val rate = MarketPriceHelper(intervalTick, DoubleRange(0.5, 1.5),plugin)

    fun buyPrice(): Int {
        return if (isRealMode.value()) {
            baseValue.value()   // TODO: Add calculation
        } else {
            (baseValue.value() * rate.nowValue()).toInt()
        }
    }

    fun sellPrice(): Int {
        return if (isRealMode.value()) {
            baseValue.value()   // TODO: Add calculation
        } else {
            (baseValue.value() * rate.nowValue()).toInt()
        }
    }

    fun canBuy(wallet: Wallet) = wallet.has(buyPrice())

    fun buy(wallet: Wallet, toAddInventory: PlayerInventory) {
        if (canBuy(wallet)) {
            wallet.remove(buyPrice())
            toAddInventory.addOrDrop(item.value())
            buyCount++  // Count up buyCount
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
                        text("[${(toAddInventory.holder!! as Player).name}]", NamedTextColor.YELLOW) +
                                text(
                                    " ${item.value().amount}個の${item.value().type}を購入しました",
                                    NamedTextColor.GREEN
                                )

                    )
                }
            }
        } else {
            // Should not happen
            wallet.owner.sendMessage(
                text(
                    "お金が足りません(${buyPrice() - wallet.balance}不足しています",
                    NamedTextColor.RED
                )
            )
        }
    }

    /**
     * Sell the item
     * @return (whether the item is sold, the amount of money)
     */
    fun sell(wallet: Wallet, fromInventory: PlayerInventory): Pair<Boolean, Int> {
        val item = item.value()
        if (fromInventory.containsAtLeast(item, item.amount)) {
            val price = sellPrice()
            wallet.add(price)
            fromInventory.removeItem(item)
            sellCount++ // Count up sellCount
            return Pair(true, price)
        }

        return Pair(false, 0)
    }
}
package com.github.bun133.minetrade.market

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.config.ItemValue
import net.kunmc.lab.configlib.value.IntegerValue
import net.kunmc.lab.configlib.value.MaterialValue
import net.kyori.adventure.text.format.NamedTextColor
import org.apache.commons.lang.math.DoubleRange
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
    var baseValue: IntegerValue,
    private val plugin: Minetrade
) {
    private var buyCount = 0
    private var sellCount = 0
    private val rate = MarketPriceHelper(DoubleRange(0.5, 1.5), plugin)

    // TODO 前のゲームの値段が残っているので、リセットする必要がある
    fun buyPrice(): Int {
        return (baseValue.value() * rate.nowValue()).toInt()
    }

    fun sellPrice(): Int {
        return (baseValue.value() * rate.nowValue()).toInt()
    }

    fun canBuy(wallet: Wallet) = wallet.has(buyPrice())

    fun buy(
        wallet: Wallet,
        toAddInventory: PlayerInventory,
        muchAsPossible: Boolean
    ): Pair<Boolean, Int> {
        if (canBuy(wallet)) {
            val price = buyPrice()
            val amount = if (muchAsPossible) {
                (wallet.balance / price)
            } else {
                1
            }
            wallet.remove(price * amount)
            val addedItem = item.value().clone().apply { this.amount = this.amount * amount }
            toAddInventory.addOrDrop(addedItem)
            buyCount++  // Count up buyCount

            if (wallet.owner.isPlayerWallet()) {
                wallet.owner.sendMessage(
                    text(
                        "${addedItem.amount}個の${plugin.translator.getTranslated(item.material.value().translationKey)}を購入しました",
                        NamedTextColor.GREEN
                    )
                )
            } else {
                wallet.owner.sendMessage(
                    text("[${(toAddInventory.holder!! as Player).name}]", NamedTextColor.YELLOW) +
                            text(
                                " ${addedItem.amount}個の${plugin.translator.getTranslated(item.material.value().translationKey)}を購入しました",
                                NamedTextColor.GREEN
                            )

                )
            }
            return Pair(true, price * amount)
        } else {
            // Should not happen
            wallet.owner.sendMessage(
                text(
                    "お金が足りません(${buyPrice() - wallet.balance}不足しています",
                    NamedTextColor.RED
                )
            )
            return Pair(false, 0)
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
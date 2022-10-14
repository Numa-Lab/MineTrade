package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.config.MineTradeEntryConfig
import com.github.bun133.minetrade.config.fromItemStack
import dev.kotx.flylib.command.Command
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MarketAddCommand(config: MineTradeConfig) : Command("add") {
    init {
        description("マーケットに商品を追加します")
        usage {
            stringArgument("アイテム", suggestion = { suggestAll(Material.values().filter { it.isItem }.map { it.name }) })
            integerArgument("数量", min = 1, max = 64)
            integerArgument("ベース価格", min = 1)
            executes {
                val materialID = typedArgs[0] as String
                val material = Material.getMaterial(materialID)
                if (material == null) {
                    fail("そのアイテムは存在しません")
                    return@executes
                }
                val amount = typedArgs[1] as Int
                val price = typedArgs[2] as Int
                config.tradings.add(fromItemStack(ItemStack(material, amount), price))
                success("商品を追加しました")
            }
        }
    }
}

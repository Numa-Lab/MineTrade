package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.config.fromItemStack
import dev.kotx.flylib.command.Command
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class MarketAddCommand(config: MineTradeConfig) : Command("add") {
    init {
        description("マーケットに商品を追加します")
        usage {
            stringArgument("ブロック", suggestion = { suggestAll(Material.values().map { it.name }) })
            stringArgument("アイテム", suggestion = { suggestAll(Material.values().map { it.name }) })
            integerArgument("数量", min = 1, max = 64)
            integerArgument("ベース価格", min = 1)
            executes {
                val blockMaterialID = typedArgs[0] as String
                val blockMaterial = Material.getMaterial(blockMaterialID)
                if (blockMaterial == null) {
                    fail("そのブロックは存在しません")
                    return@executes
                }
                val itemMaterialID = typedArgs[1] as String
                val itemMaterial = Material.getMaterial(itemMaterialID)
                if (itemMaterial == null) {
                    fail("そのアイテムは存在しません")
                    return@executes
                }

                val amount = typedArgs[2] as Int
                val price = typedArgs[3] as Int
                config.addTrading(fromItemStack(blockMaterial, ItemStack(itemMaterial, amount), price))
                success("商品を追加しました")
            }
        }
    }
}

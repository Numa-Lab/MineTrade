package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import dev.kotx.flylib.command.Command
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ItemSetCommand : Command("itemset") {
    init {
        description("デフォルトのアイテムセットをプレイヤーに与えます")
        usage {
            entityArgument("Player", enableEntities = false)
            executes {
                val player = typedArgs as Entity
                if (player is Player) {
                    // Give Item to Player
                    val items = listOf<ItemStack>(
                        // TODO Add Items
                    )

                    player.inventory.addOrDrop(*items.toTypedArray())
                } else {
                    fail("指定されたPlayerが見つかりませんでした")
                }
            }
        }
    }
}
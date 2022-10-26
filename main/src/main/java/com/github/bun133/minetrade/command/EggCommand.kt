package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.trader.generateTraderSpawnItem
import dev.kotx.flylib.command.Command
import org.bukkit.entity.Player

class EggCommand : Command("egg") {
    init {
        usage {
            entityArgument("player", enableEntities = false)
            executes {
                val plist = typedArgs[0] as List<*>
                val players = plist.filterIsInstance<Player>()
                if (players.isEmpty()) {
                    fail("プレイヤーが見つかりませんでした")
                    return@executes
                } else {
                    players.forEach {
                        it.inventory.addOrDrop(generateTraderSpawnItem())
                    }
                    success("卵を配布しました")
                }
            }
        }
    }
}
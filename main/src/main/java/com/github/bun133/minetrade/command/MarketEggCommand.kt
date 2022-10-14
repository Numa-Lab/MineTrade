package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.trader.generateTraderSpawnItem
import dev.kotx.flylib.command.Command

class MarketEggCommand : Command("egg") {
    init {
        description("商人のスポーンアイテムを取得します")
        usage {
            selectionArgument("name","egg")
            executes {
                if (this.player != null) {
                    player!!.inventory.addOrDrop(generateTraderSpawnItem())
                    success("商人のスポーンアイテムを取得しました")
                } else {
                    fail("このコマンドはプレイヤーからのみ実行できます")
                }
            }
        }
    }
}
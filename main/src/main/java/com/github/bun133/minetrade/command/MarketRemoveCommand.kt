package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MarketRemoveCommand(config: MineTradeConfig) : Command("remove") {
    init {
        description("マーケットから商品を削除します")
        usage {
            integerArgument("番号", min = 1)
            executes {
                val index = typedArgs[0] as Int
                if (index > config.tradings.lastIndex + 1) {
                    fail("その番号の商品は存在しません")
                    return@executes
                }
                config.tradings.removeAt(index - 1)
                success("商品を削除しました")
            }
        }
    }
}

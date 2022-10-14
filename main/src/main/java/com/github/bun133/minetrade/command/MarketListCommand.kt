package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MarketListCommand(config: MineTradeConfig) : Command("list") {
    init {
        description("マーケットの商品一覧を表示します")
        usage {
            selectionArgument("","list")
            executes {
                val str = config.tradings.mapIndexed { i, it -> "[${i + 1}${it.prettyPrint()}]" }.joinToString("\n")
                success("========== 取引一覧 ==========")
                success(str)
            }
        }
    }
}

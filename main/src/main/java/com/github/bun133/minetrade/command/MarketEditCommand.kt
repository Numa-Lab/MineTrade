package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MarketEditCommand(config: MineTradeConfig) : Command("market") {
    init {
        description("マーケットの編集を行います")
        children(
            MarketAddCommand(config),
            MarketRemoveCommand(config),
            MarketPriceConfigCommand(config)
        )

        usage{
            selectionArgument("operation","list")
            executes{
                when(typedArgs[0] as String){
                    "list" -> {
                        val str = config.tradings.mapIndexed { i, it -> "[${i + 1}]${it.prettyPrint()}" }.joinToString("\n")
                        success("========== 取引一覧 ==========")
                        success(str)
                    }
                    else -> {
                        fail("その操作は存在しません")
                    }
                }
            }
        }
    }
}

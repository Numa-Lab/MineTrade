package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MarketPriceConfigCommand(config: MineTradeConfig) : Command("price") {
    init {
        description("マーケットの価格を設定します")
        usage {
            integerArgument("番号", min = 1)
            selectionArgument("モード", "real", "random")
            executes {
                val index = typedArgs[0] as Int
                val mode = typedArgs[1] as String

                val entry = config.tradings.getOrNull(index - 1)
                if (entry == null) {
                    fail("その番号の商品は存在しません")
                    return@executes
                }

                when(mode) {
                    "real" -> {
                        config.isRealMode.value(true)
                        success("価格をリアルモードに設定しました")
                    }
                    "random" -> {
                        config.isRealMode.value(false)
                        success("価格をランダムに設定しました")
                    }
                }
            }
        }
    }
}
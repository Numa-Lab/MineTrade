package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.Minetrade
import dev.kotx.flylib.command.Command

class StartCommand : Command("start") {
    init {
        description("Minetradeを開始します")
        usage {
            selectionArgument("","start")
            executes {
                // TODO Start Minetrade
                val p = this.plugin as Minetrade
                p.initMarket()
                success("ゲームを開始しました")
            }
        }
    }
}
package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MinetradeCommand(val config:MineTradeConfig) : Command("minetrade") {
    init {
        description("Minetradeのコマンドです")
        children(ItemSetCommand())
    }
}
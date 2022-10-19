package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import dev.kotx.flylib.command.Command

class MarketEditCommand(config: MineTradeConfig) : Command("market") {
    init {
        description("マーケットの編集を行います")
        children(
            MarketAddCommand(config),
            MarketRemoveCommand(config),
            MarketListCommand(config),
            MarketPriceConfigCommand(config)
        )
    }
}

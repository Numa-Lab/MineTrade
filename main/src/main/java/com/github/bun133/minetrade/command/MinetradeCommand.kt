package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.scoreboard.ScoreBoardManager
import dev.kotx.flylib.command.Command
import net.kunmc.lab.configlib.ConfigCommandBuilder

class MinetradeCommand(val config: MineTradeConfig,val scoreBoardManager: ScoreBoardManager) : Command("minetrade") {
    init {
        description("Minetradeのコマンドです")
        children(
            ItemSetCommand(),
            StartCommand(),
            ConfigCommandBuilder(config).build(),
            MarketEditCommand(config),
            MarketEggCommand(),
            WalletCommand(),
            ScoreBoardSelectCommand(scoreBoardManager)
        )
    }
}
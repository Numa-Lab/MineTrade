package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.market.WalletManager
import com.github.bun133.minetrade.scoreboard.ScoreBoardManager
import dev.kotx.flylib.command.Command
import net.kunmc.lab.configlib.ConfigCommandBuilder

class MinetradeCommand(
    val config: MineTradeConfig,
    scoreBoardManager: ScoreBoardManager,
    walletManager: WalletManager
) : Command("minetrade") {
    init {
        description("Minetradeのコマンドです")
        children(
            ConfigCommandBuilder(config).build(),
            MarketEditCommand(config),
            WalletCommand(),
            ScoreBoardSelectCommand(scoreBoardManager),
            TeamRegisterCommand(walletManager),
            EggCommand()
        )

        usage {
            selectionArgument("operation", "start")
            executes {
                when (typedArgs[0] as String) {
                    "start" -> {
                        val p = this.plugin as Minetrade
                        p.init()
                        success("ゲームを開始しました")
                    }

                    else -> {
                        fail("その操作は存在しません")
                    }
                }
            }
        }
    }
}
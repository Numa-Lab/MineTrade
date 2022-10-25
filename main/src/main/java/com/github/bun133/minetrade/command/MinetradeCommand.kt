package com.github.bun133.minetrade.command

import com.github.bun133.bukkitfly.stack.addOrDrop
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.market.WalletManager
import com.github.bun133.minetrade.scoreboard.ScoreBoardManager
import com.github.bun133.minetrade.trader.generateTraderSpawnItem
import dev.kotx.flylib.command.Command
import net.kunmc.lab.configlib.ConfigCommandBuilder

class MinetradeCommand(val config: MineTradeConfig, scoreBoardManager: ScoreBoardManager,walletManager: WalletManager) : Command("minetrade") {
    init {
        description("Minetradeのコマンドです")
        children(
            ConfigCommandBuilder(config).build(),
            MarketEditCommand(config),
            WalletCommand(),
            ScoreBoardSelectCommand(scoreBoardManager),
            TeamRegisterCommand(walletManager)
        )

        usage {
            selectionArgument("operation", "egg","start")
            executes {
                when(typedArgs[0] as String){
                    "egg" -> {
                        if (this.player != null) {
                            player!!.inventory.addOrDrop(generateTraderSpawnItem())
                            success("商人のスポーンアイテムを取得しました")
                        } else {
                            fail("このコマンドはプレイヤーからのみ実行できます")
                        }
                    }
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
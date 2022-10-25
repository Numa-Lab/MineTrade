package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.scoreboard.ScoreBoardManager
import com.github.bun133.minetrade.scoreboard.ScoreBoardSelector
import dev.kotx.flylib.command.Command
import org.bukkit.scoreboard.DisplaySlot

class ScoreBoardSelectCommand(val scoreBoardManager: ScoreBoardManager) : Command("scoreboard") {
    init {
        usage {
            selectionArgument("ScoreBoardType", "PlayerWallet", "TeamWallet", "Market")
            selectionArgument("DisplaySlot", *(DisplaySlot.values().map { it.name }.toTypedArray()))
            executes {
                val type = typedArgs[0] as String
                val slotString = typedArgs[1] as String
                try {
                    val slot = DisplaySlot.valueOf(slotString)
                    when (type) {
                        "PlayerWallet" -> scoreBoardManager.showScoreBoard(ScoreBoardSelector.PlayerWallet(slot))
                        "TeamWallet" -> scoreBoardManager.showScoreBoard(ScoreBoardSelector.TeamWallet(slot))
                        "Market" -> scoreBoardManager.showScoreBoard(ScoreBoardSelector.Market(slot))
                        else -> {
                            fail("スコアボードのタイプが不正です")
                            return@executes
                        }
                    }

                    success("スコアボードを${type}に変更しました")
                } catch (e: IllegalArgumentException) {
                    fail("不正なスロットです")
                }
            }
        }
    }
}
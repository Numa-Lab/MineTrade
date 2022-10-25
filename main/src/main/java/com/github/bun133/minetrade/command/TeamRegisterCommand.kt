package com.github.bun133.minetrade.command

import com.github.bun133.minetrade.market.WalletManager
import dev.kotx.flylib.command.Command
import org.bukkit.Bukkit

class TeamRegisterCommand(val walletManager: WalletManager) : Command("team") {
    init {
        description("チーム登録などのコマンドです")
        usage {
            selectionArgument("operation", "add", "remove")
            stringArgument(
                "teamName",
                suggestion = { Bukkit.getServer().scoreboardManager.mainScoreboard.teams.map { it.name } })
            executes {
                val operation = typedArgs[0] as String
                val teamName = typedArgs[1] as String
                val team = Bukkit.getServer().scoreboardManager.mainScoreboard.getTeam(teamName)
                when (operation) {
                    "add" -> {
                        if (team != null) {
                            walletManager.registerTeamWallet(team, true)
                            success("チームを追加しました")
                        } else {
                            fail("そのチームは存在しません")
                        }
                    }

                    "remove" -> {
                        if (team != null) {
                            walletManager.removeTeamWallet(team)
                            success("チームを削除しました")
                        } else {
                            fail("そのチームは存在しません")
                        }
                    }

                    else -> {
                        fail("その操作は存在しません")
                    }
                }
            }
        }
    }
}
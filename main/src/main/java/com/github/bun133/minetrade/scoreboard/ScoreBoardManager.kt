package com.github.bun133.minetrade.scoreboard

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.market.MarketPriceHelperTimer
import com.github.bun133.minetrade.market.WalletManager
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot
import kotlin.math.roundToInt

sealed class ScoreBoardSelector {
    data class PlayerWallet(val displaySlot: DisplaySlot) : ScoreBoardSelector()
    data class TeamWallet(val displaySlot: DisplaySlot) : ScoreBoardSelector()
    data class Market(val displaySlot: DisplaySlot) : ScoreBoardSelector()
}

class ScoreBoardManager(plugin: Minetrade) {
    private val playerWalletScoreBoard = PlayerWalletScoreBoard(plugin.walletManager)
    private val teamWalletScoreBoard = TeamWalletScoreBoard(plugin.walletManager)
    private val marketScoreBoard = MarketScoreBoard(plugin)

    fun updateScoreBoard() {
        playerWalletScoreBoard.update()
        teamWalletScoreBoard.update()
        marketScoreBoard.update()
    }

    fun resetScoreBoard() {
        playerWalletScoreBoard.reset()
        teamWalletScoreBoard.reset()
        marketScoreBoard.reset()
    }

    fun showScoreBoard(selector: ScoreBoardSelector) {
        when (selector) {
            is ScoreBoardSelector.PlayerWallet -> {
                playerWalletScoreBoard.show(selector.displaySlot)
            }

            is ScoreBoardSelector.TeamWallet -> {
                teamWalletScoreBoard.show(selector.displaySlot)
            }

            is ScoreBoardSelector.Market -> {
                marketScoreBoard.show(selector.displaySlot)
            }
        }
    }
}

class PlayerWalletScoreBoard(private val walletManager: WalletManager) {
    private var slot: DisplaySlot? = null
    fun update() {
        val targets = Bukkit.getOnlinePlayers().toMutableList()

        // プレイヤーのウォレット金額表示を更新する
        walletManager.wallets.filterKeys { it.isPlayerWallet() }.forEach { (owner, wallet) ->
            val score = getObjective().getScore(owner.stringName())
            score.score = wallet.balance

            targets.remove(owner.player)
        }

        // いなくなったプレイヤーのスコアを削除する
        targets.forEach { p ->
            getObjective().getScore(p.name).score = 0
        }

        // チームモードでもTabのところに表示できるようにする
        walletManager.wallets.filterKeys { it.isTeamWallet() }.forEach { (owner, wallet) ->
            owner.players().forEach { player ->
                val score = getObjective().getScore(player.name)
                score.score = wallet.balance
            }
        }
    }

    fun reset() {
        getObjective().unregister()
        getObjective().displaySlot = slot
    }

    fun show(displaySlot: DisplaySlot) {
        getObjective().displaySlot = displaySlot
        slot = displaySlot
    }

    companion object {
        private fun genObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective("playerwallet", "dummy", text("所持金"))

        private fun getObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.getObjective("playerwallet") ?: genObjective()
    }
}

class TeamWalletScoreBoard(private val walletManager: WalletManager) {
    private var slot: DisplaySlot? = null
    fun update() {
        val target = Bukkit.getScoreboardManager().mainScoreboard.teams.toMutableList()

        walletManager.wallets.filterKeys { it.isTeamWallet() }.forEach { (owner, wallet) ->
            val score = getObjective().getScore(owner.stringName())
            score.score = wallet.balance

            target.remove(owner.team)
        }

        // いなくなったチームのスコアを削除する
        target.forEach { team ->
            getObjective().getScore(team.name).score = 0
        }
    }

    fun reset() {
        getObjective().unregister()
        getObjective().displaySlot = slot
    }

    fun show(displaySlot: DisplaySlot) {
        getObjective().displaySlot = displaySlot
        slot = displaySlot
    }

    companion object {
        private fun genObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective("teamwallet", "dummy", text("チーム所持金"))

        private fun getObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.getObjective("teamwallet") ?: genObjective()
    }
}

class MarketScoreBoard(private val plugin: Minetrade) {
    private var slot: DisplaySlot? = null
    fun update() {
        plugin.market?.entries()?.forEach { e ->
            val score = getObjective().getScore(e.item.material.value().name)
            score.score = e.buyPrice()
        }

        // Update Title
        getObjective().displayName(
            text("市場価格 ") + text(
                "更新まで:${
                    (MarketPriceHelperTimer.getInstance(plugin).remainTick() / 20.0).roundToInt()
                }秒", NamedTextColor.GRAY
            )
        )
    }

    fun reset() {
        getObjective().unregister()
        getObjective().displaySlot = slot
    }

    fun show(displaySlot: DisplaySlot) {
        getObjective().displaySlot = displaySlot
        slot = displaySlot
    }

    companion object {
        private fun genObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective("market", "dummy", text("取引価格"))

        private fun getObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.getObjective("market") ?: genObjective()
    }
}
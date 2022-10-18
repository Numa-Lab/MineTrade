package com.github.bun133.minetrade.scoreboard

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.market.WalletManager
import org.bukkit.Bukkit
import org.bukkit.scoreboard.DisplaySlot

sealed class ScoreBoardSelector {
    data class Wallet(val displaySlot: DisplaySlot) : ScoreBoardSelector()
    data class Market(val displaySlot: DisplaySlot) : ScoreBoardSelector()
}

class ScoreBoardManager(plugin: Minetrade) {
    private val walletScoreBoard = WalletScoreBoard(plugin.walletManager)
    private val marketScoreBoard = MarketScoreBoard(plugin)

    fun updateScoreBoard() {
        walletScoreBoard.update()
        marketScoreBoard.update()
    }

    fun resetScoreBoard() {
        walletScoreBoard.reset()
        marketScoreBoard.reset()
    }

    fun showScoreBoard(selector: ScoreBoardSelector) {
        when (selector) {
            is ScoreBoardSelector.Wallet -> {
                walletScoreBoard.show(selector.displaySlot)
            }

            is ScoreBoardSelector.Market -> {
                marketScoreBoard.show(selector.displaySlot)
            }
        }
    }
}

class WalletScoreBoard(private val walletManager: WalletManager) {
    fun update() {
        walletManager.wallets.forEach { (owner, wallet) ->
            val score = getObjective().getScore(owner.stringName())
            score.score = wallet.balance
        }
    }

    fun reset() {
        getObjective().unregister()
    }

    fun show(displaySlot: DisplaySlot) {
        getObjective().displaySlot = displaySlot
    }

    companion object {
        private fun genObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective("wallet", "dummy", text("所持金"))

        private fun getObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.getObjective("wallet") ?: genObjective()
    }
}

class MarketScoreBoard(private val plugin: Minetrade) {
    fun update() {
        plugin.market?.entries()?.forEach { e ->
            val score = getObjective().getScore(e.item.material.value().name)
            score.score = e.buyPrice()
        }
    }

    fun reset() {
        getObjective().unregister()
    }

    fun show(displaySlot: DisplaySlot) {
        getObjective().displaySlot = displaySlot
    }

    companion object {
        private fun genObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.registerNewObjective("market", "dummy", text("市場価格"))

        private fun getObjective() =
            Bukkit.getScoreboardManager().mainScoreboard.getObjective("market") ?: genObjective()
    }
}
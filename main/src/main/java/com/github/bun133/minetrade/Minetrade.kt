package com.github.bun133.minetrade

import com.github.bun133.minetrade.command.MinetradeCommand
import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.market.Market
import com.github.bun133.minetrade.market.WalletManager
import com.github.bun133.minetrade.scoreboard.ScoreBoardManager
import com.github.bun133.minetrade.trader.TraderEventHelper
import com.github.bun133.minetrade.trader.TraderSpawnItemEventHelper
import com.github.bun133.minetrade.translate.Translator
import dev.kotx.flylib.flyLib
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class Minetrade : JavaPlugin() {
    lateinit var config: MineTradeConfig
    var market: Market? = null
        private set
    val walletManager = WalletManager()
    lateinit var scoreBoardManager: ScoreBoardManager
    lateinit var translator: Translator

    override fun onEnable() {
        // start Translator
        translator = Translator(getResource("ja_jp.json")!!)
        // Plugin startup logic
        config = MineTradeConfig(this)
        config.saveConfigIfAbsent()
        config.loadConfig()

        scoreBoardManager = ScoreBoardManager(this,translator)

        initHelperClasses()

        flyLib {
            command(MinetradeCommand(config, scoreBoardManager, walletManager))
        }
    }

    override fun onDisable() {
        config.saveConfigIfPresent()
    }

    private fun initHelperClasses() {
        TraderEventHelper(this)
        TraderSpawnItemEventHelper(this)
        DigHelper(this)

        Bukkit.getServer().scheduler.runTaskTimer(this, Runnable {
            scoreBoardManager.updateScoreBoard()    // Update ScoreBoard
        }, 0, 1)
    }

    fun init() {
        market = Market(config, this)
        scoreBoardManager.resetScoreBoard()
    }
}
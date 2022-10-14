package com.github.bun133.minetrade

import com.github.bun133.minetrade.command.MinetradeCommand
import com.github.bun133.minetrade.config.MineTradeConfig
import com.github.bun133.minetrade.market.Market
import com.github.bun133.minetrade.market.WalletManager
import com.github.bun133.minetrade.trader.TraderEventHelper
import com.github.bun133.minetrade.trader.TraderSpawnItemEventHelper
import dev.kotx.flylib.flyLib
import org.bukkit.plugin.java.JavaPlugin

class Minetrade : JavaPlugin() {
    lateinit var config: MineTradeConfig
    var market: Market? = null
        private set
    val walletManager = WalletManager()

    override fun onEnable() {
        // Plugin startup logic
        config = MineTradeConfig(this)
        config.saveConfigIfAbsent()
        config.loadConfig()

        initHelperClasses()

        flyLib {
            command(MinetradeCommand(config))
        }
    }

    override fun onDisable() {
        config.saveConfigIfPresent()
    }

    private fun initHelperClasses() {
        TraderEventHelper(this)
        TraderSpawnItemEventHelper(this)
    }

    fun initMarket() {
        market = Market(config.tradings)// TODO Init With Config
    }
}
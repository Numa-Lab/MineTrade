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
    lateinit var market: Market // TODO Init With Config
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

    private fun initHelperClasses() {
        TraderEventHelper(this)
        TraderSpawnItemEventHelper(this)
    }
}
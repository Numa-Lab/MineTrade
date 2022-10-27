package com.github.bun133.minetrade.market

import org.apache.commons.lang.math.DoubleRange
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

/**
 * @param intervalTick is the interval of price change.
 * @param range is the range of price change rate.
 */
class MarketPriceHelper(var range: DoubleRange, plugin: JavaPlugin) {
    private var value: Double = random()

    init {
        MarketPriceHelperTimer.getInstance(plugin).add(this)
    }

    fun nowValue() = value

    private fun random(): Double {
        return Random.nextDouble(range.minimumDouble, range.maximumDouble)
    }

    internal fun update() {
        value = random()
    }
}

const val INTERVAL_TICK: Long = 20L * 30L

class MarketPriceHelperTimer private constructor(plugin: JavaPlugin) {
    companion object {
        private var timer: MarketPriceHelperTimer? = null
        fun getInstance(plugin: JavaPlugin): MarketPriceHelperTimer {
            if (timer == null) {
                timer = MarketPriceHelperTimer(plugin)
            }
            return timer!!
        }
    }

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            update()
        }, 0, INTERVAL_TICK)
    }

    private val helpers = mutableListOf<MarketPriceHelper>()
    private var lastUpdateTime = 0

    private fun update() {
        lastUpdateTime = Bukkit.getServer().currentTick
        helpers.forEach { it.update() }
    }

    fun add(helper: MarketPriceHelper) {
        helpers.add(helper)
    }

    fun remainTick(): Long {
        if (helpers.isEmpty()) return INTERVAL_TICK
        return (lastUpdateTime + INTERVAL_TICK - Bukkit.getServer().currentTick) % INTERVAL_TICK
    }
}
package com.github.bun133.minetrade.market

import org.apache.commons.lang.math.DoubleRange
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

/**
 * @param intervalTick is the interval of price change.
 * @param range is the range of price change rate.
 */
class MarketPriceHelper(var intervalTick: Int, var range: DoubleRange, plugin: JavaPlugin) {
    private var value = random()

    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            value = random()
        }, intervalTick.toLong(), intervalTick.toLong())
    }

    fun nowValue() = value

    private fun random(): Double {
        return Random.nextDouble(range.minimumDouble, range.maximumDouble)
    }
}
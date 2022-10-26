package com.github.bun133.minetrade.bossbar

import com.github.bun133.bukkitfly.component.plus
import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import com.github.bun133.minetrade.market.Wallet
import com.github.bun133.minetrade.market.WalletOwner
import net.kunmc.lab.configlib.value.IntegerValue
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

class BossBarManager(private val plugin: Minetrade, private val targetMoney: IntegerValue) {
    init {
        Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            if (plugin.market != null) {
                update()
            }
        }, 0, 1)
    }

    private val bossBars = mutableMapOf<WalletOwner, BossBar>()
    private var isDisabled = false

    fun disable() {
        // TODO 時々消えない
        bossBars.values.forEach {
            it.removeAll()
            it.isVisible = false
        }
        bossBars.clear()
        isDisabled = true
    }

    fun enable() {
        if (!isDisabled) disable()  // If not disabled, disable it.
        isDisabled = false
    }

    private fun update() {
        if (isDisabled) return
        Bukkit.getOnlinePlayers().forEach { p ->
            val wallet = plugin.walletManager.getWallet(p)
            val bossBar = bossBars[wallet.owner] ?: genBossBar(wallet.owner)

            updateBossBar(bossBar, wallet)
            show(bossBar, p)
        }
    }

    private fun updateBossBar(bossBar: BossBar, wallet: Wallet) {
        val progress = min(1.0, max(0.0, wallet.balance.toDouble() / targetMoney.value().toDouble()))    // 0.0 ~ 1.0
        bossBar.progress = progress
        bossBar.setTitle("${wallet.owner.stringName()} : ${wallet.balance} / ${targetMoney.value()}") // Update title
        if (progress >= 1.0) {
            onFinish(wallet.owner, wallet, plugin)
            disable()
        }
    }

    private fun show(bossBar: BossBar, p: Player) {
        bossBar.addPlayer(p)
        // remove player from other boss bars
        bossBars.values.filter { it != bossBar }.forEach {
            it.removePlayer(p)
        }
    }

    private fun genBossBar(owner: WalletOwner): BossBar {
        val bar =
            Bukkit.createBossBar(
                NamespacedKey(plugin, owner.stringName()),
                "${owner.stringName()} : 0 / ${targetMoney.value()}",
                BarColor.YELLOW,
                BarStyle.SEGMENTED_20
            )
        bossBars[owner] = bar
        return bar
    }
}

/**
 * 目標金額を超えた時の処理
 */
fun onFinish(owner: WalletOwner, wallet: Wallet, plugin: Minetrade) {
    Bukkit.getOnlinePlayers().forEach {
        it.showTitle(
            Title.title(
                text("ゲーム終了!"),
                text(owner.stringName(), NamedTextColor.BLUE) + text("の勝利!", NamedTextColor.WHITE)
            )
        )
    }
}
package com.github.bun133.minetrade.market

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class WalletOwner(val player: Player?, val team: Team?) {
    init {
        assert(player != null || team != null)  // Either player or team must be not null
    }

    fun isPlayerWallet(): Boolean {
        return player != null
    }

    fun isTeamWallet(): Boolean {
        return team != null
    }

    fun isOwningWallet(player: Player): Boolean {
        return if (isPlayerWallet()) {
            this.player!!.uniqueId == player.uniqueId
        } else {
            this.team!!.hasEntry(player.name)
        }
    }

    fun exactEquals(other: WalletOwner): Boolean {
        return if (isPlayerWallet()) {
            other.isPlayerWallet() && this.player!!.uniqueId == other.player!!.uniqueId
        } else {
            other.isTeamWallet() && this.team!!.name == other.team!!.name
        }
    }

    fun sendMessage(comp: Component) {
        if (isPlayerWallet()) {
            player!!.sendMessage(comp)
        } else {
            team!!.entries.mapNotNull { Bukkit.getPlayer(it) }.forEach { it.sendMessage(comp) }
        }
    }

    fun name(): Component {
        return if (isPlayerWallet()) {
            player!!.displayName()
        } else {
            team!!.displayName()
        }
    }
}

class Wallet(val owner: WalletOwner) {
    var balance = 0
        private set

    fun add(amount: Int) {
        balance += amount
    }

    fun remove(amount: Int) {
        balance -= amount
    }

    fun has(amount: Int): Boolean {
        return balance >= amount
    }

    fun set(amount: Int) {
        balance = amount
    }
}
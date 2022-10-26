package com.github.bun133.minetrade.market

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class WalletManager {
    val wallets = mutableMapOf<WalletOwner, Wallet>()

    fun reset() {
        wallets.forEach { (_, wallet) ->
            wallet.set(0)   // Set to 0
        }
    }

    /**
     * @param toMarge whether to marge the wallet if the wallets owned by team player are already exists.
     */
    fun registerTeamWallet(t: Team, toMarge: Boolean): Wallet {
        val players = t.entries.mapNotNull { Bukkit.getPlayer(it) }
        val playerWallets = players.mapNotNull { getExistingWalletFor(it) }
        val teamWallet = Wallet(WalletOwner(null, t))

        // marge and remove
        if (playerWallets.isNotEmpty()) {
            if (toMarge) {
                playerWallets.forEach {
                    teamWallet.add(it.balance)
                    wallets.remove(it.owner)
                }
            } else {
                playerWallets.forEach {
                    wallets.remove(it.owner)
                }
            }
        }

        wallets[teamWallet.owner] = teamWallet

        return teamWallet
    }

    fun removeTeamWallet(t: Team) {
        wallets.filter { it.key.isTeamWallet() && it.key.team!!.name == t.name }.toList().forEach {
            wallets.remove(it.first)
        }
    }

    fun getWallet(p: Player): Wallet {
        val w = getExistingWalletFor(p)
        if (w != null) return w
        return Wallet(WalletOwner(p, null)).also {
            wallets[WalletOwner(p, null)] = it
        }
    }

    fun getWallet(t: Team): Wallet {
        val w = getExistingWalletFor(t)
        if (w != null) return w
        return Wallet(WalletOwner(null, t)).also {
            wallets[WalletOwner(null, t)] = it
        }
    }

    private fun getExistingWalletFor(p: Player): Wallet? {
        val joiningTeam = Bukkit.getScoreboardManager().mainScoreboard.getEntryTeam(p.name)
        if (joiningTeam != null) {
            val w = getExistingWalletFor(joiningTeam)
            if (w != null) return w
        }

        return wallets.entries.firstOrNull {
            it.key.isOwningWallet(p)
        }?.value
    }

    private fun getExistingWalletFor(t: Team): Wallet? {
        return wallets.entries.firstOrNull {
            it.key.exactEquals(WalletOwner(null, t))
        }?.value
    }
}

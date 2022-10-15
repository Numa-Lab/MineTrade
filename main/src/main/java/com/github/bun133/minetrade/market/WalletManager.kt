package com.github.bun133.minetrade.market

import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class WalletManager {
    val wallets = mutableMapOf<WalletOwner, Wallet>()

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

    fun getExistingWalletFor(p: Player): Wallet? {
        return wallets.entries.firstOrNull {
            it.key.isOwningWallet(p)
        }?.value
    }

    fun getExistingWalletFor(t: Team): Wallet? {
        return wallets.entries.firstOrNull {
            it.key.exactEquals(WalletOwner(null, t))
        }?.value
    }
}

package com.github.bun133.minetrade.trader

import com.github.bun133.bukkitfly.component.text
import com.github.bun133.minetrade.Minetrade
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

fun generateTraderSpawnItem(): org.bukkit.inventory.ItemStack {
    val item = org.bukkit.inventory.ItemStack(org.bukkit.Material.VILLAGER_SPAWN_EGG)
    item.editMeta {
        it.displayName(text("商人のスポーンエッグ"))
        it.lore(listOf(text("地面に向かって右クリックで商人をスポーンさせます")))
        it.persistentDataContainer[TraderSpawnItemEventHelper.KEY, org.bukkit.persistence.PersistentDataType.STRING] =
            "true"
    }
    return item
}

fun isTraderSpawnItem(item: org.bukkit.inventory.ItemStack): Boolean {
    return item.type == Material.VILLAGER_SPAWN_EGG && item.hasItemMeta() && item.itemMeta!!.persistentDataContainer.has(
        TraderSpawnItemEventHelper.KEY,
        org.bukkit.persistence.PersistentDataType.STRING
    )
}

class TraderSpawnItemEventHelper(val plugin: Minetrade) : Listener {
    companion object {
        val KEY = NamespacedKey("minetrade", "trader_spawn_item")
    }

    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            val item = event.item
            if (item != null && isTraderSpawnItem(item)) {
                val block = event.clickedBlock
                if (block != null) {
                    val loc = block.location
                    loc.y += 1
                    spawnTraderAtLocation(loc, plugin.market, plugin)
                    event.isCancelled = true
                    event.player.sendMessage(text("商人をスポーンさせました", NamedTextColor.GREEN))
                }
            }
        }
    }
}
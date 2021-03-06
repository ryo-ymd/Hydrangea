package life.grass.hydrangea.listener

import life.grass.hydrangea.Hydrangea
import life.grass.hydrangea.hotbar.choosePhantomHotbarSlot
import life.grass.hydrangea.hotbar.closePhantomHotbar
import life.grass.hydrangea.hotbar.isOpeningHotbarSlot
import life.grass.hydrangea.hotbar.openPhantomHotbar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent

/**
 * @author BlackBracken
 */
class PhantomHotbarListener : Listener {

    @EventHandler
    fun onSwapHandItems(event: PlayerSwapHandItemsEvent) {
        val player = event.player

        if (player.isOpeningHotbarSlot()) {
            event.isCancelled = true
            player.closePhantomHotbar()

            return
        }

        val openedPhantomHotbar = Hydrangea.instance.hotbarRegistererSet
                .find { registerer -> registerer.filter(event) }
                ?: return

        event.isCancelled = true
        player.openPhantomHotbar(openedPhantomHotbar.instantiate(event))
    }

    @EventHandler
    fun onInventoryOpen(event: InventoryOpenEvent) {
        (event.player as? Player)?.closePhantomHotbar()
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        (event.whoClicked as? Player)?.closePhantomHotbar()
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val hotbarSlotIndex = player.inventory.heldItemSlot

        if (event.action in setOf(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK) && player.isOpeningHotbarSlot()) {
            event.isCancelled = true
            player.choosePhantomHotbarSlot(hotbarSlotIndex)
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.player.closePhantomHotbar()
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.entity.closePhantomHotbar()
    }

}
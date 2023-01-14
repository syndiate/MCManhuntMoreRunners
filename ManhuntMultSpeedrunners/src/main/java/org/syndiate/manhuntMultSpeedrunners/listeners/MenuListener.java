package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		
		if (!(e.getView().getTitle().equalsIgnoreCase(Main.TITLE_COLOR + "Speedrunners"))) return;
		if (e.getCurrentItem() == null) return;
		
		Player hunter = (Player) e.getWhoClicked();
		Player runner = hunter.getServer().getPlayer(e.getCurrentItem().getItemMeta().getDisplayName());
		
		Main.trackPlayer(hunter, runner);
		e.setCancelled(true);
		hunter.closeInventory();
		
	}

}

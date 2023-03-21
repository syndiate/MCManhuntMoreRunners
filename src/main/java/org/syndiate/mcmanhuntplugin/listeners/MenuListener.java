package org.syndiate.mcmanhuntplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.SkullMeta;
import org.syndiate.mcmanhuntplugin.Main;

public class MenuListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {
		
		if (e == null) {
			return;
		}
		if (e.getCurrentItem() == null) {
			return;
		}
		if (!(e.getView().getTitle().equalsIgnoreCase(Main.TITLE_COLOR + "Speedrunners"))) {
			return;
		}
		
		
		
		
		Player hunter = (Player) e.getWhoClicked();
		
		SkullMeta runnerHead = ((SkullMeta) e.getCurrentItem().getItemMeta());
		Player runner = Main.supportsModernHeads ? Bukkit.getPlayer(runnerHead.getOwningPlayer().getUniqueId())
												 : Bukkit.getPlayer(runnerHead.getOwner());
		
		
		
		
		Main.trackPlayer(hunter, runner);
		e.setCancelled(true);
		hunter.closeInventory();
		
	}

}

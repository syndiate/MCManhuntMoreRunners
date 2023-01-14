package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class CompassListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		if (Main.manhuntEnded) return;
		if (event.getMaterial() != Material.COMPASS) return;
		
			
		Player p = event.getPlayer();
		Action act = event.getAction();
		
			
		for (Player hunter : Main.HunterList) {
				
			if (!hunter.equals(p)) continue;
			
			if (act.equals(Action.RIGHT_CLICK_AIR) || act.equals(Action.RIGHT_CLICK_BLOCK)) {
				p.openInventory(Main.runnerMenu);
			}
			else if (act.equals(Action.PHYSICAL)) {
				
				
				if (Main.HunterTracking.get(hunter) == null) {
					hunter.sendMessage(Main.ERROR_COLOR + "Please select which speedrunner you'd like to track by either using the compass menu (right-click to access) or /track <playerName>.");
					return;
				}
				Main.trackPlayer(hunter, Main.HunterTracking.get(hunter));
				
				
			}
			return;
					
		}
	}
}

package org.syndiate.mcmanhuntplugin.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.syndiate.mcmanhuntplugin.Main;

public class CompassListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		
		
		Player p = event.getPlayer();
		Action act = event.getAction();
		
		
		if (Main.manhuntEnded || event == null) {
			return;
		}
		if (event.getItem() == null) {
			return;
		}
		if (event.getItem().getType() != Material.COMPASS) {
			return;
		}
		if (!Main.HunterList.contains(p)) {
			return;
		}
		event.setCancelled(true);
		
		
		

		
		
		if (act.equals(Action.RIGHT_CLICK_AIR) || act.equals(Action.RIGHT_CLICK_BLOCK)) {
			Main.openCompassMenu(p);
		}
		else if (act.equals(Action.PHYSICAL) || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
				
			if (Main.HunterTracking.get(p) == null) {
				p.sendMessage(Main.ERROR_COLOR + "Please select which speedrunner you'd like to track by either using the compass menu (right-click to access) or /track <playerName>.");
				return;
			}
			Main.trackPlayer(p, Main.HunterTracking.get(p));
					
		}
		
		
	}
}

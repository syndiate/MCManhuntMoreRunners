package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class CompassListener implements Listener {
	
	@EventHandler
	public void onClick(PlayerInteractEvent event, EntityPortalEvent PortalEvent) {
		if (!Main.manhuntEnded) {
			
			Player p = event.getPlayer();
			Action act = event.getAction();
			
			for (Player hunter : Main.HunterList) {
				
				if (!hunter.equals(p)) continue;
				if (event.getMaterial() == Material.COMPASS) {
					
					Environment hunterEnv = hunter.getWorld().getEnvironment();
					
					if (act.equals(Action.RIGHT_CLICK_AIR) || act.equals(Action.RIGHT_CLICK_BLOCK)) {
						
					}
					
				}
			}
			
			
		}
	}

}

package org.syndiate.mcmanhuntplugin.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.syndiate.mcmanhuntplugin.Main;

public class PortalListener implements Listener {

	@EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
		
		
		if (Main.manhuntEnded || event.getEntity().getType() != EntityType.PLAYER) {
			return; 
		}
		Player p = (Player) event.getEntity();
		
		
		Location from = event.getFrom();
        World.Environment fromEnv = from.getWorld().getEnvironment();
        
        
        if (fromEnv == World.Environment.NORMAL) {
            Main.putPortalEntrance(p, from);
            // Store exit portal location so player can track their own portal
//            Main.putPortalExit(p, to);
        } else {
            // Player is leaving the Nether/End, so remove their entrance portal location
            Main.clearPortalEntrance(p);
            Main.putPortalExit(p, from);
        }
        
        /*
		if (!Main.RunnerList.contains(p)) {
			return;
		}
		
		
		Main.RunnerPortals.remove(p);
		Main.RunnerPortals.put(p, event.getFrom());*/

    }
	

}
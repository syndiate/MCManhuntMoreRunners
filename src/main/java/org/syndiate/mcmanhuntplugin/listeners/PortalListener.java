package org.syndiate.mcmanhuntplugin.listeners;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.syndiate.mcmanhuntplugin.Main;

public class PortalListener implements Listener {

	@EventHandler
    public void onEntityPortal(PlayerPortalEvent event) {
		
		if (Main.manhuntEnded) {
			return;
		}
		Player p = event.getPlayer();
		
		
		Location from = event.getFrom();
		Location to = event.getTo();
        Environment fromEnv = from.getWorld().getEnvironment();

        if (fromEnv == Environment.NORMAL) {
        	Main.putPortalEntrance(p, from);
        	Main.putPortalExit(p, to);
        } else {
        	Main.clearPortalExit(p);
        	Main.putPortalEntrance(p, from);
        }

    }
	

}

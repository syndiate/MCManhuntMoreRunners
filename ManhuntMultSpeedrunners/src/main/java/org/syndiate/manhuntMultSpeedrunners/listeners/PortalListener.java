package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class PortalListener implements Listener {

	@EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
		
		if (!(event.getEntity() instanceof Player)) return;
		Player p = (Player) event.getEntity();
		
		for (Player runner : Main.RunnerList) {
			
			if (!runner.equals(p)) continue;
			Main.RunnerPortals.put(p, event.getFrom());
			
		}

    }
	

}

package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class PortalListener implements Listener {

	@EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
		
		if (Main.manhuntEnded) return; 
		if (!(event.getEntity() instanceof Player)) return;
		
		Player p = (Player) event.getEntity();
		
		
		if (!Main.RunnerList.contains(p)) return;
		Main.RunnerPortals.remove(p);
		Main.RunnerPortals.put(p, event.getFrom());

    }
	

}

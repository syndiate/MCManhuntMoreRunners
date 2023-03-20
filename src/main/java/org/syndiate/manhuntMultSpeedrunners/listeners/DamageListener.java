package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class DamageListener implements Listener {

    @EventHandler
    public void theEndDamageEvent(EntityDamageByEntityEvent e) {
    	
    	
    	if (Main.manhuntEnded || (e.getEntityType() != EntityType.ENDER_DRAGON && e.getEntityType() != EntityType.ENDER_CRYSTAL)) {
    		return;
    	}
    	if (e.getDamager().getType() != EntityType.PLAYER) {
    		return;
    	}
    	if (!Main.HunterList.contains((Player) e.getDamager())) {
    		return;
    	}

    	e.setCancelled(true);
         
    }

}

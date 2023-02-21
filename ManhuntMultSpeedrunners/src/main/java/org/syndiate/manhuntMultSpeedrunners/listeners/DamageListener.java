package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class DamageListener implements Listener {

    @EventHandler
    public void hunterDragonDamageEvent(EntityDamageByEntityEvent e) {
    	
    	if (!Main.manhuntEnded) return;
    	
    	if (e.getEntityType() != EntityType.ENDER_DRAGON) return;
    	if (e.getDamager().getType() != EntityType.PLAYER) return;
    	
    	if (!Main.HunterList.contains((Player) e.getDamager())) return;

         e.setCancelled(true);
         
    }

    public void hunterCrystalDamageEvent(EntityDamageByEntityEvent e) {
    	
    	if (!Main.manhuntEnded) return;
    	
    	if (e.getEntityType() != EntityType.ENDER_CRYSTAL) return;
    	if (e.getDamager().getType() != EntityType.PLAYER) return;
    	
    	if (!Main.HunterList.contains((Player) e.getDamager())) return;
        
        e.setCancelled(true);
        
    }

}

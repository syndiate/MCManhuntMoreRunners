package org.syndiate.manhuntMultSpeedrunners.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class RespawnListener implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		
		Player respawnedPlayer = e.getPlayer();
		for (Player p : Main.HunterList) if (respawnedPlayer.equals(p)) Main.giveCompass(p);
		
	}
	
}
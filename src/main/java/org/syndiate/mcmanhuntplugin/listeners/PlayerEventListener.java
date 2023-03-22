package org.syndiate.mcmanhuntplugin.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.syndiate.mcmanhuntplugin.Main;

public class PlayerEventListener implements Listener {
	
	
	
	
	private void victoryMsg(ArrayList<Player> playerList) {
		for (Player player : playerList) {
			player.sendTitle(Main.VICTORY_COLOR + "VICTORY!", "", 10, 100, 20);
			player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
		}
	}
	
	private void defeatMsg(ArrayList<Player> playerList) {
		for (Player player : playerList) {
			player.sendTitle(Main.DEFEAT_COLOR + "GAME OVER", "", 10, 100, 20);
			player.playSound(player.getLocation(), Sound.AMBIENT_CAVE, 1, 1);
		}
	}
	
	
	@EventHandler
	public void onDeath(EntityDeathEvent deadEntity) {
		
		if (Main.manhuntEnded) {
			return;
		}
			
		Server server = deadEntity.getEntity().getServer();
			
		if (deadEntity.getEntityType() == EntityType.PLAYER) {
				
				
			Player deadPlayer = (Player) deadEntity.getEntity();
			if (!Main.RunnerList.contains(deadPlayer)) {
				return;
			}
			
			
			Main.killRunner(deadPlayer);
			
			if (Main.RunnerList.size() > 0) {
				return;
			}
					
			Main.manhuntEnded = true;
			server.broadcastMessage(Main.VICTORY_COLOR + "The hunters have won the manhunt!");
					
			victoryMsg(Main.HunterList);
			defeatMsg(Main.DeadRunnerList);
					
			Main.HunterList.clear();
			Main.RunnerList.clear();
			Main.DeadRunnerList.clear();
				
				
				
		} else if (deadEntity.getEntityType() == EntityType.ENDER_DRAGON) {
			
			
			Main.manhuntEnded = true;
			server.broadcastMessage(Main.VICTORY_COLOR + "The speedrunner(s) have WON the manhunt!");
				
			defeatMsg(Main.HunterList);
				
			for (Player deadRunner : Main.DeadRunnerList) {
				Main.addRunner(deadRunner);
				deadRunner.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
			}
			
			victoryMsg(Main.RunnerList);
			
			
			Main.HunterList.clear();
			Main.RunnerList.clear();
			Main.DeadRunnerList.clear();
				
				
		}
	}
	
	
	
	
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		
		if (Main.manhuntEnded) {
			return; 
		}
		
		Player respawnedPlayer = e.getPlayer();
		if (!Main.HunterList.contains(respawnedPlayer)) {
			return;
		}
		Main.giveCompass(respawnedPlayer);
		
	}
	
	
	
	
	@EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
		
		if (Main.manhuntEnded) {
			return;
		}
		
		Player p = event.getPlayer();
		UUID pId = p.getUniqueId();
		
		
		if (Main.HunterList.contains(p)) {
			Main.removeHunter(p);
			Main.DisconnectedPlayers.put(pId, "hunter");
			return;
		}
		
		if (Main.RunnerList.contains(p)) {
			Main.removeRunner(p);
			Main.DisconnectedPlayers.put(pId,  "runner");
			return;
		}
		
		if (Main.DeadRunnerList.contains(p)) {
			Main.removeRunner(p);
			Main.DisconnectedPlayers.put(pId,  "spectator");
			return;
		}
//        TrackCommand.putOfflinePlayerLocation(p.getName(), p.getLocation());
		
    }
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (Main.manhuntEnded) {
			return;
		}
		
		Player p = event.getPlayer();
		UUID pId = p.getUniqueId();
		if (!Main.DisconnectedPlayers.containsKey(pId)) {
			return;
		}
		
		switch(Main.DisconnectedPlayers.get(pId)) {
		
			case "hunter":
				Main.addHunter(p);
				break;
			case "runner":
				Main.addRunner(p);
				break;
			case "spectator":
				Main.killRunner(p);
				break;
				
		}
		
		Main.DisconnectedPlayers.remove(pId);
		
	}

}

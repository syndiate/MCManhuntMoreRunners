package org.syndiate.manhuntMultSpeedrunners.listeners;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.syndiate.manhuntMultSpeedrunners.Main;

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
		if (Main.manhuntEnded) return;
			
		Server server = deadEntity.getEntity().getServer();
			
		if (deadEntity.getEntityType() == EntityType.PLAYER) {
				
				
			Player deadPlayer = (Player) deadEntity.getEntity();
			if (!Main.RunnerList.contains(deadPlayer)) return;
			
			
			Main.RunnerList.remove(deadPlayer);
			Main.DeadRunnerList.add(deadPlayer);
			deadPlayer.setGameMode(GameMode.SPECTATOR);
			
			
			
			if (Main.RunnerList.size() > 0) return;
					
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
					
				Main.DeadRunnerList.remove(deadRunner);
				Main.RunnerList.add(deadRunner);
				deadRunner.teleport(new Location(Bukkit.getWorld("world"), 0, 0, 0));
				deadRunner.setGameMode(GameMode.SURVIVAL);
					
			}
			
			victoryMsg(Main.RunnerList);
			
			
			Main.HunterList.clear();
			Main.RunnerList.clear();
			Main.DeadRunnerList.clear();
				
				
		}
	}
	
	
	
	
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		
		if (Main.manhuntEnded) return; 
		
		Player respawnedPlayer = e.getPlayer();
		Main.giveCompass(respawnedPlayer);
		
	}
	
	
	
	
	@EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
		
		if (Main.manhuntEnded) return;
		
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
			Main.DisconnectedPlayers.put(pId,  "spectator");
			Main.RunnerList.remove(p);
			Main.DeadRunnerList.remove(p);
			return;
		}
//        TrackCommand.putOfflinePlayerLocation(p.getName(), p.getLocation());
		
    }
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		if (Main.manhuntEnded) return;
		
		Player p = event.getPlayer();
		UUID pId = p.getUniqueId();
		if (!Main.DisconnectedPlayers.containsKey(pId)) return;
		
		switch(Main.DisconnectedPlayers.get(pId)) {
		
			case "hunter":
				Main.addHunter(p);
				Main.giveCompass(p);
				break;
			case "runner":
				Main.addRunner(p);
				break;
			case "spectator":
				Main.DeadRunnerList.add(p);
//				TODO: make player go in spectator mode
				break;
				
		}
		
		Main.DisconnectedPlayers.remove(pId);
		
	}

}

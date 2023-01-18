package org.syndiate.manhuntMultSpeedrunners.listeners;

import java.util.ArrayList;

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
import org.syndiate.manhuntMultSpeedrunners.Main;

public class DeathListener implements Listener {
	
	
	
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
			boolean isRunner = false;
			
			
			for (Player runner : Main.RunnerList) {
				
				if (!runner.equals(deadPlayer)) continue;
				isRunner = true;
				break;
				
			}
			if (!isRunner) return;
			
			
			Main.RunnerList.remove(deadPlayer);
			Main.DeadRunnerList.add(deadPlayer);
			
				
			if (Main.RunnerList.size() == 0) {
					
				Main.manhuntEnded = true;
				server.broadcastMessage(Main.VICTORY_COLOR + "The hunters have won the manhunt!");
					
				victoryMsg(Main.HunterList);
				defeatMsg(Main.DeadRunnerList);
					
				Main.HunterList.clear();
				Main.RunnerList.clear();
				Main.DeadRunnerList.clear();
					
					
			} else {
				deadPlayer.setGameMode(GameMode.SPECTATOR);
			}
				
				
				
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

}

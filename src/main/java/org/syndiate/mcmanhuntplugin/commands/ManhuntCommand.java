package org.syndiate.mcmanhuntplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.mcmanhuntplugin.Main;

public class ManhuntCommand implements CommandExecutor {
	
	
	
	
	private boolean singleArg(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		String manhuntOperation = args[0];
		Server server = sender.getServer();
		
		
		
		switch (manhuntOperation.toLowerCase()) {

			case "start": {

				if (Main.HunterList.size() == 0 || Main.RunnerList.size() == 0) {
					sender.sendMessage(Main.ERROR_COLOR
							+ "You have not added any hunters and/or runners. The manhunt will not begin.");
					return true;
				}

				Main.HunterTracking.clear();
				Main.DeadRunnerList.clear();
				//Main.RunnerPortals.clear();

				Main.manhuntEnded = false;
				for (Player hunter : Main.HunterList) {
					Main.giveCompass(hunter);
				}

				server.broadcastMessage(ChatColor.GREEN + "The manhunt has started!");
				return true;

			}
			
			
			case "stop": {

				if (Main.manhuntEnded) {
					sender.sendMessage(Main.ERROR_COLOR + "The manhunt has not yet started.");
					return true;
				}
				Main.manhuntEnded = true;

				for (Player hunter : Main.HunterList) {
					hunter.getInventory().remove(Material.COMPASS);
				}

				Main.RunnerList.clear();
				Main.HunterList.clear();
				Main.clearCompassMenu();

				server.broadcastMessage(Main.VICTORY_COLOR + "The manhunt has ended!");
				return true;

			}
			
			
			case "status": {

				if (Main.manhuntEnded) {
					sender.sendMessage("The manhunt is not going on.");
				} else {
					sender.sendMessage("The manhunt is going on.");
				}
				return true;

			}
			
			

			case "compass": {
				for (Player hunter : Main.HunterList) {
					Main.giveCompass(hunter);
				}
				sender.sendMessage(Main.ADDED_COLOR + "All hunters have been given compasses.");
				return true;
			}
			
			

			case "debug": {
				sender.sendMessage("Runners:" + Main.RunnerList.toString());
				sender.sendMessage("Hunters:" + Main.HunterList.toString());
				sender.sendMessage("Dead runners:" + Main.DeadRunnerList.toString());
//				sender.sendMessage("RunnerPortals" + Main.RunnerPortals.toString());
				sender.sendMessage("HunterTracking" + Main.HunterTracking.toString());
				sender.sendMessage("Disconnected Players" + Main.DisconnectedPlayers.toString());
				sender.sendMessage("Portal Entrances" + Main.PortalEntrances.toString());
				sender.sendMessage("Portal Exits" + Main.PortalExits.toString());
				return true;
			}

			case "help": {
				sender.sendMessage("List of command arguments for the manhunt command:");
				sender.sendMessage("/" + Main.MANHUNT_COMMAND + " start - Starts the manhunt.");
				sender.sendMessage("/" + Main.MANHUNT_COMMAND + " stop - Stops the manhunt.");
				sender.sendMessage("/" + Main.MANHUNT_COMMAND + " help - Provides a list of command arguments.");
				sender.sendMessage("List of other commands:");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " help");
				sender.sendMessage("/" + Main.HUNTER_COMMAND + " help");
				sender.sendMessage("/" + Main.TRACK_COMMAND + " help");
				break;
			}
			default:
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.MANHUNT_COMMAND + " help\" for more details.");
				
		}

		return false;

		
		
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1) {
			return singleArg(sender, cmd, label, args);
		}
		
		String manhuntOperation = args[0].toLowerCase();
		String target = args[1];
		Server server = sender.getServer();
		
		
		switch(manhuntOperation) {
		
			case "compass": {
				Player p = server.getPlayer(target);
				if (p == null) {
					sender.sendMessage(Main.PLAYER_DOES_NOT_EXIST);
					return true;
				}

				Main.giveCompass(p);
				sender.sendMessage(Main.ADDED_COLOR + target + " has been given a compass.");
				return true;
			}
			
		}
		
		return false;
		
	}
		
		
	
	

}

package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class HunterCommand implements CommandExecutor {
	
	
	
	private boolean singleArg(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		switch(args[0].toLowerCase()) {
		
			case "help": {
				sender.sendMessage("List of command arguments:");
				sender.sendMessage("/" + Main.HUNTER_COMMAND + " add <playerName> - This adds a player as a hunter.");
				sender.sendMessage("/" + Main.HUNTER_COMMAND + " remove <playerName> - This removes a player as a hunter.");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " clearinv <playerName>|\"all\" - This clears a hunter's inventory. Instead of specifying a single hunter's username, you can also type \"all\" to clear the inventories of all hunters.");
				sender.sendMessage("/" + Main.HUNTER_COMMAND + " list - This lists all of the hunters.");
				sender.sendMessage("/" + Main.HUNTER_COMMAND + " help - Provides a list of command arguments.");
				return false;
			}
			case "list": {
				sender.sendMessage("List of hunters:");
				for (Player hunter : Main.HunterList) {
					sender.sendMessage(hunter.getName());
				}
				return true;
			}
			case "clearinv": {
			
				for (Player hunter : Main.HunterList) {
					hunter.getInventory().clear();
				}
				sender.sendMessage(Main.REMOVED_COLOR + "All hunters' inventories have been cleared.");
				return true;
			
			}
			case "add": {
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					if (Main.RunnerList.contains(p) || Main.HunterList.contains(p)) {
						continue;
					}
					Main.addHunter(p);
					
				}
				sender.getServer().broadcastMessage(Main.ADDED_COLOR + "All players that aren't already hunters/runners have been added as hunters.");
				return true;
				
			}
			case "remove": {
				Main.RunnerList.clear();
				Main.runnerMenu.clear();
				sender.getServer().broadcastMessage(Main.REMOVED_COLOR + "The list of hunters has been cleared.");
				return true;
			}
			default: {
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.RUNNER_COMMAND + " help\" for more details.");
				break;
			}
			
		}
		return false;
		
		
	}
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Server server = sender.getServer();
		String listOperation = args[0].toLowerCase();
		
		if (args.length == 1) {
			return singleArg(sender, cmd, label, args);
		}
		
		
		
		Player givenHunter = server.getPlayer(args[1]);
		
		if (givenHunter == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Could not find the player specified.");
			return true;
		}
		
		
		
		switch(listOperation) {
		
			case "add": {
				Main.addHunter(givenHunter);
				server.broadcastMessage(Main.ADDED_COLOR + givenHunter.getName() + " was added as a hunter!");
				break;
			}
			case "remove": {
				Main.removeHunter(givenHunter);
				server.broadcastMessage(Main.REMOVED_COLOR + givenHunter.getName() + " was removed as a hunter.");
				break;
			}
			case "clearinv": {
				givenHunter.getInventory().clear();
				break;
			}
			default: {
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.HUNTER_COMMAND + " help\" for more details.");
				break;
			}
			
		}
		
		
		return true;
		
	}

}

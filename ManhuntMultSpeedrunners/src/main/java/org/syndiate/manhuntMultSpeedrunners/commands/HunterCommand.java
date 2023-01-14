package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class HunterCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Server server = sender.getServer();
		String listOperation = args[0].toLowerCase();
		
		
		switch(listOperation) {
		
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
				for (Player hunter : Main.HunterList) sender.sendMessage(hunter.getName());
				return false;
			}
			case "clearinv": {
				
				if (args[1].equals("all")) {
					for (Player hunter : Main.HunterList) hunter.getInventory().clear();
				}
				break;
				
			}
			case "add":
			case "remove": { break; }
			default: {
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.RUNNER_COMMAND + " help\" for more details.");
				return false;
			}
				
		}
		
		
		Player givenHunter = server.getPlayer(args[1]);
		
		if (givenHunter == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Could not find the player specified.");
			return false;
		}
		
		
		switch(listOperation) {
		
			case "add": {
				Main.RunnerList.remove(givenHunter);
				Main.HunterList.add(givenHunter);
				
				givenHunter.setHealth(givenHunter.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				givenHunter.setFoodLevel(20);
				givenHunter.setSaturation(5.0f);
				
				server.broadcastMessage(Main.ADDED_COLOR + givenHunter.getName() + " was added as a hunter!");
				break;
			}
			case "remove": {
				Main.HunterList.remove(givenHunter);
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
		
		
		return false;
		
	}

}

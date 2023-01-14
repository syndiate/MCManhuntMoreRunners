package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class RunnerCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Server server = sender.getServer();
		String listOperation = args[0].toLowerCase();
		
		
		// logic for when there is only one argument
		switch(listOperation) {
		
			case "help": {
				sender.sendMessage("List of command arguments:");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " add <playerName> - This adds a player as a runner.");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " remove <playerName> - This removes a player as a runner.");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " clearinv <playerName>|\"all\" - This clears a runner's inventory. Instead of specifying a single runner's username, you can also type \"all\" to clear the inventories of all runners.");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " list - This lists all of the runners.");
				sender.sendMessage("/" + Main.RUNNER_COMMAND + " help - Provides a list of command arguments.");
				return false;
			}
			case "list": {
				sender.sendMessage("List of runners:");
				for (Player runner : Main.RunnerList) sender.sendMessage(runner.getName());
				return false;
			}
			case "clearinv": {
				
				if (args[1].equals("all")) {
					for (Player runner : Main.RunnerList) runner.getInventory().clear();
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
		
		
		
		
		Player givenRunner = server.getPlayer(args[1]);
		
		if (givenRunner == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Could not find the player specified.");
			return false;
		}
		
		
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
		skullMeta.setOwningPlayer(givenRunner);
		playerHead.setItemMeta(skullMeta);
		
		
		
		switch(listOperation) {
		
			case "add": {
				Main.HunterList.remove(givenRunner);
				Main.RunnerList.add(givenRunner);
				Main.runnerMenu.addItem(playerHead);
				
				givenRunner.setHealth(givenRunner.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
				givenRunner.setFoodLevel(20);
				givenRunner.setSaturation(5.0f);
				
				server.broadcastMessage(Main.ADDED_COLOR + givenRunner.getName() + " was added as a runner!");
				break;
			}
			case "remove": {
				Main.RunnerList.remove(givenRunner);
				Main.runnerMenu.removeItem(playerHead);
				server.broadcastMessage(Main.REMOVED_COLOR + givenRunner.getName() + " was removed as a runner.");
				break;
			}
			case "clearinv": {
				givenRunner.getInventory().clear();
				break;
			}
			default: {
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.RUNNER_COMMAND + " help\" for more details.");
				break;
			}
			
		}
		
		
		return false;
		
	}

}

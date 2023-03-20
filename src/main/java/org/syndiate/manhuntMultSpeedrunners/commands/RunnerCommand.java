package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class RunnerCommand implements CommandExecutor {
	
	
	
	
	private boolean singleArg(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		switch(args[0].toLowerCase()) {
				
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
				return true;
			}
			case "clearinv": {
				for (Player runner : Main.RunnerList) runner.getInventory().clear();
				sender.sendMessage(Main.REMOVED_COLOR + "All runners' inventories have been cleared.");
				return true;
			}
			case "add": {
				
				for (Player p : Bukkit.getOnlinePlayers()) {
					
					if (Main.RunnerList.contains(p)) continue;
					if (Main.HunterList.contains(p)) continue;
					Main.addRunner(p);
					
				}
				sender.getServer().broadcastMessage(Main.ADDED_COLOR + "All players that aren't already hunters/runners have been added as runners.");
				return true;
				
			}
			case "remove": {
				Main.RunnerList.clear();
				Main.runnerMenu.clear();
				sender.getServer().broadcastMessage(Main.REMOVED_COLOR + "The list of runners has been cleared.");
				return true;
			}
			default:
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/" + Main.RUNNER_COMMAND + " help\" for more details.");
				break;
					
		}
		
		return false;
		
	}
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Server server = sender.getServer();
		String listOperation = args[0].toLowerCase();
		
		if (args.length == 1) return singleArg(sender, cmd, label, args);
		
		
		
		
		
		Player givenRunner = server.getPlayer(args[1]);
		
		if (givenRunner == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Could not find the player specified.");
			return true;
		}
		
		
		
		
		
		
		switch(listOperation) {
		
			case "add": {
				Main.addRunner(givenRunner);
				server.broadcastMessage(Main.ADDED_COLOR + givenRunner.getName() + " was added as a runner!");
				break;
			}
			case "remove": {
				Main.removeRunner(givenRunner);
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
		
		
		return true;
		
	}

}

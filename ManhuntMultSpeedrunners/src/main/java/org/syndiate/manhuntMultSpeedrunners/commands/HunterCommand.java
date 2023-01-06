package org.syndiate.manhuntMultSpeedrunners.commands;

import java.util.ArrayList;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class HunterCommand implements CommandExecutor {
	
	private static ArrayList<Player> HunterList = new ArrayList<>();
	
	public HunterCommand() {
		Main.HunterList = HunterList;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Server server = sender.getServer();
		String listOperation = args[0];
		Player givenRunner = server.getPlayer(args[1]);
		
		if (givenRunner == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Could not find the player specified.");
			return false;
		}
		
		switch(listOperation.toLowerCase()) {
		
			case "add": {
				HunterList.add(givenRunner);
				server.broadcastMessage(Main.ADDED_COLOR + givenRunner.getName() + " was added as a hunter!");
				break;
			}
			case "remove": {
				HunterList.remove(givenRunner);
				server.broadcastMessage(Main.REMOVED_COLOR + givenRunner.getName() + "was removed as a hunter.");
				break;
			}
			case "help": {
				sender.sendMessage("List of command arguments:");
				sender.sendMessage("/hunter add <playerName> - This adds a player as a hunter.");
				sender.sendMessage("/hunter remove <playerName> - This removes a player as a hunter.");
				sender.sendMessage("/hunter help - Provides a list of command arguments.");
				break;
			}
			default:
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/hunter help\" for more details.");
		}
		
		return false;
	}

}

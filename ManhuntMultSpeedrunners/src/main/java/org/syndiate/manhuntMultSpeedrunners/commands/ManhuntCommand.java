package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class ManhuntCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		String manhuntOperation = args[0];
		Server server = sender.getServer();
		
		switch(manhuntOperation.toLowerCase()) {
		
			case "start": {
				
				if (Main.HunterList.size() == 0 || Main.RunnerList.size() == 0) {
					sender.sendMessage(ChatColor.RED + "You have not specified any hunters and/or runners. The manhunt will not begin.");
					return false;
				}
				
				
				for (Player hunter : Main.HunterList) Main.giveCompass(hunter);

				Main.manhuntEnded = false;
				server.broadcastMessage(ChatColor.GREEN + "The manhunt has started!");
				break;
				
			}
			case "stop": {
				
				for (Player hunter : Main.HunterList) hunter.getInventory().remove(Material.COMPASS);
				Main.manhuntEnded = true;
				Main.RunnerList.clear();
				Main.HunterList.clear();
				server.broadcastMessage(ChatColor.RED + "The anhunt has ended!");
				break;
				
			}
			case "help": {
				sender.sendMessage("List of command arguments for the manhunt command:");
				sender.sendMessage("/manhunt start - Starts the manhunt.");
				sender.sendMessage("/manhunt stop - Stops the manhunt.");
				sender.sendMessage("/manhunt help - Provides a list of command arguments.");
				sender.sendMessage("List of other commands:");
				sender.sendMessage("/runner help");
				sender.sendMessage("/hunter help");
				break;
			}
			default:
				sender.sendMessage(ChatColor.RED + "Invalid input. Type \"/manhunt help\" for more details.");
				
		}
		
		return false;
	}
	
	

}

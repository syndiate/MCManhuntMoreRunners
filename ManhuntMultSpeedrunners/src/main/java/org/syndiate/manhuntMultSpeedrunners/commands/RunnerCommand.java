package org.syndiate.manhuntMultSpeedrunners.commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class RunnerCommand implements CommandExecutor {
	
	private static ArrayList<Player> RunnerList = new ArrayList<>();
	
	public RunnerCommand() {
		Main.RunnerList = RunnerList;
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
		
		ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
		skullMeta.setOwningPlayer(givenRunner);
		playerHead.setItemMeta(skullMeta);
		
		switch(listOperation.toLowerCase()) {
		
			case "add": {
				RunnerList.add(givenRunner);
				Main.runnerMenu.addItem(playerHead);
				
				server.broadcastMessage(Main.ADDED_COLOR + givenRunner.getName() + " was added as a runner!");
				break;
			}
			case "remove": {
				RunnerList.remove(givenRunner);
				Main.runnerMenu.removeItem(playerHead);
				server.broadcastMessage(Main.REMOVED_COLOR + givenRunner.getName() + " was removed as a runner.");
				break;
			}
			case "help": {
				sender.sendMessage("List of command arguments:");
				sender.sendMessage("/runner add <playerName> - This adds a player as a runner.");
				sender.sendMessage("/runner remove <playerName> - This removes a player as a runner.");
				sender.sendMessage("/runner help - Provides a list of command arguments.");
				break;
			}
			default:
				sender.sendMessage(Main.ERROR_COLOR + "Invalid input. Type \"/runner help\" for more details.");
		}
		
		return false;
	}

}

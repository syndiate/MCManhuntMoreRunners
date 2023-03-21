package org.syndiate.mcmanhuntplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.mcmanhuntplugin.Main;

public class TrackCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (args.length <= 0) {
			return false;
		}
		
		if (args[0].equals("help")) {
			sender.sendMessage("Usage: /" + Main.TRACK_COMMAND + " <playerName> - Tracks a player via the compass.");
			return true;
		}
		
		
		Player p = (Player) sender;
		Player runner = p.getServer().getPlayer(args[0]);
		
		
		Main.trackPlayer(p, runner);
		return true;
		
		
	}
	
	

}

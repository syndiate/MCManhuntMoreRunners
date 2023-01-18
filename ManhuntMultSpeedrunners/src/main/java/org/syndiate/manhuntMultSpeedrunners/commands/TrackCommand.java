package org.syndiate.manhuntMultSpeedrunners.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class TrackCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		if (args.length <= 0) return false;
		
		if (args[0].equals("help")) {
			sender.sendMessage("Usage: /" + Main.TRACK_COMMAND + " <playerName> - Tracks a player via the compass.");
			return true;
		}
		
		
		Player p = (Player) sender;
		Player runner = p.getServer().getPlayer(args[0]);
		
		
		if (runner == null) {
			sender.sendMessage(Main.ERROR_COLOR + "Player does not exist.");
			return true;
		}
		for (Player hunter : Main.HunterList) {
			if (!hunter.equals(p)) continue;
			Main.trackPlayer(hunter, runner);
			return true;
		}

		
		sender.sendMessage(Main.ERROR_COLOR + "You are not a hunter.");
		return true;
		
		
	}
	
	

}

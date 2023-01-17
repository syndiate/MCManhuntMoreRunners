package org.syndiate.manhuntMultSpeedrunners.tabCompleters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.syndiate.manhuntMultSpeedrunners.Main;

public class TrackCommandCompleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length != 1) return null;
		
		List<String> runners = new ArrayList<>();
		for (Player runner : Main.RunnerList) runners.add(runner.getDisplayName());
		return runners;
		
	}

}
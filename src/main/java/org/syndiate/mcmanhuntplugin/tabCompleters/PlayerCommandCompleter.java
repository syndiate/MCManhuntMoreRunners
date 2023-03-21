package org.syndiate.mcmanhuntplugin.tabCompleters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PlayerCommandCompleter implements TabCompleter {
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
		if (args.length == 1) return new ArrayList<>(Arrays.asList("add","remove","clearinv","list","help"));
		return null;
	}

}
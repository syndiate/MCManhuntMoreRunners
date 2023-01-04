package org.syndiate.manhuntMultSpeedrunners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.syndiate.manhuntMultSpeedrunners.commands.*;
import org.syndiate.manhuntMultSpeedrunners.listeners.*;
import org.syndiate.manhuntMultSpeedrunners.tabCompleters.*;

public class Main extends JavaPlugin {
	
	public static ArrayList<Player> RunnerList = new ArrayList<>();
	public static ArrayList<Player> DeadRunnerList = new ArrayList<>();
	public static ArrayList<Player> HunterList = new ArrayList<>();
	
	public static Map<Player, Player> hunterTracking = new HashMap<Player, Player>();
	
	public static Inventory runnerMenu = Bukkit.createInventory(null, InventoryType.CHEST, "Runners");
	
	public static boolean manhuntEnded;
	
	private final PluginCommand RUNNER_COMMAND = getCommand("runner");
	private final PluginCommand HUNTER_COMMAND = getCommand("hunter");
	private final PluginCommand MANHUNT_COMMAND = getCommand("manhunt");

	@Override
	public void onEnable() {
		
		PluginManager manager = getServer().getPluginManager();
		
		RUNNER_COMMAND.setExecutor(new RunnerCommand());
		RUNNER_COMMAND.setTabCompleter(new PlayerCommandCompleter());
		
		HUNTER_COMMAND.setExecutor(new HunterCommand());
		HUNTER_COMMAND.setTabCompleter(new PlayerCommandCompleter());
		
		MANHUNT_COMMAND.setExecutor(new ManhuntCommand());
		MANHUNT_COMMAND.setTabCompleter(new ManhuntCommandCompleter());
		
		manager.registerEvents(new DeathListener(), this);
		manager.registerEvents(new RespawnListener(), this);
		manager.registerEvents(new CompassListener(), this);
		
		getLogger().info("The Manhunt with Multiple Speedrunners plugin has been initialized! Type /manhunt help for more info.");

	}

	@Override
	public void onDisable() {
		getLogger().info("I'm melting!");
	}
	
	public static void giveCompass(Player p) {
		if (!Main.manhuntEnded) {
			ItemStack HunterCompass = new ItemStack(Material.COMPASS);
			HunterCompass.addEnchantment(Enchantment.VANISHING_CURSE, 1);

			p.getInventory().addItem(HunterCompass);
		}
	}

}

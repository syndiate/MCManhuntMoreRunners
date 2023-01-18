package org.syndiate.manhuntMultSpeedrunners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
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
	
	
	public static final ChatColor ERROR_COLOR = ChatColor.DARK_RED;
	public static final ChatColor VICTORY_COLOR = ChatColor.GOLD;
	public static final ChatColor DEFEAT_COLOR = ChatColor.RED;
	public static final ChatColor TITLE_COLOR = ChatColor.GRAY;
	public static final ChatColor ADDED_COLOR = ChatColor.GREEN;
	public static final ChatColor REMOVED_COLOR = ChatColor.DARK_GREEN;
	
	
	public static final String RUNNER_COMMAND = "runner";
	public static final String HUNTER_COMMAND = "hunter";
	public static final String MANHUNT_COMMAND = "manhunt";
	public static final String TRACK_COMMAND = "track";
	
	
	
	
	
	public static ArrayList<Player> RunnerList = new ArrayList<>();
	public static Map<Player, Location> RunnerPortals = new HashMap<Player, Location>();
	public static ArrayList<Player> DeadRunnerList = new ArrayList<>();
	public static ArrayList<Player> HunterList = new ArrayList<>();
	public static Map<Player, Player> HunterTracking = new HashMap<Player, Player>();
	
	
	public static Inventory runnerMenu = Bukkit.createInventory(null, InventoryType.CHEST, Main.TITLE_COLOR + "Speedrunners");
	
	
	public static boolean menuOpen = false;
	public static boolean manhuntEnded = true;
	
	
	

	@Override
	public void onEnable() {
		
		PluginManager manager = getServer().getPluginManager();
		
		this.getCommand(RUNNER_COMMAND).setExecutor(new RunnerCommand());
		this.getCommand(RUNNER_COMMAND).setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand(HUNTER_COMMAND).setExecutor(new HunterCommand());
		this.getCommand(HUNTER_COMMAND).setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand(MANHUNT_COMMAND).setExecutor(new ManhuntCommand());
		this.getCommand(MANHUNT_COMMAND).setTabCompleter(new ManhuntCommandCompleter());
		
		this.getCommand(TRACK_COMMAND).setExecutor(new TrackCommand());
		this.getCommand(TRACK_COMMAND).setTabCompleter(new TrackCommandCompleter());
		
		manager.registerEvents(new DeathListener(), this);
		manager.registerEvents(new RespawnListener(), this);
		manager.registerEvents(new CompassListener(), this);
		manager.registerEvents(new MenuListener(), this);
		manager.registerEvents(new PortalListener(), this);
		
		getLogger().info("The Manhunt with Multiple Speedrunners plugin has been initialized! Type /" + Main.MANHUNT_COMMAND + " help for more info.");

	}
	

	@Override
	public void onDisable() {
		getLogger().info("I'm melting!");
	}
	
	
	/**
	 * 
	 * universal functions
	 * 
	 */
	
	public static void giveCompass(Player p) {
		
		if (Main.manhuntEnded) return;
		
		ItemStack HunterCompass = new ItemStack(Material.COMPASS, 1);
		p.getInventory().addItem(HunterCompass);
		
	}
	
	
	public static void trackPlayer(Player hunter, Player runner) {
		Environment hunterEnv = hunter.getWorld().getEnvironment();
		Environment runnerEnv = runner.getWorld().getEnvironment();
		
		if (hunterEnv == runnerEnv) {
			RunnerPortals.remove(runner);
			hunter.setCompassTarget(runner.getLocation());
		} else {
			hunter.setCompassTarget(RunnerPortals.get(runner));
		}

		hunter.sendMessage(ChatColor.GREEN + "Tracking " + runner.getName());
		HunterTracking.put(hunter, runner);
	}

}

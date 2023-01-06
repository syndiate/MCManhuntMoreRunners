package org.syndiate.manhuntMultSpeedrunners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPortalEvent;
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
	public static Map<Player, Player> HunterTracking = new HashMap<Player, Player>();
	
	public static Inventory runnerMenu = Bukkit.createInventory(null, InventoryType.CHEST, Main.TITLE_COLOR + "Speedrunners");
	
	
	public static boolean menuOpen = false;
	public static boolean manhuntEnded;
	
	
	public static final ChatColor ERROR_COLOR = ChatColor.DARK_RED;
	public static final ChatColor VICTORY_COLOR = ChatColor.GOLD;
	public static final ChatColor DEFEAT_COLOR = ChatColor.RED;
	public static final ChatColor TITLE_COLOR = ChatColor.GRAY;
	public static final ChatColor ADDED_COLOR = ChatColor.GREEN;
	public static final ChatColor REMOVED_COLOR = ChatColor.DARK_GREEN;
	
	

	@Override
	public void onEnable() {
		
		PluginManager manager = getServer().getPluginManager();
		
		this.getCommand("runner").setExecutor(new RunnerCommand());
		this.getCommand("runner").setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand("hunter").setExecutor(new HunterCommand());
		this.getCommand("hunter").setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand("manhunt").setExecutor(new ManhuntCommand());
		this.getCommand("manhunt").setTabCompleter(new ManhuntCommandCompleter());
		
		this.getCommand("track").setExecutor(new TrackCommand());
		this.getCommand("track").setTabCompleter(new TrackCommandCompleter());
		
		manager.registerEvents(new DeathListener(), this);
		manager.registerEvents(new RespawnListener(), this);
		manager.registerEvents(new CompassListener(), this);
		manager.registerEvents(new MenuListener(), this);
		
		getLogger().info("The Manhunt with Multiple Speedrunners plugin has been initialized! Type /manhunt help for more info.");

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
		
		ItemStack HunterCompass = new ItemStack(Material.COMPASS);
		HunterCompass.addEnchantment(Enchantment.VANISHING_CURSE, 1);

		p.getInventory().addItem(HunterCompass);
		
	}
	
	public static void trackPlayer(Player hunter, Player runner, EntityPortalEvent portalEvent) {
		Environment hunterEnv = hunter.getWorld().getEnvironment();
		Environment runnerEnv = runner.getWorld().getEnvironment();
		
		if (hunterEnv == runnerEnv) hunter.setCompassTarget(runner.getLocation());
		
		if (portalEvent == null) {
			hunter.sendMessage("Left-click to track " + runner.getName());
		} else if (portalEvent.getEntity().equals(runner)) {
			// this is for when the runner(s) enters a new dimension
			hunter.setCompassTarget(portalEvent.getFrom());
			hunter.sendMessage(ChatColor.GREEN + "Tracking " + runner.getName());
		}
		
		HunterTracking.put(hunter, runner);
	}

}

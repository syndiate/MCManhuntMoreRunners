package org.syndiate.mcmanhuntplugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.syndiate.mcmanhuntplugin.commands.HunterCommand;
import org.syndiate.mcmanhuntplugin.commands.ManhuntCommand;
import org.syndiate.mcmanhuntplugin.commands.RunnerCommand;
import org.syndiate.mcmanhuntplugin.commands.TrackCommand;
import org.syndiate.mcmanhuntplugin.listeners.CompassListener;
import org.syndiate.mcmanhuntplugin.listeners.MenuListener;
import org.syndiate.mcmanhuntplugin.listeners.PlayerEventListener;
import org.syndiate.mcmanhuntplugin.listeners.PortalListener;
import org.syndiate.mcmanhuntplugin.tabCompleters.ManhuntCommandCompleter;
import org.syndiate.mcmanhuntplugin.tabCompleters.PlayerCommandCompleter;
import org.syndiate.mcmanhuntplugin.tabCompleters.TrackCommandCompleter;



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
	
	
	public static final String PLAYER_DOES_NOT_EXIST = Main.ERROR_COLOR + "Could not find the player specified.";
	
	
	
	
	
	public static ArrayList<Player> RunnerList = new ArrayList<>();
	public static ArrayList<Player> DeadRunnerList = new ArrayList<>();
	public static ArrayList<Player> HunterList = new ArrayList<>();
	
	
	public static Map<Player, Location> PortalEntrances = new HashMap<Player, Location>();
	public static Map<Player, Location> PortalExits = new HashMap<Player, Location>();

	public static Map<Player, Player> HunterTracking = new HashMap<Player, Player>();
	public static Map<UUID, String> DisconnectedPlayers = new HashMap<UUID, String>();
	
	
	private static final Inventory runnerMenu = Bukkit.createInventory(null, InventoryType.CHEST, Main.TITLE_COLOR + "Speedrunners");
	
	public static final boolean supportsModernHeads = Main.versionCompat("1.13");
	public static boolean manhuntEnded = true;
	
	
	private static FileConfiguration config;
	private static final int SAVE_INTERVAL = 200;
	
	
	
	
	

	
	
	@Override
	public void onEnable() {
		
		saveDefaultConfig();
		config = getConfig();
		loadManhuntData();
		
		PluginManager manager = getServer().getPluginManager();
		
		this.getCommand(RUNNER_COMMAND).setExecutor(new RunnerCommand());
		this.getCommand(RUNNER_COMMAND).setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand(HUNTER_COMMAND).setExecutor(new HunterCommand());
		this.getCommand(HUNTER_COMMAND).setTabCompleter(new PlayerCommandCompleter());
		
		this.getCommand(MANHUNT_COMMAND).setExecutor(new ManhuntCommand());
		this.getCommand(MANHUNT_COMMAND).setTabCompleter(new ManhuntCommandCompleter());
		
		this.getCommand(TRACK_COMMAND).setExecutor(new TrackCommand());
		this.getCommand(TRACK_COMMAND).setTabCompleter(new TrackCommandCompleter());
		
		manager.registerEvents(new PlayerEventListener(), this);
		manager.registerEvents(new CompassListener(), this);
		manager.registerEvents(new MenuListener(), this);
		manager.registerEvents(new PortalListener(), this);
		
		getLogger().info("The Manhunt with Multiple Speedrunners plugin has been initialized! Type /" + Main.MANHUNT_COMMAND + " help for more info.");
		
		
		new BukkitRunnable() {
			@Override
			public void run() {
				saveManhuntData();
			}
		}.runTaskTimer(this, SAVE_INTERVAL, SAVE_INTERVAL);

	}
	

	@Override
	public void onDisable() {
		saveManhuntData();
		getLogger().info("I'm melting!");
	}
	
	
	
	@SuppressWarnings("unchecked")
	private void loadManhuntData() {
		Main.RunnerList = (ArrayList<Player>) config.get("RunnerList");
		Main.DeadRunnerList = (ArrayList<Player>) config.get("DeadRunnerList");
		Main.HunterList = (ArrayList<Player>) config.get("HunterList");
		Main.PortalEntrances = (Map<Player, Location>) config.get("PortalEntrances");
		Main.PortalExits = (Map<Player, Location>) config.get("PortalExits");
		Main.HunterTracking = (Map<Player, Player>) config.get("HunterTracking");
		Main.DisconnectedPlayers = (Map<UUID, String>) config.get("DisconnectedPlayers");
		Main.manhuntEnded = config.getBoolean("manhuntEnded");
		
		for (Player runner : Main.RunnerList) {
			Main.addCompassItem(runner);
		}
	}
	
	
	private void saveManhuntData() {
		config.set("RunnerList", RunnerList);
		config.set("DeadRunnerList", DeadRunnerList);
		config.set("HunterList", HunterList);
		config.set("PortalEntrances", PortalEntrances);
		config.set("PortalExits", PortalExits);
		config.set("HunterTracking", HunterTracking);
		config.set("DisconnectedPlayers", DisconnectedPlayers);
		config.set("manhuntEnded", manhuntEnded);
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * universal functions
	 * 
	 */

	
	public static void addHunter(Player givenHunter) {

		if (Main.HunterList.contains(givenHunter)) {
			return;
		}
		
		Main.removeRunner(givenHunter);
		Main.HunterList.remove(givenHunter);
		Main.HunterList.add(givenHunter);
		Main.setMaxHealth(givenHunter);
		Main.giveCompass(givenHunter);
		
	}

	public static void removeHunter(Player givenHunter) {
		Main.HunterList.remove(givenHunter);
		Main.DisconnectedPlayers.remove(givenHunter.getUniqueId());
	}
	
	public static void addRunner(Player givenRunner) {
		
		if (Main.RunnerList.contains(givenRunner)) {
			return;
		}
		
		Main.DeadRunnerList.remove(givenRunner);
		Main.RunnerList.add(givenRunner);
		Main.addCompassItem(givenRunner);
		Main.setMaxHealth(givenRunner);
		givenRunner.setGameMode(GameMode.SURVIVAL);
		
	}
	
	public static void removeRunner(Player givenRunner) {
		Main.RunnerList.remove(givenRunner);
		Main.DeadRunnerList.remove(givenRunner);
		Main.DisconnectedPlayers.remove(givenRunner.getUniqueId());
		Main.removeCompassItem(givenRunner);
		
		for (Map.Entry<Player, Player> entry : Main.HunterTracking.entrySet()) {
			
			if (!entry.getValue().getUniqueId().equals(givenRunner.getUniqueId())) {
				continue;
			}
			Main.HunterTracking.remove(entry.getKey());
			
		}
		
	}
	
	public static void killRunner(Player givenRunner) {
		Main.removeRunner(givenRunner);
		Main.DeadRunnerList.add(givenRunner);
		givenRunner.setGameMode(GameMode.SPECTATOR);
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	private static void setMaxHealth(Player p) {
		
		
		if (Main.versionCompat("1.13")) {
			p.setHealth(p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
		} else {
			p.setHealth(p.getMaxHealth());
		}
		p.setFoodLevel(20);
		p.setSaturation(5.0f);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void giveCompass(Player p) {
		
		if (Main.manhuntEnded || !Main.HunterList.contains(p)/* || p.getInventory().contains(Material.COMPASS)*/) {
			return;
		}
		
		ItemStack hunterCompass = new ItemStack(Material.COMPASS, 1);
		hunterCompass.addEnchantment(Enchantment.VANISHING_CURSE, 1);
		
		p.getInventory().removeItem(hunterCompass);
		p.getInventory().addItem(hunterCompass);
		
	}

	
	
	
	
	
	
	
	
	
	public static void trackPlayer(Player hunter, Player runner) {
		
		
		if (hunter == null) {
			return;
		}
		if (runner == null) {
			hunter.sendMessage(Main.PLAYER_DOES_NOT_EXIST);
			return;
		}
		
		
		if (!Main.HunterList.contains(hunter)) {
			hunter.sendMessage(Main.ERROR_COLOR + "You may not track a player because you are not a hunter.");
			return;
		}
		if (!Main.RunnerList.contains(runner)) {
			hunter.sendMessage(Main.ERROR_COLOR + "You may not track this player because they are not a runner.");
			return;
		}
		
		
		
		PlayerInventory hunterInv = hunter.getInventory();
		int compassPosition = hunterInv.first(Material.COMPASS);
		
		
		
		if (compassPosition < 0) {
			hunter.sendMessage(Main.ERROR_COLOR + "You don't have a compass in your inventory.");
			return;
		}
		if (Main.HunterList.contains(runner)) {
			hunter.sendMessage(Main.ERROR_COLOR + "This player is a hunter.");
			return;
		}
		if (Main.DeadRunnerList.contains(runner)) {
			hunter.sendMessage(Main.ERROR_COLOR + "This runner has died.");
			return;
		}
		
		
		
		
		Environment hunterEnv = hunter.getWorld().getEnvironment();
		Environment runnerEnv = runner.getWorld().getEnvironment();
		Location runnerLoc = runner.getLocation();

		if (hunterEnv != runnerEnv) {
			if (hunterEnv == Environment.NORMAL) {
				runnerLoc = Main.PortalEntrances.get(runner);
			} else {
				runnerLoc = Main.PortalExits.get(hunter);
			}
		}
		
		
		if (Main.versionCompat("1.16")) {
			
			ItemStack compass = hunterInv.getItem(compassPosition);
			
			CompassMeta compassMeta = (CompassMeta) compass.getItemMeta();
			compassMeta.setLodestone(runnerLoc);
			compassMeta.setLodestoneTracked(false);
			
			hunterInv.removeItem(compass);
			compass.setItemMeta(compassMeta);
			hunterInv.addItem(compass);
			
		} else {
			hunter.setCompassTarget(runnerLoc);
		}
		
		
		
		hunter.sendMessage(ChatColor.GREEN + "Tracking " + runner.getName());
		HunterTracking.put(hunter, runner);
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("deprecation")
	public static void addCompassItem(Player p) {
		
		ItemStack playerHead = null;
		
		if (!Main.RunnerList.contains(p)) {
			return;
		}
		
		if (Main.versionCompat("1.13")) {
			
			playerHead = new ItemStack(Material.getMaterial("PLAYER_HEAD"));
			SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
			skullMeta.setOwningPlayer(p);
			skullMeta.setDisplayName(p.getDisplayName());
			playerHead.setItemMeta(skullMeta);
			
		} else {

			playerHead = new ItemStack(Material.getMaterial("SKULL_ITEM"), 1, (byte) 3);
			SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();
			skullMeta.setOwner(p.getDisplayName());
			skullMeta.setDisplayName(ChatColor.YELLOW + p.getDisplayName());
			playerHead.setItemMeta(skullMeta);
			
		}
		

		
		Main.removeCompassItem(p);
		Main.runnerMenu.addItem(playerHead);
		
	}
	
	
	
	
	@SuppressWarnings("deprecation")
	public static void removeCompassItem(Player p) {
		
	
		for (ItemStack runnerHead : runnerMenu.getContents()) {
			if (runnerHead == null) {
				continue;
			}
			
			Material headType = Main.supportsModernHeads ? Material.getMaterial("PLAYER_HEAD") : Material.getMaterial("SKULL_ITEM");

			if (runnerHead.getType() != headType) {
				continue;
			}
			
			
			SkullMeta skullMeta = (SkullMeta) runnerHead.getItemMeta();
			UUID pId = p.getUniqueId();
			
			
			if (Main.supportsModernHeads) {
			
				if (skullMeta.getOwningPlayer() == null) {
					continue;
				}
				if (!skullMeta.getOwningPlayer().getUniqueId().equals(pId)) {
					continue;
				}
					
			} else {
					
				if (skullMeta.getOwner() == null) {
					continue;
				}
				if (Bukkit.getPlayer(skullMeta.getOwner()).getUniqueId().equals(pId)) {
					continue;
				}
					
			}
			
			
			runnerMenu.removeItem(runnerHead);
			break;
		}
		
		// fix inventory so that it's aligned to the left without any empty slots	
		Arrays.stream(Main.runnerMenu.getContents())
		.filter(item -> item != null)
		.forEach(item -> {
			
			int itemIndex = Main.runnerMenu.first(item);
			
			if (itemIndex == 0 || Main.runnerMenu.getItem(itemIndex - 1) != null) {
				return;
			}

			Main.runnerMenu.setItem(itemIndex - 1, item);
			Main.runnerMenu.setItem(itemIndex, null);
			
		});
		
	}
	
	public static void clearCompassMenu() {
		Main.runnerMenu.clear();
	}
	
	public static void openCompassMenu(Player hunter) {
		hunter.openInventory(Main.runnerMenu);
	}
	
	
	
	
	
	
	
	
	public static boolean versionCompat(String version) {
	    // Split the version strings into parts
	    String[] parts1 = Bukkit.getBukkitVersion().split("-")[0].split("\\.");
	    String[] parts2 = version.split("\\.");
	    
	    // Compare the version parts numerically
	    for (int i = 0; i < Math.max(parts1.length, parts2.length); i++) {
	        int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
	        int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;
	        
	        if (part1 < part2) return false;
	        if (part1 > part2) return true;
	    }
	    
	    // The version numbers are equal
	    return true;
	}
	
	
	
	

	
	
	public static void putPortalEntrance(Player p, Location location) {
        PortalEntrances.put(p, location);
    }

    public static void clearPortalEntrance(Player p) {
        PortalEntrances.remove(p);
    }

    public static void putPortalExit(Player p, Location location) {
        PortalExits.put(p, location);
    }

    public static void clearPortalExit(Player p) {
        PortalExits.remove(p);
    }
	
	

	

}

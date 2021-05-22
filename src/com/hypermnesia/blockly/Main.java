package com.hypermnesia.blockly;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main extends JavaPlugin implements Listener {

    FileConfiguration config = this.getConfig();
    private boolean gameIsRunning = false;
    private String targetBlock = "diamond_ore";
    private List<String> list = new ArrayList<String>();
    private List<String> Lost = new ArrayList<String>();
    private boolean typingTestIsActive = false;
    private List<String> phrases = new ArrayList<String>();
    private String chosenPhrase = "hippo is supreme";
    private Plugin plugin = this;
    private JSONObject jsonObject = new JSONObject();
    private boolean blockLimitEnabled = config.getBoolean("block-limit-enabled");
    private int blockLimit = config.getInt("block-limit") + 1;
    private boolean extraPainMode = config.getBoolean("extra-pain-mode");
    private int extraPainValue = config.getInt("extra-pain-value");
    private boolean opBypassScenario = config.getBoolean("op-bypass-scenario");
    private boolean healthDrain = false;
    private int randomX = this.randomWithRange(1000, 2500);
    private int randomZ = this.randomWithRange(1000, 2500);
    private Location eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 150, randomZ, 0, 0);
    private List<Biome> dodoBiomes = new ArrayList<Biome>();


    private int randomWithRange(int min, int max) {

        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;

    }


    private void executeScenario(String scenario, String[] args) {
        if (scenario.equalsIgnoreCase("tnt")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "TNT", ChatColor.RED + "TNT spawns on your feet.", 10, 20, 10);
                    world.spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "TNT: " + ChatColor.RED + "TNT spawns on your feet.");
        }
        if (scenario.equalsIgnoreCase("blindness")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Blindness", ChatColor.RED + "Blindness for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 2));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Blindness: " + ChatColor.RED + "Blindness for 1 minute.");

        }
        if (scenario.equalsIgnoreCase("hunger")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Hunger", ChatColor.RED + "Hunger 10 for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 9));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Hunger: " + ChatColor.RED + "Hunger 10 for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("wither")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Wither", ChatColor.RED + "Wither for 10 seconds.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 2));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Wither: " + ChatColor.RED + "Wither 3 for 10 seconds.");
        }
        if (scenario.equalsIgnoreCase("miningfatigue")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {
                } else {

                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Mining Fatigue", ChatColor.RED + "Mining Fatigue for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 0));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Mining Fatigue: " + ChatColor.RED + "Mining fatigue for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("clearinv")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Clear Inventory", ChatColor.RED + "Inventory Cleared.", 10, 20, 10);
                    player.getInventory().clear();
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Clear Inventory: " + ChatColor.RED + "Inventory Cleared.");
        }
        if (scenario.equalsIgnoreCase("roulette")) {
            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        if (opBypassScenario) {
                            Bukkit.broadcastMessage(ChatColor.RED + "You guys got lucky. The random player was OP.");
                            continue;
                        } else {
                        }
                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                        player.sendTitle(ChatColor.DARK_RED + "Roulette", ChatColor.RED + "Random player instantly dies.", 10, 20, 10);
                    }
                }
                Player randomplayer = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                randomplayer.setHealth(0);
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "Roulette: " + ChatColor.RED + "Random player instantly dies.");
            } catch (NullPointerException e) {
                Bukkit.broadcastMessage(ChatColor.RED + "You guys got lucky. The random player was somehow already dead.");
            }
        }
        if (scenario.equalsIgnoreCase("silverfish")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Silverfish", ChatColor.RED + "Spawns 10 silverfish at your feet.", 10, 20, 10);
                    for (int x = 0; x < 11; x++) {
                        World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                        world.spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Silverfish: " + ChatColor.RED + "Spawns 10 silverfish at your feet..");

        }
        if (scenario.equalsIgnoreCase("instantdamage")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Instant Damage", ChatColor.RED + "Receive Instant Damage 2.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Instant Damage: " + ChatColor.RED + "Receive Instant Damage 2.");
        }
        if (scenario.equalsIgnoreCase("typingtest")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Lost.add(player.getName());
                    typingTestIsActive = true;
                    String phrase;
                    phrases.add("hippo is supreme god");
                    phrases.add("i will give hippo my lunch");
                    phrases.add("i will buy hippo MVP++ rank on hypixel");
                    phrases.add("i will buy hippo ice cream");
                    phrases.add("petition to give hippo developer role");
                    phrases.add("unban hippo! (i'm looking at your etqrnity)");
                    phrases.add("cikn should invite hippo to his smp");
                    phrases.add("waeaweawe -Bluebled 2021");
                    phrase = phrases.get(this.randomWithRange(0, phrases.size()));
                    if (args.length > 1) {
                        StringJoiner joiner = new StringJoiner(" ");
                        for (int x = 1; x < args.length; x++) {
                            joiner.add(args[x]);
                        }
                        phrase = joiner.toString();
                    }
                    chosenPhrase = phrase;
                    player.sendTitle(ChatColor.DARK_RED + "Typing Test", ChatColor.RED + "Type the following phrase in chat within 10 seconds or you will die.", 10, 20, 10);
                    player.sendMessage(ChatColor.YELLOW + "Type the following phrase in 10 seconds: " + ChatColor.GOLD + phrase);
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    new BukkitRunnable() {
                        public void run() {
                            for (int x = 0; x < Lost.size(); x++) {
                                Bukkit.getPlayer(Lost.get(x)).setHealth(0);
                            }
                            typingTestIsActive = false;
                            Lost.clear();
                        }
                    }.runTaskLater(this, 200);
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Typing Test: " + ChatColor.RED + "Type the following phrase in chat within 10 seconds or you will die.");
        }
        if (scenario.equalsIgnoreCase("gapple")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_GREEN + "Gapple", ChatColor.GREEN + "Receive the effects of a golden apple.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Gapple: " + ChatColor.GREEN + "Receive the effects of a golden apple.");
        }
        if (scenario.equalsIgnoreCase("maniacminer")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_GREEN + "Maniac Miner", ChatColor.GREEN + "Haste 3 and Speed 1 for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Manic Miner: " + ChatColor.GREEN + "Haste 3 and speed 1 for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("efficiency")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_GREEN + "Efficiency", ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.", 10, 20, 10);
                    player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Efficiency: " + ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.");
        }
        if (scenario.equalsIgnoreCase("fireres")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_GREEN + "Fire Resistance", ChatColor.GREEN + "Fire Resistance for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Fire Resistance: " + ChatColor.GREEN + "Fire Resistance for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("invincibility")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_GREEN + "Invincibility", ChatColor.GREEN + "Immune to damage for 1 minute.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 4));
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Invincibility: " + ChatColor.GREEN + "Immune to damage for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("dog")) {
            Material itemType = Material.BONE;
            ItemStack itemStack = new ItemStack(itemType);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "Doggo", ChatColor.BLUE + "Spawns a cute doggo on your feet and gives you 3 bones. Aww.", 10, 20, 10);
                    World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                    world.spawnEntity(player.getLocation(), EntityType.WOLF);
                    for (int x = 0; x < 3; x++) {
                        if (player.getInventory().firstEmpty() == -1) {
                            Location loc = player.getLocation();
                            world.dropItemNaturally(loc, itemStack);
                        }
                        player.getInventory().addItem(itemStack);
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Doggo: " + ChatColor.BLUE + "Spawns a cute doggo on your feet and gives you 3 bones. Aww.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }
        if (scenario.equalsIgnoreCase("nomoving")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "No Moving", ChatColor.BLUE + "Moving is cringe anyway, Me and the boys use enderpearls.", 10, 20, 10);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7777777, 140));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 7777777, 140));
                    for (int x = 0; x < 64; x++) {
                        if (player.getInventory().firstEmpty() == -1) {
                            Location loc = player.getLocation();
                            World world = player.getWorld();

                            world.dropItemNaturally(loc, NoMovingPearls());
                        }
                        player.getInventory().addItem(NoMovingPearls());
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "No Moving: " + ChatColor.BLUE + "Moving is cringe anyway, Me and the boys use enderpearls.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }

        if (scenario.equalsIgnoreCase("fakefly")) {
            Material rocket = Material.FIREWORK_ROCKET;
            ItemStack gib = new ItemStack(rocket);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "Elytra", ChatColor.BLUE + "Get teleported to y=300 with an elytra and 5 firework rockets.", 10, 20, 10);
                    Location mlg = new Location(Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1), player.getLocation().getX(), 300, player.getLocation().getZ(), 0, 0);
                    player.teleport(mlg);
                    if (player.getInventory().firstEmpty() == -1) {
                        player.sendMessage(ChatColor.RED + "You didn't have enough room in your inventory! Shame. ok have fun mlg");
                    }
                    player.getInventory().addItem(FakeElytra());
                    for (int x = 0; x < 5; x++) {
                        if (player.getInventory().firstEmpty() == -1) {
                            break;
                        }
                        player.getInventory().addItem(gib);
                    }
                }
            }
            new BukkitRunnable() {
                public void run() {
                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Elytra: " + ChatColor.RED + "Sike.");
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 1.0F);
                        player.sendTitle(ChatColor.DARK_RED + "Elytra", ChatColor.RED + "Sike.", 10, 20, 10);
                        if (player.getInventory().contains(Material.ELYTRA)) {
                            Inventory inventory = player.getInventory();

                            for (ItemStack inv : inventory.getContents()) {

                                try {
                                    if (inv.getType().equals(Material.ELYTRA)) {
                                        player.getInventory().remove(inv);
                                    }
                                } catch (NullPointerException e) {

                                }
                            }
                        }
                        try {
                            if (player.getInventory().getChestplate().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "Free Elytra")) {
                                player.getInventory().setChestplate(null);
                            }
                        } catch (NullPointerException e) {

                        }
                    }
                }
            }.runTaskLater(this, 200);
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Elytra: " + ChatColor.BLUE + "Get teleported to y=300 with an elytra and 5 firework rockets.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }
        if (scenario.equalsIgnoreCase("anklemonitor")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    if (player.isOp() && opBypassScenario) {

                    } else {
                    }
                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "Ankle Monitor", ChatColor.BLUE + "No more snek for you.", 10, 20, 10);
                    player.getInventory().setBoots(AnkleMonitor());
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Ankle Monitor: " + ChatColor.BLUE + "No more snek for you.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }
    }

    void yeetWorld(World world) {
        Bukkit.unloadWorld(world, true);
        File worldFolder = new File(world.getName());
        try {
            FileUtils.deleteDirectory(worldFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    World createNewGameServer(String worldName) {
        WorldCreator wc = new WorldCreator(worldName);
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);
        World world = wc.createWorld();
        return world;
    }

    @Override
    public void onEnable() {
        System.out.println("[Blockly] server here again :D");
        config.addDefault("extra-pain-mode", false);
        config.addDefault("extra-pain-value", 2);
        config.addDefault("block-limit-enabled", false);
        config.addDefault("block-limit", 5);
        config.addDefault("op-bypass-scenario", true);
        config.options().copyDefaults(true);
        config.options().header("These are the default values.\n" +
                "These values can be temporarily changed at any time with the /blockly <setting> <value> command in game.\n" +
                "Temporary values are reset to the defaults in this file once the server is reloaded/restarted.");
        saveConfig();
        WorldCreator wc = new WorldCreator("lobby");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.createWorld();
        createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    String message = ChatColor.GOLD + "Current target block: " + ChatColor.GREEN + Material.matchMaterial(targetBlock);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                }
            }
        }, 20, 20);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (extraPainMode) {
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + "Friendly Reminder that " + ChatColor.DARK_RED + "Extra Pain Mode " + ChatColor.RED + "is currently active. ");
                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Extra Pain Mode: Take " + extraPainValue + " times as much damage whenever you are damaged.");
                } else {
                    return;
                }

            }
        }, 6000, 6000);
    }


    @Override
    public void onDisable() {
        System.out.println("[Blockly] server gone D:");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            if (label.equalsIgnoreCase("challenge") || label.equalsIgnoreCase("chal")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                if (player.hasPermission("challenge.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Invalid Value Dumbass, Your options are 'start'  or 'end'");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("start")) {
                        if (gameIsRunning == true) {
                            player.sendMessage(ChatColor.RED + "The game is already running!");
                            return true;
                        }

                        gameIsRunning = true;
                        list.add("TNT");
                        list.add("BLINDNESS");
                        list.add("HUNGER");
                        list.add("WITHER");
                        list.add("MININGFATIGUE");
                        list.add("CLEARINV");
                        list.add("ROULETTE");
                        list.add("SILVERFISH");
                        list.add("INSTANTDAMAGE");
                        list.add("TYPINGTEST");
                        list.add("GAPPLE");
                        list.add("MANIACMINER");
                        list.add("EFFICIENCY");
                        list.add("FIRERES");
                        list.add("INVINCIBILITY");
                        dodoBiomes.add(Biome.OCEAN);
                        dodoBiomes.add(Biome.COLD_OCEAN);
                        dodoBiomes.add(Biome.DEEP_OCEAN);
                        dodoBiomes.add(Biome.FROZEN_OCEAN);
                        dodoBiomes.add(Biome.LUKEWARM_OCEAN);
                        dodoBiomes.add(Biome.WARM_OCEAN);
                        dodoBiomes.add(Biome.DEEP_COLD_OCEAN);
                        dodoBiomes.add(Biome.DEEP_FROZEN_OCEAN);
                        dodoBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
                        dodoBiomes.add(Biome.DEEP_WARM_OCEAN);
                        new BukkitRunnable() {
                            int time = 10;

                            @Override
                            public void run() {
                                if (this.time == 0) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        while (dodoBiomes.contains(Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1)
                                                .getBiome(eventStartLoc.getBlockX(), eventStartLoc.getBlockY(), eventStartLoc.getBlockZ()))) {
                                            randomX = randomWithRange(1000, 2500);
                                            randomZ = randomWithRange(1000, 2500);
                                            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 150, randomZ, 0, 0);
                                        }
                                        player.teleport(eventStartLoc);
                                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 2));
                                        player.setHealth(20);
                                        player.setSaturation(20);
                                        player.setFoodLevel(20);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    cancel();
                                }

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    try {
                                        Bukkit.getServer().getWorld("lobby").playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Teleporting in " + time, ChatColor.GREEN + "Made by hippo (Hypermnesia)", 10, 20, 10);
                                    } catch (NullPointerException e) {

                                    }
                                }
                                this.time--;
                            }
                        }.runTaskTimer(this, 0L, 20L);

                        new BukkitRunnable() {
                            public void run() {
                                World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                                int otherBlockLimit = blockLimit + 1;
                                if (blockLimitEnabled) {
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "Event Started! " + ChatColor.AQUA + "First one to mine " + ChatColor.GOLD + otherBlockLimit + " " + Material.matchMaterial(targetBlock) + ChatColor.AQUA + "wins! Good Luck!");
                                }
                                if (!blockLimitEnabled) {
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "Event Started!" +  ChatColor.AQUA + "First one to mine the target block wins! Good Luck!");
                                }
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (blockLimitEnabled) {
                                        player.sendTitle(ChatColor.GOLD + "Event Started! ", ChatColor.AQUA + "First one to mine " + ChatColor.GOLD + otherBlockLimit + " " + Material.matchMaterial(targetBlock) + ChatColor.AQUA + "wins! Good Luck!", 10, 20, 10);
                                    }
                                    if (!blockLimitEnabled) {
                                        player.sendTitle(ChatColor.GOLD + "Event Started!", ChatColor.AQUA + "First one to mine the target block wins! Good Luck!", 10, 20, 10);
                                    }
                                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                                    player.sendMessage(ChatColor.DARK_GRAY + "You are playing on server " + player.getPlayer().getWorld().getName());
                                    world.setPVP(true);
                                    jsonObject.clear();
                                    jsonObject.put(player.getUniqueId(),0);
                                }
                            }
                        }.runTaskLater(this, 260);

                        new BukkitRunnable() {
                            public void run() {
                                int index = (int) (Math.random() * list.size());
                                if (!gameIsRunning) {
                                    this.cancel();
                                }
                                if (list.get(index).equalsIgnoreCase("tnt")) {
                                    executeScenario("tnt", null);
                                }
                                if (list.get(index).equalsIgnoreCase("blindness")) {
                                    executeScenario("blindness", null);
                                }
                                if (list.get(index).equalsIgnoreCase("hunger")) {
                                    executeScenario("hunger", null);
                                }
                                if (list.get(index).equalsIgnoreCase("wither")) {
                                    executeScenario("wither", null);
                                }
                                if (list.get(index).equalsIgnoreCase("miningfatigue")) {
                                    executeScenario("miningfatigue", null);
                                }
                                if (list.get(index).equalsIgnoreCase("clearinv")) {
                                    executeScenario("clearinv", null);
                                }
                                if (list.get(index).equalsIgnoreCase("roulette")) {
                                    executeScenario("roulette", null);
                                }
                                if (list.get(index).equalsIgnoreCase("silverfish")) {
                                    executeScenario("silverfish", null);
                                }
                                if (list.get(index).equalsIgnoreCase("instantdamage")) {
                                    executeScenario("instantdamage", null);
                                }
                                if (list.get(index).equalsIgnoreCase("typingtest")) {
                                    executeScenario("typingtest", null);
                                }
                                if (list.get(index).equalsIgnoreCase("gapple")) {
                                    executeScenario("gapple", null);
                                }
                                if (list.get(index).equalsIgnoreCase("maniacminer")) {
                                    executeScenario("maniacminer", null);
                                }
                                if (list.get(index).equalsIgnoreCase("efficiency")) {
                                    executeScenario("efficiency", null);
                                }
                                if (list.get(index).equalsIgnoreCase("fireres")) {
                                    executeScenario("fireres", null);
                                }
                                if (list.get(index).equalsIgnoreCase("invincibility")) {
                                    executeScenario("invincibility", null);
                                }
                            }

                        }.runTaskTimer(this, 2400, 2400);
                    }
                    if (args[0].equalsIgnoreCase("end")) {
                        gameIsRunning = false;
                        World world = Bukkit.getWorld("lobby");
                        player.sendMessage(ChatColor.RED + "Challenge ended.");
                        for (Player playerEnd : Bukkit.getOnlinePlayers()) {
                            playerEnd.sendTitle(ChatColor.LIGHT_PURPLE + "Challenge Ended", ChatColor.LIGHT_PURPLE + "Host ended challenge prematurely.", 10, 20, 10);
                            player.sendMessage(ChatColor.LIGHT_PURPLE + "Warping back to lobby soon...");
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    sendToServer(playerEnd, "lobby");
                                }
                            }.runTaskLater(this, 100);
                            world.setPVP(false);
                            Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                            playerEnd.getInventory().clear();
                            playerEnd.setGameMode(GameMode.ADVENTURE);
                            Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
                            yeetWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1));
                            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
                        }
                        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Challenge Ended, Host ended challenge prematurely.");
                        return true;
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Insufficient Permission");
                }
            }
            if (label.equalsIgnoreCase("executescenario") || label.equalsIgnoreCase("es")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                try {
                    Player player = (Player) sender;
                    if (player.hasPermission("executescenario.use")) {
                        this.executeScenario(args[0], args);
                        for (Player playerOP : Bukkit.getOnlinePlayers()) {
                            if (playerOP.isOp()) {
                                if (args[0].equalsIgnoreCase("tnt") || args[0].equalsIgnoreCase("blindness") || args[0].equalsIgnoreCase("hunger") || args[0].equalsIgnoreCase("wither") || args[0].equalsIgnoreCase("miningfatigue") || args[0].equalsIgnoreCase("clearinv") || args[0].equalsIgnoreCase("roulette") || args[0].equalsIgnoreCase("silverfish") || args[0].equalsIgnoreCase("instantdamage") || args[0].equalsIgnoreCase("typingtest")
                                        || args[0].equalsIgnoreCase("gapple") || args[0].equalsIgnoreCase("maniacminer") || args[0].equalsIgnoreCase("efficiency") || args[0].equalsIgnoreCase("fireres") || args[0].equalsIgnoreCase("invincibility") || args[0].equalsIgnoreCase("dog") || args[0].equalsIgnoreCase("nomoving") || args[0].equalsIgnoreCase("fakefly") || args[0].equalsIgnoreCase("anklemonitor")) {
                                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (" + args[0].toLowerCase() + ")");
                                } else {
                                    playerOP.sendMessage(ChatColor.RED + player.getName() + " attempted to execute a scenario but failed. Try checking the spelling.");
                                }
                            }
                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(ChatColor.GOLD + "Available list of scenarios: \n" + ChatColor.RED + "Bad scenarios: \nTNT,BLINDNESS,HUNGER,WITHER,MININGFATIGUE,CLEARINV,ROULETTE,SILVERFISH,INSTANTDAMAGE,TYPINGTEST.\n" + ChatColor.GREEN + "Good scenarios: \nGAPPLE,MANIACMINER,EFFICIENCY,FIRERES,INVINCIBILITY. \n" + ChatColor.BLUE + "Special scenarios: \n DOG,NOMOVING,FAKEFLY,ANKLEMONITOR");
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "Insufficient Permission");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    Player player = (Player) sender;
                    player.sendMessage(ChatColor.RED + "Usage: /executescenario <scenario>");
                }
            }
            if (label.equalsIgnoreCase("stickofban") || label.equalsIgnoreCase("sob")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                if (player.hasPermission("stickofban.use")) {
                    if (player.getInventory().firstEmpty() == -1) {
                        World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                        world.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                        player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                        return false;
                    }
                    player.getInventory().addItem(StickOfBan());
                    World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                    world.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
                    player.sendMessage(ChatColor.GOLD + "Be careful with that stick! It's real" + ChatColor.RED + " dangerous");
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "Insufficient Permission.");
                }
            }
            if (label.equalsIgnoreCase("hippospeak") || label.equalsIgnoreCase("hs")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                List<String> list = new ArrayList<String>();
                if (player.hasPermission("hippospeak.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /hippospeak <message>");
                        return true;
                    }
                    for (int x = 0; x < args.length; x++) {
                        list.add(args[x]);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (String s : list) {
                        sb.append(s);
                        sb.append(" ");
                    }
                    Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=========================");
                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "    Hippo has spoken!");
                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Hippo says: " + ChatColor.RED + sb.toString());
                    Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=========================");
                    for (Player playerAll : Bukkit.getOnlinePlayers()) {
                        World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                        world.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1.0F, 1.0F);
                        playerAll.sendTitle(ChatColor.DARK_RED + "Hippo has spoken!", ChatColor.RED + "Check the chat for more information.", 10, 20, 10);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You are not hippo >:O");
                }
            }
            if (label.equalsIgnoreCase("targetblock") || label.equalsIgnoreCase("tb")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                if (player.hasPermission("targetblock.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /targetblock <block>");
                        return true;
                    }
                    if (Material.matchMaterial(args[0]) == null) {
                        player.sendMessage(ChatColor.RED + "Unknown material. Material returned null.");
                        return true;
                    }
                    targetBlock = args[0];
                    player.sendMessage(ChatColor.GREEN + "Successfully set the target block to " + ChatColor.GOLD + Material.matchMaterial(targetBlock));
                    for (Player playerNotify : Bukkit.getOnlinePlayers()) {
                        playerNotify.sendTitle(ChatColor.DARK_PURPLE + "Target Block Changed", ChatColor.LIGHT_PURPLE + "New target block is " + Material.matchMaterial(targetBlock), 10, 20, 10);
                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                    }

                } else {
                    player.sendMessage(ChatColor.RED + "Insufficient Permission.");
                }
            }
            if (label.equalsIgnoreCase("warp")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                try {
                    Player player = (Player) sender;
                    if (player.hasPermission("warp.use")) {
                        if (args.length == 0) {
                            player.sendMessage(ChatColor.RED + "Usage: /warp <world>");
                            return true;
                        }
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "⭐ Warping to: " + args[0] + " ⭐");
                        sendToServer(player, args[0]);
                    }
                } catch (IllegalArgumentException e) {
                    Player player = (Player) sender;
                        player.sendMessage(ChatColor.RED + "World does not exist!");
                }
            }

            if (label.equalsIgnoreCase("blockly") || label.equalsIgnoreCase("b")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                try {
                    Player player = (Player) sender;
                    if (player.hasPermission("blockly.use")) {
                        if (args.length == 0) {
                            player.sendMessage(ChatColor.RED + "Usage: /blockly <setting> <value>");
                            return true;
                        }

                        if (args[0].equalsIgnoreCase("refresh")) {
                            ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                            Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                            Objective objective = scoreboard.registerNewObjective("Gold", "", "test");
                            int otherBlockLimit = blockLimit + 1;
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                            objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "⛏ Blockly ⛏");
                            Score score1 = objective.getScore(ChatColor.DARK_GRAY + "Made by hippo (Hypermnesia).");
                            Score score2 = objective.getScore(ChatColor.DARK_GRAY + "Server: " + player.getPlayer().getWorld().getName());
                            Score score3 = objective.getScore("");
                            if (blockLimitEnabled) {
                                Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + otherBlockLimit);
                                score4.setScore(4);
                            }
                            if (!blockLimitEnabled) {
                                Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + "OFF");
                                score4.setScore(4);
                            }
                            if (extraPainMode) {
                                Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + extraPainValue + "x");
                                score5.setScore(5);
                            }
                            if (!extraPainMode) {
                                Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + "OFF");
                                score5.setScore(5);
                             }
                            Score score6 = objective.getScore(ChatColor.AQUA + "Participants: " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
                            Score score7 = objective.getScore(ChatColor.YELLOW + "⚠ " + ChatColor.RED + "Nether & End Disabled" + ChatColor.YELLOW + " ⚠");
                            score1.setScore(1);
                            score2.setScore(2);
                            score3.setScore(3);
                            //score4.setScore(4);
                            //score5.setScore(5);
                            score6.setScore(6);
                            score7.setScore(7);
                            player.sendMessage(ChatColor.GREEN + "Scoreboard Refreshed!");
                            for (Player playerAll : Bukkit.getOnlinePlayers()) {
                                playerAll.setScoreboard(scoreboard);
                            }
                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(ChatColor.GREEN + "List of all available values: \n blockLimit, extraPain, extraPainMode, blockLimitEnabled. \n" + ChatColor.RED + "Experimental values: \n opBypassScenario,healthDrain");
                        }

                        if (args[0].equalsIgnoreCase("blocklimit")) {
                            blockLimit = Integer.parseInt(args[1]);
                            int otherBlockLimit = blockLimit + 1;
                            config.set("block-limit", blockLimit);
                            player.sendMessage(ChatColor.GREEN + "Block Limit is now set to " + ChatColor.GOLD + otherBlockLimit);
                        }
                        if (args[0].equalsIgnoreCase("extrapain")) {
                            extraPainValue = Integer.parseInt(args[1]);
                            config.set("extra-pain-value", extraPainValue);
                            player.sendMessage(ChatColor.GREEN + "Extra Pain Modifier is now set to " + ChatColor.GOLD + args[1]);
                        }
                        if (args[0].equalsIgnoreCase("extrapainmode")) {
                            if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                                extraPainMode = Boolean.parseBoolean(args[1]);
                                config.set("extra-pain-mode", extraPainMode);
                                player.sendMessage(ChatColor.GREEN + "Extra Pain Mode is now set to " + ChatColor.GOLD + args[1]);
                            } else {
                                player.sendMessage(ChatColor.RED + "Value must be either true or false.");
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("blocklimitenabled")) {
                            if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                                blockLimitEnabled = Boolean.parseBoolean(args[1]);
                                config.set("block-limit-enabled", blockLimitEnabled);
                                player.sendMessage(ChatColor.GREEN + "Block Limit Enabled is now set to " + ChatColor.GOLD + args[1]);
                            } else {
                                player.sendMessage(ChatColor.RED + "Value must be either true or false.");
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("opbypassscenario")) {
                            if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                                blockLimitEnabled = Boolean.parseBoolean(args[1]);
                                config.set("op-bypass-scenario", opBypassScenario);
                                player.sendMessage(ChatColor.GREEN + "Op Bypass Scenario is now set to " + ChatColor.GOLD + args[1]);
                            } else {
                                player.sendMessage(ChatColor.RED + "Value must be either true or false.");
                                return true;
                            }
                        }
                        if (args[0].equalsIgnoreCase("healthdrain")) {
                            if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                                healthDrain = Boolean.parseBoolean(args[1]);
                                player.sendMessage(ChatColor.GREEN + "Health Drain is now set to " + ChatColor.GOLD + args[1]);
                            } else {
                                player.sendMessage(ChatColor.RED + "Value must be either true or false.");
                                return true;
                            }
            }



                    } else {
                        player.sendMessage(ChatColor.RED + "Insufficient Permission.");
                    }
                } catch (NumberFormatException e) {
                    Player player = (Player) sender;
                    player.sendMessage(ChatColor.RED + "Value must be a number!");
                    return true;
                }
            }
            if (label.equalsIgnoreCase("yeetWorld") || label.equalsIgnoreCase("yw")) {
                try {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                        return true;
                    }
                    Player player = (Player) sender;
                    if (player.hasPermission("yeetworld.use")) {
                        if (args.length == 0) {
                            player.sendMessage(ChatColor.RED + "Usage: /yeetworld <world>");
                            return true;
                        }
                        player.sendMessage(ChatColor.RED + "Boom, World " + args[0].toLowerCase() + " was yeeted. ");
                        yeetWorld(Bukkit.getWorld(args[0]));
                    }
                } catch (NullPointerException e) {
                    Player player = (Player) sender;
                        player.sendMessage(ChatColor.RED + "Failed. The world does not exist.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (typingTestIsActive) {
            if (event.getMessage().equals(chosenPhrase)) {
                event.getPlayer().sendMessage(ChatColor.GREEN + "Congrats you get to live!" + ChatColor.DARK_PURPLE + " :)");
                Lost.remove(event.getPlayer().getName());
            }
        }
    }

    void sendToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
        Location loc = new Location(Bukkit.getWorld(server), 0, 250, 0, 0, 0);
        player.teleport(loc);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
            try {
                if (event.getBlock().getType().equals(Material.matchMaterial(targetBlock))) {
                    if (!gameIsRunning) return;
                    int blocksMined = (int) jsonObject.get(event.getPlayer().getUniqueId());
                    if (blockLimitEnabled) {
                        if (!(blocksMined == blockLimit)) {
                            blocksMined = blocksMined + 1;
                            jsonObject.put(event.getPlayer().getUniqueId(), blocksMined);
                        } else {
                            gameIsRunning = false;
                            playerWinBlockLimit(event);
                        }
                    }
                    if (!blockLimitEnabled) {
                        gameIsRunning = false;
                        playerWin(event);
                    }
                }
            } catch (NullPointerException e) {

            }
        }

    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent event) {
        try {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                if (((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "" + ChatColor.BOLD + "Stick of Ban")) {
                    if (event.getDamager().isOp()) {
                        world.spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ChatColor.RED + "You were given a third degree burn by " + ChatColor.GOLD + "The Stick of Ban" + ChatColor.RED + ".", null, null);
                        player.kickPlayer(ChatColor.RED + "You were given a third degree burn by " + ChatColor.GOLD + "The Stick of Ban" + ChatColor.RED + ".");
                    } else {
                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1.0F, 1.0F);
                        event.getDamager().sendMessage(ChatColor.RED + "how tf did you get this");
                        ((Player) event.getDamager()).getInventory().getItemInMainHand().setType(Material.AIR);
                        event.setCancelled(true);
                        for (Player playerOP : Bukkit.getOnlinePlayers()) {
                            if (playerOP.isOp()) {
                                playerOP.sendMessage(ChatColor.RED + ((Player) event.getDamager()).getDisplayName() + " has a stick of ban, might wanna check it out.");
                            }
                        }
                    }
                }
            }
        } catch (ClassCastException | NullPointerException e) {
        }
    }

    @EventHandler
    public void onPlayerFall (EntityDamageEvent event) {
        try {
            Player player = (Player) event.getEntity();
            if (player.getWorld().getName().equals("lobby")) event.setCancelled(true);
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "" + ChatColor.BOLD + "Moving Pearls") || player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "" + ChatColor.BOLD + "Moving Pearls")) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.SLOW);
                if (effect != null && effect.getAmplifier() == 140 ) {
                    if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        event.setCancelled(true);
                    }
                }
            }
        } catch (ClassCastException | NullPointerException e) {
        }
    }

    @EventHandler
    public void extraPainMode (EntityDamageEvent event) {
        try {
            if (extraPainMode) {
                if (!(event.getEntity() instanceof Player))
                    return;
                double dmg = event.getDamage();
                event.setDamage(dmg * extraPainValue);
            } else {
                return;
            }
        } catch (ClassCastException | NullPointerException e) {
        }
    }

    @EventHandler
    public void onPlayerMove (PlayerMoveEvent event) {
        try {
            Player player = event.getPlayer();
            if (healthDrain) {
                player.setHealth(player.getHealth() - 1);
            }
            if (player.getInventory().getBoots().getItemMeta().getDisplayName().equals(ChatColor.GOLD + "" + ChatColor.BOLD + "Ankle Monitor")) {
                PotionEffect effect = player.getPotionEffect(PotionEffectType.SLOW);
                if (effect != null && effect.getAmplifier() == 1) {
                    return;
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0));
            }
        } catch (ClassCastException | NullPointerException | IllegalArgumentException e) {
            Player player = event.getPlayer();
            if (healthDrain) {
                if (player.getHealth() < 1) player.setHealth(0);
            }

        }
    }

    @EventHandler
    public void onPlayerDeath (PlayerDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = event.getEntity();
            player.sendTitle(ChatColor.DARK_RED + "Fatality",ChatColor.RED + "Beeg Pain",40,40,40);
            player.sendMessage(ChatColor.BLUE +  "You died at " + ChatColor.DARK_BLUE + " X:" + ChatColor.DARK_BLUE + player.getLocation().getBlockX() + " , Y:" + player.getLocation().getBlockY() + " , Z:" + player.getLocation().getBlockZ());
        if (event.getDeathMessage().contains("drowned")) {
            event.setDeathMessage(player.getDisplayName() + " failed to press spacebar while in water");
        }
    }

    @EventHandler
    public void onPlayerRespawn (PlayerRespawnEvent event) {
        if (!gameIsRunning) {
            Location loc = new Location(Bukkit.getWorld("lobby"), 0, 250, 0, 0, 0);
            sendToServer(event.getPlayer(), "lobby");
            event.setRespawnLocation(loc);
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else {
            event.setRespawnLocation(eventStartLoc);
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 2));
                }
            }.runTaskLater(this, 10);
        }
    }

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent event) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Gold", "", "test");
        int otherBlockLimit = blockLimit + 1;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "⛏ Blockly ⛏");
        Score score1 = objective.getScore(ChatColor.DARK_GRAY + "Made by hippo (Hypermnesia).");
        Score score2 = objective.getScore(ChatColor.DARK_GRAY + "Server: " + event.getPlayer().getWorld().getName());
        Score score3 = objective.getScore("");
        if (blockLimitEnabled) {
            Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + otherBlockLimit);
            score4.setScore(4);
        }
        if (!blockLimitEnabled) {
            Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + "OFF");
            score4.setScore(4);
        }
        if (extraPainMode) {
            Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + extraPainValue + "x");
            score5.setScore(5);
        }
        if (!extraPainMode) {
            Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + "OFF");
            score5.setScore(5);
        }
        Score score6 = objective.getScore(ChatColor.AQUA + "Participants: " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
        Score score7 = objective.getScore(ChatColor.YELLOW + "⚠ " + ChatColor.RED + "Nether & End Disabled" + ChatColor.YELLOW + " ⚠");
        score1.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        //score4.setScore(4);
        //score5.setScore(5);
        score6.setScore(6);
        score7.setScore(7);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);

        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        int otherBlockLimit = blockLimit + 1;
        Objective objective = scoreboard.registerNewObjective("Gold", "","test");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "⛏ Blockly ⛏");
        Score score1 = objective.getScore(ChatColor.DARK_GRAY + "Made by hippo (Hypermnesia).");
        Score score2 = objective.getScore(ChatColor.DARK_GRAY + "Server: " + event.getPlayer().getWorld().getName());
        Score score3 = objective.getScore("");
        if (blockLimitEnabled) {
            Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + otherBlockLimit);
            score4.setScore(4);
        }
        if (!blockLimitEnabled) {
            Score score4 = objective.getScore(ChatColor.GREEN + "Block Limit: " + ChatColor.GOLD + "OFF");
            score4.setScore(4);
        }
        if (extraPainMode) {
            Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + extraPainValue + "x");
            score5.setScore(5);
        }
        if (!extraPainMode) {
            Score score5 = objective.getScore(ChatColor.GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + "OFF");
            score5.setScore(5);
        }
        Score score6 = objective.getScore(ChatColor.AQUA + "Participants: " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
        Score score7 = objective.getScore(ChatColor.YELLOW + "⚠ " + ChatColor.RED + "Nether & End Disabled" + ChatColor.YELLOW + " ⚠");
        score1.setScore(1);
        score2.setScore(2);
        score3.setScore(3);
        //score4.setScore(4);
        //score5.setScore(5);
        score6.setScore(6);
        score7.setScore(7);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(scoreboard);
        }
        if (!gameIsRunning) {
            sendToServer(event.getPlayer(), "lobby");
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
        } else {
            sendToServer(event.getPlayer(), Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1).getName());
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 600, 2));
                }
            }.runTaskLater(this, 10);
        }
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.GRAY + "Welcome to hippo's target block event. I spent several hours of my life on this plugin.");
    }

    public void playerWin (BlockBreakEvent event) {
        Player winner = event.getPlayer();
        Bukkit.broadcastMessage(ChatColor.GOLD + winner.getDisplayName() + ChatColor.GREEN + " has found the target block! " + ChatColor.AQUA + "(" + Material.matchMaterial(targetBlock) + ")");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
            player.sendTitle(ChatColor.GOLD + winner.getDisplayName() + ChatColor.GREEN + " has found the target block!", ChatColor.RED + "Please Wait, warping to lobby soon.", 10, 20, 10);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendToServer(player, "lobby");
                }
            }.runTaskLater(this, 100);
            Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
            yeetWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1));
            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
        }

}
    public void playerWinBlockLimit (BlockBreakEvent event) {
        Player winner = event.getPlayer();
        int otherBlockLimit = blockLimit + 1;
        Bukkit.broadcastMessage(ChatColor.GOLD + winner.getDisplayName() + ChatColor.GREEN + " was the first to mine " + otherBlockLimit + " " + ChatColor.AQUA + Material.matchMaterial(targetBlock) + ChatColor.GREEN + "!");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
            player.sendTitle(ChatColor.GOLD + winner.getDisplayName() + ChatColor.GREEN + " was the first to mine " + otherBlockLimit + " " + ChatColor.AQUA + Material.matchMaterial(targetBlock) + ChatColor.GREEN + "!", ChatColor.RED + "Please Wait, warping to lobby soon.", 10, 20, 10);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().clear();
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendToServer(player, "lobby");
                }
            }.runTaskLater(this, 100);
            Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
            yeetWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1));
            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
        }

    }
    /* public void executeFireworkBarrage() {
        new BukkitRunnable() {
            public void run() {
                World world = Bukkit.getWorld("lobby");
                Location f1 = new Location(Bukkit.getWorld("lobby"), -1, 98, 1, 0, 0);
                Location f2 = new Location(Bukkit.getWorld("lobby"), -1, 98, -1, 0, 0);
                Location f3 = new Location(Bukkit.getWorld("lobby"), 1, 98, -1, 0, 0);
                Location f4 = new Location(Bukkit.getWorld("lobby"), 1, 98, 1, 0, 0);
                world.spawnEntity(f1, EntityType.FIREWORK);
                world.spawnEntity(f2, EntityType.FIREWORK);
                world.spawnEntity(f3, EntityType.FIREWORK);
                world.spawnEntity(f4, EntityType.FIREWORK);
            }
        }.runTaskLater(this, 20);
    } */

    public ItemStack StickOfBan() {


        ItemStack stick = new ItemStack(Material.STICK);
        ItemMeta meta = (ItemMeta) stick.getItemMeta();
        stick.setItemMeta(meta);
        meta.setDisplayName(ChatColor.GOLD +  "" + ChatColor.BOLD + "Stick of Ban");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&dThe Almighty Stick of Ban"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&5Anyone hit with this stick will be banned."));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&a※&c Requires OP to use."));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 32767, true);
        meta.addEnchant(Enchantment.DAMAGE_ALL, 32767, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stick.setItemMeta(meta);

        return stick;
    }

    public ItemStack NoMovingPearls() {


        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta meta = (ItemMeta) pearl.getItemMeta();
        pearl.setItemMeta(meta);
        meta.setDisplayName(ChatColor.BLUE +  "" + ChatColor.BOLD + "Moving Pearls");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&', ""));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&9Moving Pearls"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&1Your only way to travel while the 'No Moving' scenario is active."));
        meta.setLore(lore);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 32767, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        pearl.setItemMeta(meta);

        return pearl;
    }

    public ItemStack AnkleMonitor() {


        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta meta = (ItemMeta) boots.getItemMeta();
        boots.setItemMeta(meta);
        meta.setDisplayName(ChatColor.GOLD +  "" + ChatColor.BOLD + "Ankle Monitor");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.RED + "🧂 Permanent Slowness 2 while Wearing.");
        lore.add(ChatColor.GREEN + "🍙 Permanent Resistance 1 while Wearing.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 32767, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 32767, true);
        meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 10, true);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        boots.setItemMeta(meta);

        return boots;
    }

    public ItemStack FakeElytra() {


        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = (ItemMeta) elytra.getItemMeta();
        elytra.setItemMeta(meta);
        meta.setDisplayName(ChatColor.BLUE +"Free Elytra");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.GOLD + "fly machine 2000");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.VANISHING_CURSE, 32767, true);
        meta.addEnchant(Enchantment.BINDING_CURSE, 32767, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        elytra.setItemMeta(meta);

        return elytra;
    }


}

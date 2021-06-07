package com.hypermnesia.blockly;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    private int blockLimit = config.getInt("block-limit");
    private boolean extraPainMode = config.getBoolean("extra-pain-mode");
    private int extraPainValue = config.getInt("extra-pain-value");
    private boolean opBypassScenario = config.getBoolean("op-bypass-scenario");
    private boolean healthDrain = false;
    private int randomX = this.randomWithRange(1000, 2500);
    private int randomZ = this.randomWithRange(1000, 2500);
    private Location eventStartLoc;
    private List<Biome> dodoBiomes = new ArrayList<Biome>();
    private Map<String, Integer> lb = new TreeMap<String, Integer>();
    private Inventory Inv;
    private ConsoleCommandSender console = getServer().getConsoleSender();
    private EntityPlayer npc;



    private int randomWithRange(int min, int max) {

        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;

    }


    private void executeScenario(String scenario, String[] args) {
        if (scenario.equalsIgnoreCase("tnt")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    World world = player.getWorld();
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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

                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Clear Inventory", ChatColor.RED + "Inventory Cleared.", 10, 20, 10);
                    player.getInventory().clear();
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Clear Inventory: " + ChatColor.RED + "Inventory Cleared.");
        }
        if (scenario.equalsIgnoreCase("roulette")) {
            try {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "Roulette: " + ChatColor.RED + "Random player instantly dies.");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isOp()) {
                        if (opBypassScenario) {
                            Bukkit.broadcastMessage(ChatColor.RED + "You guys got lucky. The random player was OP.");
                            return;
                        } else {
                        }
                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                        player.sendTitle(ChatColor.DARK_RED + "Roulette", ChatColor.RED + "Random player instantly dies.", 10, 20, 10);
                    }
                }
                Player randomplayer = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                randomplayer.setHealth(0);
            } catch (NullPointerException e) {
                Bukkit.broadcastMessage(ChatColor.RED + "You guys got lucky. The random player was somehow already dead.");
            }
        }
        if (scenario.equalsIgnoreCase("silverfish")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_RED + "Silverfish", ChatColor.RED + "Spawns 10 silverfish at your feet.", 10, 20, 10);
                    for (int x = 0; x < 11; x++) {
                        World world = player.getWorld();
                        world.spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                    }
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Silverfish: " + ChatColor.RED + "Spawns 10 silverfish at your feet.");

        }
        if (scenario.equalsIgnoreCase("instantdamage")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp() && opBypassScenario) {

                } else {
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "Doggo", ChatColor.BLUE + "Spawns a cute doggo on your feet and gives you 3 bones. Aww.", 10, 20, 10);
                    World world = player.getWorld();
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
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
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                    player.sendTitle(ChatColor.DARK_BLUE + "Elytra", ChatColor.BLUE + "Get teleported to y=300 with an elytra and 5 firework rockets.", 10, 20, 10);
                    Location mlg = new Location(player.getWorld(), player.getLocation().getX(), 300, player.getLocation().getZ(), 0, 0);
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
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1.0F, 1.0F);
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
                    if (player.isOp() && opBypassScenario) {

                    } else {
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
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

    public static Player getNearestPlayer() {
        Player nearest = null;
        for (Player p : Bukkit.getWorld("lobby").getPlayers()) {
                if (nearest == null) nearest = p;
                else if (p.getLocation().distance(new Location(Bukkit.getWorld("lobby"), 11.5, 6.0, -4.4)) < nearest.getLocation().distance(new Location(Bukkit.getWorld("lobby"), 11.5, 6.0, -4.4))) nearest = p;
        }
        return nearest;
    }

    void loadNPC(){
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)Bukkit.getWorld("lobby")).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "hippo");
        npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        Location location = new Location(Bukkit.getWorld("lobby"), 11.5, 6.0, -4.4, 73, -4);
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);
    }

    public void lookNPCPacket(Entity npc, Player player, float yaw, float pitch) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte)((yaw + 180) * 256 / 360)));
        connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getId(), (byte)((yaw + 180) * 256 / 360), (byte)((pitch * -1) * 256 / 360), true));
    }

    @Override
    public void onEnable() {
        try {
            System.out.println("[Blockly] server here again :D");
            config.addDefault("extra-pain-mode", false);
            config.addDefault("extra-pain-value", 1);
            config.addDefault("block-limit-enabled", false);
            config.addDefault("block-limit", 0);
            config.addDefault("op-bypass-scenario", true);
            config.options().copyDefaults(true);
            config.options().header("These are the default values.\n" +
                    "These values can be temporarily changed at any time with the /blockly <setting> <value> command in game.\n" +
                    "Temporary values are reset to the defaults in this file once the server is reloaded/restarted." +
                    "Note: When setting the 'block-limit' variable set the variable to 1 higher than what you want. ex: 0 = 1, 1 = 2");
            saveConfig();
            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
            WorldCreator wc = new WorldCreator("lobby");
            wc.environment(World.Environment.NORMAL);
            wc.type(WorldType.FLAT);
            wc.createWorld();
            this.getServer().getPluginManager().registerEvents(this, this);
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            updateScoreboard();
            loadNPC();
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
                    if (Bukkit.getWorld("lobby").getPlayers().size() != 0) {
                        for (Player player : Bukkit.getWorld("lobby").getPlayers()) {
                            Player nearest = getNearestPlayer();
                            if (nearest != null) {
                                lookNPCPacket(npc, player, nearest.getLocation().getYaw(), nearest.getLocation().getPitch());
                            }
                        }
                    }
                }
            }, 5, 5);
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
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("[Blockly] No idea why but you need to reload the server to have the plugin work.");
        }
    }


    @Override
    public void onDisable() {
        System.out.println("[Blockly] server gone D:");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        try {
            addNPCPacket(npc, (Player) sender);
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
                    if (!(args[0].equalsIgnoreCase("start") || args[0].equalsIgnoreCase("end"))) {
                        player.sendMessage(ChatColor.RED + "Invalid Value Dumbass, Your options are 'start'  or 'end'");
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
                        dodoBiomes.add(Biome.SWAMP);
                        dodoBiomes.add(Biome.SWAMP_HILLS);
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
                                            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
                                        }
                                        player.teleport(eventStartLoc);
                                        Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 2));
                                        player.setHealth(20);
                                        player.setSaturation(20);
                                        player.setFoodLevel(20);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    cancel();
                                }

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6.9F, 6.9F);
                                        player.sendTitle(ChatColor.RED + "Teleporting in " + time, ChatColor.GREEN + "Made by hippo (Hypermnesia)", 10, 20, 10);
                                }
                                this.time--;
                            }
                        }.runTaskTimer(this, 0L, 20L);

                        new BukkitRunnable() {
                            public void run() {
                                World world = Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1);
                                int otherBlockLimit = blockLimit + 1;
                                if (blockLimitEnabled) {
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "Event Started! " + ChatColor.AQUA + "First one to mine " + ChatColor.GOLD + otherBlockLimit + " " + Material.matchMaterial(targetBlock) + ChatColor.AQUA + " wins! Good Luck!");
                                }
                                if (!blockLimitEnabled) {
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "Event Started! " +  ChatColor.AQUA + "First one to mine the target block wins! Good Luck!");
                                }
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    if (blockLimitEnabled) {
                                        player.sendTitle(ChatColor.GOLD + "Event Started!", ChatColor.AQUA + "First one to mine " + ChatColor.GOLD + otherBlockLimit + " " + Material.matchMaterial(targetBlock) + ChatColor.AQUA + " wins! Good Luck!", 10, 20, 10);
                                    }
                                    if (!blockLimitEnabled) {
                                        player.sendTitle(ChatColor.GOLD + "Event Started!", ChatColor.AQUA + "First one to mine the target block wins! Good Luck!", 10, 20, 10);
                                    }
                                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                                    player.sendMessage(ChatColor.DARK_GRAY + "You are playing on server " + player.getPlayer().getWorld().getName());
                                    world.setPVP(true);
                                    jsonObject.clear();
                                    for (Player p: Bukkit.getOnlinePlayers()) jsonObject.put(String.valueOf(p.getUniqueId()), 0);
                                    updateScoreboard();
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
                        }
                        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Challenge Ended, Host ended challenge prematurely.");
                        createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
                        Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
                        eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
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
                                    console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (" + args[0].toLowerCase() + ")");
                                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (" + args[0].toLowerCase() + ")");
                                } else {
                                    console.sendMessage(ChatColor.RED + player.getName() + " attempted to execute a scenario but failed. Try checking the spelling.");
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
                    player.getWorld().playSound(player.getLocation(),Sound.ITEM_BOOK_PAGE_TURN,1.0F,1.0F);
                    createInv();  player.openInventory(Inv);
                }
            }
            if (label.equalsIgnoreCase("hippospeak") || label.equalsIgnoreCase("hs")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                List<String> list = new ArrayList<String>();
                if (player.getUniqueId().toString().equals("7e2ad381-193e-40e2-adfe-8df266134d8c")) {
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
                            updateScoreboard();
                            player.sendMessage(ChatColor.GREEN + "Scoreboard refreshed!");
                        }
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(ChatColor.GREEN + "List of all available values: \n blockLimit, extraPain, extraPainMode, blockLimitEnabled, healthDrain, targetBlock, give \n" + ChatColor.RED + "Experimental values: \n opBypassScenario, createNewGameServer, getActiveGameServer");
                        }

                        if (args[0].equalsIgnoreCase("blocklimit")) {
                            blockLimit = Integer.parseInt(args[1]);
                            int otherBlockLimit = blockLimit + 1;
                            config.set("block-limit", blockLimit);
                            updateScoreboard();
                            player.sendMessage(ChatColor.GREEN + "Block Limit is now set to " + ChatColor.GOLD + otherBlockLimit);
                        }
                        if (args[0].equalsIgnoreCase("extrapain")) {
                            extraPainValue = Integer.parseInt(args[1]);
                            config.set("extra-pain-value", extraPainValue);
                            updateScoreboard();
                            player.sendMessage(ChatColor.GREEN + "Extra Pain Modifier is now set to " + ChatColor.GOLD + args[1]);
                        }
                        if (args[0].equalsIgnoreCase("extrapainmode")) {
                            if (args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")) {
                                extraPainMode = Boolean.parseBoolean(args[1]);
                                config.set("extra-pain-mode", extraPainMode);
                                updateScoreboard();
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
                        if (args[0].equalsIgnoreCase("createnewgameserver")) {
                            player.sendMessage(ChatColor.DARK_AQUA + "Creating new game server. Please allow up to a minute for the server to create.");
                            player.sendMessage(ChatColor.DARK_AQUA + "The server will lag.");
                            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
                            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
                            player.sendMessage(ChatColor.GREEN + "Server Created!");
                        }
                        if (args[0].equalsIgnoreCase("getactivegameserver")) {
                            player.sendMessage(ChatColor.DARK_PURPLE + "Current game server: " + ChatColor.LIGHT_PURPLE + Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1).getName());
                        }
                        if (args[0].equalsIgnoreCase("targetblock") || args[0].equalsIgnoreCase("tb")) {
                            if (player.hasPermission("targetblock.use")) {
                                if (args.length == 0) {
                                    player.sendMessage(ChatColor.RED + "Usage: /targetblock <block>");
                                    return true;
                                }
                                if (Material.matchMaterial(args[1]) == null) {
                                    player.sendMessage(ChatColor.RED + "Unknown material. Material returned null.");
                                    return true;
                                }
                                targetBlock = args[1];
                                player.sendMessage(ChatColor.GREEN + "Successfully set the target block to " + ChatColor.GOLD + Material.matchMaterial(targetBlock));
                                for (Player playerNotify : Bukkit.getOnlinePlayers()) {
                                    playerNotify.sendTitle(ChatColor.DARK_PURPLE + "Target Block Changed", ChatColor.LIGHT_PURPLE + "New target block is " + Material.matchMaterial(targetBlock), 10, 20, 10);
                                    Bukkit.getServer().getWorlds().get(Bukkit.getWorlds().size() - 1).playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
                                }
                            }
                        }

                        if (args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("g")) {
                            if (player.hasPermission("blocklygive.use")) {
                                if (args[1].equalsIgnoreCase("list")) {
                                    player.sendMessage(ChatColor.GREEN + "List of available items: opBerry, stickOfBan, fireballLauncher, cynide");
                                }
                                if (args[1].equalsIgnoreCase("opberry")) {
                                    try {
                                        if (player.getInventory().firstEmpty() == -1) {
                                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                                            player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                                            return false;
                                        }
                                        for (int x = 0; x < Integer.parseInt(args[2]); x++) {
                                            player.getInventory().addItem(Berry());
                                        }
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you " + Integer.parseInt(args[2]) + ChatColor.LIGHT_PURPLE + " " +
                                                ChatColor.BOLD + "Overpowered Berry" + ChatColor.GREEN + "!");
                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0F, 1.0F);
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you an" + ChatColor.LIGHT_PURPLE + " " +
                                                ChatColor.BOLD + "Overpowered Berry" + ChatColor.GREEN + "!");
                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT_SWEET_BERRY_BUSH, 1.0F, 1.0F);
                                        player.getInventory().addItem(Berry());
                                        return true;
                                    }
                                }
                                if (args[1].equalsIgnoreCase("stickofban") || args[1].equalsIgnoreCase("sob")) {
                                    try {
                                        if (!(sender instanceof Player)) {
                                            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                                            return true;
                                        }
                                        if (player.getInventory().firstEmpty() == -1) {
                                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                                            player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                                            return false;
                                        }
                                        for (int x = 0; x < Integer.parseInt(args[2]); x++) {
                                            player.getInventory().addItem(StickOfBan());
                                        }
                                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you " + Integer.parseInt(args[2]) + ChatColor.GOLD + " " +
                                                ChatColor.BOLD + "Stick of Ban" + ChatColor.GREEN + "!");
                                        player.sendMessage(ChatColor.GOLD + "Be careful with that stick! It's real" + ChatColor.RED + " dangerous");
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        player.getInventory().addItem(StickOfBan());
                                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you an " + ChatColor.GOLD + "" +
                                                ChatColor.BOLD + "Stick of Ban" + ChatColor.GREEN + "!");
                                        player.sendMessage(ChatColor.GOLD + "Be careful with that stick! It's real" + ChatColor.RED + " dangerous");
                                        return true;
                                    }
                                }

                                if (args[1].equalsIgnoreCase("fireballlauncher") || args[1].equalsIgnoreCase("fbl")) {
                                    try {
                                        if (!(sender instanceof Player)) {
                                            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                                            return true;
                                        }
                                        if (player.getInventory().firstEmpty() == -1) {
                                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                                            player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                                            return false;
                                        }
                                        for (int x = 0; x < Integer.parseInt(args[2]); x++) {
                                            player.getInventory().addItem(FireballLauncher());
                                        }
                                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you " + Integer.parseInt(args[2]) + ChatColor.RED + " " +
                                                ChatColor.BOLD + "Fireball launcher" + ChatColor.GREEN + "!");
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        player.getInventory().addItem(FireballLauncher());
                                        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you an " + ChatColor.RED + "" +
                                                ChatColor.BOLD + "Fireball launcher" + ChatColor.GREEN + "!");
                                        return true;
                                    }
                                }

                                if (args[1].equalsIgnoreCase("cynide")) {
                                    try {
                                        if (!(sender instanceof Player)) {
                                            sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                                            return true;
                                        }
                                        if (player.getInventory().firstEmpty() == -1) {
                                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                                            player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                                            return false;
                                        }
                                        for (int x = 0; x < Integer.parseInt(args[2]); x++) {
                                            player.getInventory().addItem(Cynide());
                                        }
                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you " + Integer.parseInt(args[2]) + ChatColor.DARK_RED + "Cynide" + ChatColor.GREEN + "!");
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        player.getInventory().addItem(Cynide());
                                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, 1.0F, 1.0F);
                                        player.sendMessage(ChatColor.GREEN + "Successfully gave you an " + ChatColor.DARK_RED + "Cynide" + ChatColor.GREEN + "!");
                                        return true;
                                    }
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "Insufficient Permission.");
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
                                if (args[0].equalsIgnoreCase("lobby")) {
                                    player.sendMessage(ChatColor.RED + "NO");
                                    return true;
                                }
                                player.sendMessage(ChatColor.RED + "Boom, World " + args[0].toLowerCase() + " was yeeted. ");
                                yeetWorld(Bukkit.getWorld(args[0]));
                            } else {
                                player.sendMessage(ChatColor.RED + "Insufficient Permission.");
                            }
                        } catch (NullPointerException e) {
                            Player player = (Player) sender;
                            player.sendMessage(ChatColor.RED + "Failed. The world does not exist.");
                        }
                    }

            if (label.equalsIgnoreCase("hipponick")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                if (player.getUniqueId().toString().equals("7e2ad381-193e-40e2-adfe-8df266134d8c")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /hipponick <name>");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase(".reset")) {
                        player.setDisplayName(player.getName());
                        player.sendMessage(ChatColor.GREEN + "Your nick has been reset");
                        return true;
                    }
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "Your new nick is " + ChatColor.GOLD + "" + args[0]);
                    player.setDisplayName(args[0]);
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "You are not hippo >:O");
                }
            }

            if (label.equalsIgnoreCase("massspawn") || label.equalsIgnoreCase("ms")) {
                try {
                    Player player = (Player) sender;
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                        return true;
                    }
                    if (player.hasPermission("massspawn.use")) {
                        if (args.length == 0) {
                            player.sendMessage(ChatColor.RED + "Usage: /massspawn <entity> <number>");
                            return true;
                        }
                        if (args.length < 2) {
                            player.sendMessage(ChatColor.RED + "Usage: /massspawn <entity> <number>");
                            return true;
                        }
                        for (int x = 0; x < Integer.parseInt(args[1]); x++) {
                            player.getWorld().spawnEntity(player.getLocation(), EntityType.valueOf(args[0]));
                        }
                        player.sendMessage(ChatColor.GREEN + "Successfully spawned " + args[1] + " " + args[0]);
                    }
                } catch (NumberFormatException e) {
                    Player player = (Player) sender;
                        player.sendMessage(ChatColor.RED + "Argument needs to be a number!");
                        return true;
                }
            }

            if (label.equalsIgnoreCase("executeplayer") || label.equalsIgnoreCase("ep")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(ChatColor.RED + "This command cannot be executed from console, Try doing it in game.");
                    return true;
                }
                Player player = (Player) sender;
                if (player.hasPermission("executeplayer.use")) {
                    if (args.length == 0) {
                        player.sendMessage(ChatColor.RED + "Usage: /executeplayer <target>");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase(player.getName())) {
                        player.sendMessage(ChatColor.RED + "You can't execute yourself.");
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + "Executing target " + args[0]);
                    Player target = getServer().getPlayer(args[0]);
                    if (!target.isDead()) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                target.getWorld().strikeLightning(target.getLocation());
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 1.0F, 1.0F);
                                if (target.isDead()) {
                                    player.sendMessage(ChatColor.RED + "Target eliminated");
                                    cancel();
                                }
                            }
                        }.runTaskTimer(this, 5, 5);
                    } else {
                        player.sendMessage(ChatColor.RED + "The target is already dead!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Insufficient Permission");
                }
            }

        } catch (Exception e) {
        }
        return false;
    }

    public void addNPCPacket(EntityPlayer npc, Player player) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
    }

    void updateScoreboard() {
        for (Player playerAll : Bukkit.getOnlinePlayers()) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("Gold", "", "test");
        int otherBlockLimit = blockLimit + 1;
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "⛏ Blockly ⛏");
        Score score1 = objective.getScore(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "Leaderboard");
        Score score2 = objective.getScore(" ");
        Score score3 = objective.getScore(ChatColor.DARK_GREEN + "Block Limit: " + ChatColor.GOLD + otherBlockLimit);
        Score score4 = objective.getScore(ChatColor.DARK_GREEN + "Extra Pain Modifier: " + ChatColor.GOLD + extraPainValue + "x");
        Score score5 = objective.getScore(ChatColor.AQUA + "Participants: " + ChatColor.GOLD + Bukkit.getOnlinePlayers().size());
        Score score6 = objective.getScore(ChatColor.YELLOW + "⚠ " + ChatColor.RED + "Nether & End Disabled" + ChatColor.YELLOW + " ⚠");
        Score score7 = objective.getScore("");
        Score score8 = objective.getScore(ChatColor.DARK_GRAY + "Server: " + playerAll.getPlayer().getWorld().getName());
        Score score9 = objective.getScore(ChatColor.DARK_GRAY + "Made by hippo (Hypermnesia)");
        score1.setScore(6);
        score2.setScore(7);
        score3.setScore(8);
        score4.setScore(9);
        score5.setScore(10);
        score6.setScore(11); 
        score7.setScore(2);
        score8.setScore(1);
        score9.setScore(0);
        if (gameIsRunning) {
            List<String> players = new ArrayList<String>();
            players.addAll(lb.keySet());
            Bukkit.broadcastMessage(ChatColor.RED + String.valueOf(players));
            try {
                objective.getScore(ChatColor.GOLD + "1. " + Bukkit.getPlayer(UUID.fromString(players.get(0)))
                        .getName() + ": " + lb.get(players.get(0))).setScore(5);
                objective.getScore(ChatColor.GRAY + "2. " + Bukkit.getPlayer(UUID.fromString(players.get(1)))
                        .getName() + ": " + lb.get(players.get(1))).setScore(4);
                objective.getScore(ChatColor.GREEN + "3. " + Bukkit.getPlayer(UUID.fromString(players.get(2)))
                        .getName() + ": " + lb.get(players.get(2))).setScore(3);
            } catch (IndexOutOfBoundsException e) {
                playerAll.setScoreboard(scoreboard);
            }
        }
        playerAll.setScoreboard(scoreboard);
        }
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

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
            try {
                if (event.getBlock().getType().equals(Material.matchMaterial(targetBlock))) {
                    if (!gameIsRunning) return;
                    int blocksMined = (int) jsonObject.get(String.valueOf(event.getPlayer().getUniqueId()));
                    if (blockLimitEnabled) {
                        if (!(blocksMined == blockLimit)) {
                            blocksMined = blocksMined + 1;
                            jsonObject.put(String.valueOf(event.getPlayer().getUniqueId()), blocksMined);
                            for (Object key : jsonObject.keySet()) {
                                String element = (String) key;
                                int value = (int) jsonObject.get(element);
                                lb.put(element, value);
                            }
                            event.getPlayer().sendMessage(String.valueOf(jsonObject));
                            lb = sortByValue(lb);
                            event.getPlayer().sendMessage("Current LB: " + String.valueOf(lb));
                            updateScoreboard();
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
                if (((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "" + ChatColor.BOLD + "Stick of Ban")) {
                    if (event.getDamager().isOp()) {
                        player.getWorld().strikeLightning(player.getLocation());
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_IMPACT,1.0F,1.0F);
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
                if (effect != null && effect.getAmplifier() == 140) {
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
            PotionEffect effectSlow = player.getPotionEffect(PotionEffectType.SLOW_FALLING);
            if (effectSlow != null && effectSlow.getAmplifier() == 2) {
                if(event.getPlayer().isOnGround()) {
                    event.getPlayer().removePotionEffect(PotionEffectType.SLOW_FALLING);
                }
            }
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
        if (event.getDeathMessage().contains("tried to swim in lava")) {
            event.setDeathMessage(player.getDisplayName() + " took a dip in the orange juice");
        }
        if (event.getDeathMessage().contains("burned to death")) {
            event.setDeathMessage(player.getDisplayName() + " didn't know what stop, drop and roll meant");
        }
        if (event.getDeathMessage().contains("went up in flames")) {
            event.setDeathMessage(player.getDisplayName() + " got toasted");
        }
    }

    @EventHandler
    public void onPlayerRespawn (PlayerRespawnEvent event) {
        if (!gameIsRunning) {
            Location loc = new Location(Bukkit.getWorld("lobby"), 0, 250, 0, 0, 0);
            sendToServer(event.getPlayer(), "lobby");
            event.setRespawnLocation(loc);
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            event.getPlayer().setSaturation(20);
        } else {
            event.setRespawnLocation(eventStartLoc);
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 2));
                }
            }.runTaskLater(this, 10);
        }
    }

    @EventHandler
    public void onWorldChange (PlayerChangedWorldEvent event) {
        updateScoreboard();
        if (event.getPlayer().getWorld().getName().equals("lobby")) {
            addNPCPacket(npc, event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        jsonObject.put(String.valueOf(event.getPlayer().getUniqueId()), 0);
        updateScoreboard();
        if (!gameIsRunning) {
            sendToServer(event.getPlayer(), "lobby");
            event.getPlayer().setGameMode(GameMode.ADVENTURE);
            addNPCPacket(npc, event.getPlayer());
        } else {
            sendToServer(event.getPlayer(), Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1).getName());
            event.getPlayer().teleport(eventStartLoc);
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 2));
                }
            }.runTaskLater(this, 10);
        }
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.GRAY + "Welcome to hippo's target block event. I spent several hours of my life on this plugin.");
    }

    @EventHandler
    public void onPlayerHarvest (PlayerHarvestBlockEvent event) {
        if (event.getHarvestedBlock().getType().equals(Material.SWEET_BERRY_BUSH)) {
            int chance = this.randomWithRange(1,100);
            if (chance < 5) {
                event.getPlayer().sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Lucky! " + ChatColor.YELLOW + "You found an " + ChatColor.LIGHT_PURPLE + "" +
                        ChatColor.BOLD + "Overpowered Berry" + ChatColor.YELLOW + "!");
                event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(),Berry());
                event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(),Sound.ENTITY_VILLAGER_YES,1.0F,1.0F);
            }
        }
    }

    @EventHandler
    public void onPlayerEat (PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType().equals(Material.SWEET_BERRIES)) {
            if (event.getItem().getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Overpowered Berry")) {
                    player.setHealth(20);
                    player.setAbsorptionAmount(6);
                    player.giveExpLevels(3);
                    player.setFoodLevel(20);
                    player.setSaturation(20);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 0));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Wowza! " + ChatColor.YELLOW + "You ate the " + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Overpowered Berry" +
                            ChatColor.YELLOW + "! Delicious!");
                    player.getWorld().playSound(player.getLocation(),Sound.BLOCK_ANVIL_PLACE,1.0F,1.0F);
            }
            }
        }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() == Material.SWEET_BERRY_BUSH) {
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            if (itemMeta.getDisplayName().equals(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Overpowered Berry")) {
                player.sendMessage(ChatColor.RED + "These berries are meant for consumption, not planting idiot.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler()
    public void onArmorStandClick(PlayerInteractAtEntityEvent event){
            if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
                event.getPlayer().sendMessage(ChatColor.GOLD + "[Hippo]: " + ChatColor.GREEN + "hippo");
            }
    }

    @EventHandler()
    public void onClick(PlayerInteractEvent event) {
        if(event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.DIAMOND_SWORD))
            if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                Player player = event.getPlayer();
                if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.RED +  "" + ChatColor.BOLD + "Fireball Launcher"))
                    if(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            player.launchProjectile(Fireball.class);
                            player.sendMessage(ChatColor.RED + "Launched Fireball!");
                            player.getInventory().setItemInMainHand(FireballLauncherCooldown());
                            BukkitTask bukkitTask = new BukkitRunnable() {
                                public void run() {
                                    Inventory inventory = player.getInventory();

                                    for (ItemStack inv : inventory.getContents()) {

                                        try {
                                            if (inv.getItemMeta().getDisplayName().equals(FireballLauncherCooldown().getItemMeta().getDisplayName())) {
                                                inv.setType(Material.DIAMOND_SWORD);
                                                inv.setItemMeta(FireballLauncher().getItemMeta());
                                            }
                                        } catch (NullPointerException e) {

                                        }
                                    }                                }
                            }.runTaskLater(this, 100);
                        }
                }
            }
    }

    @EventHandler
    public void onCLick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains(ChatColor.RED + "Scenarios"))
            return;
        if (event.getCurrentItem() == null) return;
        if (event.getCurrentItem().getItemMeta() == null) return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        if (event.getSlot() == 1) {
            this.executeScenario("tnt", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (tnt)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (tnt)");
                }
            }
        }

        if (event.getSlot() == 2) {
            this.executeScenario("blindness", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (blindness)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (blindness)");
                }
            }
        }
        if (event.getSlot() == 3) {
            this.executeScenario("hunger", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (hunger)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (hunger)");
                }
            }
        }
        if (event.getSlot() == 4) {
            this.executeScenario("wither", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (wither)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (wither)");
                }
            }
        }
        if (event.getSlot() == 5) {
            this.executeScenario("miningfatigue", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (miningfatigue)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (miningfatigue)");
                }
            }
        }
        if (event.getSlot() == 6) {
            this.executeScenario("clearinv", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (clearinv)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (clearinv)");
                }
            }
        }
        if (event.getSlot() == 7) {
            this.executeScenario("roulette", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (roulette)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (roulette)");
                }
            }
        }
        if (event.getSlot() == 8) {
            this.executeScenario("silverfish", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (silverfish)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (silverfish)");
                }
            }
        }
        if (event.getSlot() == 10) {
            this.executeScenario("instantdamage", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (instantdamage)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (instantdamage)");
                }
            }
        }
        if (event.getSlot() == 11) {
            this.executeScenario("typingtest", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (typingtest)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (typingtest)");
                }
            }
        }
        if (event.getSlot() == 19) {
            this.executeScenario("gapple", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (gapple)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (gapple)");
                }
            }
        }
        if (event.getSlot() == 20) {
            this.executeScenario("maniacminer", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (maniacminer)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (maniacminer)");
                }
            }
        }
        if (event.getSlot() == 21) {
            this.executeScenario("efficiency", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (efficiency)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (efficiency)");
                }
            }
        }
        if (event.getSlot() == 22) {
            this.executeScenario("fireres", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (fireres)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (fireres)");
                }
            }
        }
        if (event.getSlot() == 23) {
            this.executeScenario("invincibility", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (invincibility)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (invincibility)");
                }
            }
        }
        if (event.getSlot() == 28) {
            this.executeScenario("dog", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (dog)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (dog)");
                }
            }
        }
        if (event.getSlot() == 29) {
            this.executeScenario("nomoving", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (nomoving)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (nomoving)");
                }
            }
        }
        if (event.getSlot() == 30) {
            this.executeScenario("fakefly", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (fakefly)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (fakefly)");
                }
            }
        }
        if (event.getSlot() == 31) {
            this.executeScenario("anklemonitor", null);
            player.closeInventory();
            console.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (anklemonitor)");
            for (Player playerOP : Bukkit.getOnlinePlayers()) {
                if (playerOP.isOp()) {
                    playerOP.sendMessage(ChatColor.RED + player.getName() + " executed a scenario. (anklemonitor)");
                }
            }
        }
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
            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
            Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
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
            createNewGameServer(Integer.toString(this.randomWithRange(10000, 99999), 16));
            Bukkit.unloadWorld(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), false);
            eventStartLoc = new Location(Bukkit.getWorlds().get(Bukkit.getWorlds().size() - 1), randomX, 200, randomZ, 0, 0);
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

    public ItemStack Cynide() {

        ItemStack cynide = new ItemStack(Material.POTION);
        ItemMeta itemMeta = cynide.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_RED + "Cynide");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.RED + "Yes it's spelled wrong on purpose. What are you going to do about it.");
        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        PotionMeta potionMeta = ((PotionMeta) itemMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.CONFUSION, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.POISON, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BAD_OMEN, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1200, 0), true);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.JUMP, 1200, 0), true);
        potionMeta.setColor(Color.FUCHSIA);
        cynide.setItemMeta(potionMeta);

        return cynide;
    }

    public ItemStack FireballLauncherCooldown() {


        ItemStack fblc = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta meta = (ItemMeta) fblc.getItemMeta();
        fblc.setItemMeta(meta);
        meta.setDisplayName(ChatColor.GRAY +  "" + ChatColor.BOLD + "Fireball Launcher");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.RED + "On Cooldown! (5 Seconds)");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        fblc.setItemMeta(meta);

        return fblc;
    }

    public ItemStack FireballLauncher() {


        ItemStack fbl = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = (ItemMeta) fbl.getItemMeta();
        fbl.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RED +  "" + ChatColor.BOLD + "Fireball Launcher");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.RED + "Left Click to launch a fireball");
        lore.add(ChatColor.RED + "Cooldown: 5 seconds");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.KNOCKBACK, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        fbl.setItemMeta(meta);

        return fbl;
    }

    public ItemStack Berry() {


        ItemStack berry = new ItemStack(Material.SWEET_BERRIES);
        ItemMeta meta = (ItemMeta) berry.getItemMeta();
        berry.setItemMeta(meta);
        meta.setDisplayName(ChatColor.LIGHT_PURPLE +  "" + ChatColor.BOLD + "Overpowered Berry");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Eat to gain the following effects:");
        lore.add(ChatColor.BOLD + "");
        lore.add(ChatColor.DARK_RED + "Gain fire resistance for 1 minute.");
        lore.add(ChatColor.RED + "Instantly heal to max health.");
        lore.add(ChatColor.YELLOW + "Gain 6 HP worth of a absorption.");
        lore.add(ChatColor.GREEN + "Gain three XP levels.");
        lore.add(ChatColor.DARK_AQUA + "Regain all hunger.");
        lore.add(ChatColor.BLUE + "Regain all saturation.");
        lore.add(ChatColor.DARK_PURPLE + "Gain strength I for 5 seconds.");
        lore.add(ChatColor.WHITE + "Gain speed III for 5 seconds.");
        meta.setLore(lore);
        meta.addEnchant(Enchantment.SILK_TOUCH, 32767, true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        berry.setItemMeta(meta);

        return berry;
    }

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

    public void createInv() {

        Inv = Bukkit.createInventory(null, 36, ChatColor.RED + "Scenarios");

        ItemStack nomore = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta nomoreItemMeta = nomore.getItemMeta();
        nomoreItemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Nothing");
        List<String> nomoreLore = new ArrayList<String>();
        nomoreLore.add(ChatColor.DARK_PURPLE + "Select an item to trigger a scenario!");
        nomoreItemMeta.setLore(nomoreLore);
        nomore.setItemMeta(nomoreItemMeta);
        Inv.setItem(12, nomore);
        Inv.setItem(13, nomore);
        Inv.setItem(14, nomore);
        Inv.setItem(15, nomore);
        Inv.setItem(16, nomore);
        Inv.setItem(17, nomore);
        Inv.setItem(24, nomore);
        Inv.setItem(25, nomore);
        Inv.setItem(26, nomore);
        Inv.setItem(32, nomore);
        Inv.setItem(33, nomore);
        Inv.setItem(34, nomore);
        Inv.setItem(35, nomore);

        ItemStack ankle = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta ankleItemMeta = ankle.getItemMeta();
        ankleItemMeta.setDisplayName(ChatColor.DARK_BLUE + "Ankle Monitor");
        List<String> ankleLore = new ArrayList<String>();
        ankleLore.add(ChatColor.BLUE + "Sets everyone's boots to an Ankle Monitor");
        ankleLore.add(ChatColor.BLUE + "which gives them permanent slowness 2 and resistance 1.");
        ankleItemMeta.setLore(ankleLore);
        ankleItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        ankle.setItemMeta(ankleItemMeta);
        Inv.setItem(31, ankle);

        ItemStack fake = new ItemStack(Material.ELYTRA);
        ItemMeta fakeItemMeta = fake.getItemMeta();
        fakeItemMeta.setDisplayName(ChatColor.DARK_BLUE + "Fake Fly");
        List<String> fakeLore = new ArrayList<String>();
        fakeLore.add(ChatColor.BLUE + "Teleports everyone to y=300 gives them an elytra and 5 rockets.");
        fakeLore.add(ChatColor.BLUE + "However, the elytra gets removed after 10 seconds.");
        fakeItemMeta.setLore(fakeLore);
        fakeItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        fake.setItemMeta(fakeItemMeta);
        Inv.setItem(30, fake);

        ItemStack no = new ItemStack(Material.ENDER_PEARL);
        ItemMeta noItemMeta = no.getItemMeta();
        noItemMeta.setDisplayName(ChatColor.DARK_BLUE + "No Moving");
        List<String> noLore = new ArrayList<String>();
        noLore.add(ChatColor.BLUE + "Gives everyone 64 ender pearls but also removes their ability to walk/jump");
        noItemMeta.setLore(noLore);
        noItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        no.setItemMeta(noItemMeta);
        Inv.setItem(29, no);

        ItemStack dog = new ItemStack(Material.BONE);
        ItemMeta dogItemMeta = dog.getItemMeta();
        dogItemMeta.setDisplayName(ChatColor.DARK_BLUE + "Dog");
        List<String> dogLore = new ArrayList<String>();
        dogLore.add(ChatColor.BLUE + "Spawns a cute doggo on everyone.");
        dogItemMeta.setLore(dogLore);
        dogItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        dog.setItemMeta(dogItemMeta);
        Inv.setItem(28, dog);

        ItemStack special = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta specialMeta = special.getItemMeta();
        specialMeta.setDisplayName(ChatColor.BLUE + "Special Scenarios");
        specialMeta.addEnchant(Enchantment.SILK_TOUCH,1,true);
        specialMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        special.setItemMeta(specialMeta);
        Inv.setItem(27, special);

        ItemStack invc = new ItemStack(Material.IRON_BLOCK);
        ItemMeta invcItemMeta = invc.getItemMeta();
        invcItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Invincibility");
        List<String> invcLore = new ArrayList<String>();
        invcLore.add(ChatColor.GREEN + "Makes everyone immune to damage for a minute.");
        invcItemMeta.setLore(invcLore);
        invcItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        invc.setItemMeta(invcItemMeta);
        Inv.setItem(23, invc);

        ItemStack fire = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta fireItemMeta = fire.getItemMeta();
        fireItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Fire Resistance");
        List<String> fireLore = new ArrayList<String>();
        fireLore.add(ChatColor.GREEN + "Gives everyone fire resistance for a minute.");
        fireItemMeta.setLore(fireLore);
        fireItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        fire.setItemMeta(fireItemMeta);
        Inv.setItem(22, fire);

        ItemStack efficiency = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta efficiencyItemMeta = efficiency.getItemMeta();
        efficiencyItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Efficiency");
        List<String> efficiencyLore = new ArrayList<String>();
        efficiencyLore.add(ChatColor.GREEN + "Enchants everyone's current item in hand with efficiency 1.");
        efficiencyItemMeta.setLore(efficiencyLore);
        efficiencyItemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        efficiencyItemMeta.addEnchant(Enchantment.DIG_SPEED,1,true);
        efficiency.setItemMeta(efficiencyItemMeta);
        Inv.setItem(21, efficiency);

        ItemStack maniac = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta maniacItemMeta = maniac.getItemMeta();
        maniacItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Maniac Miner");
        List<String> maniacLore = new ArrayList<String>();
        maniacLore.add(ChatColor.GREEN + "Gives everyone haste 3 and speed 1 for a minute.");
        maniacItemMeta.setLore(maniacLore);
        maniacItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        maniac.setItemMeta(maniacItemMeta);
        Inv.setItem(20, maniac);

        ItemStack gap = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta gapItemMeta = gap.getItemMeta();
        gapItemMeta.setDisplayName(ChatColor.DARK_GREEN + "Gapple");
        List<String> gapLore = new ArrayList<String>();
        gapLore.add(ChatColor.GREEN + "Gives everyone the effects of a gapple.");
        gapItemMeta.setLore(gapLore);
        gapItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        gap.setItemMeta(gapItemMeta);
        Inv.setItem(19, gap);

        ItemStack good = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta goodMeta = good.getItemMeta();
        goodMeta.setDisplayName(ChatColor.GREEN + "Good Scenarios");
        goodMeta.addEnchant(Enchantment.SILK_TOUCH,1,true);
        goodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        good.setItemMeta(goodMeta);
        Inv.setItem(18, good);

        ItemStack typing = new ItemStack(Material.PAPER);
        ItemMeta typingItemMeta = typing.getItemMeta();
        typingItemMeta.setDisplayName(ChatColor.DARK_RED + "Typing Test");
        List<String> typingLore = new ArrayList<String>();
        typingLore.add(ChatColor.RED + "Gives everyone 10 seconds to type random phrase or they die.");
        typingItemMeta.setLore(typingLore);
        typingItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        typing.setItemMeta(typingItemMeta);
        Inv.setItem(11, typing);

        ItemStack damage = new ItemStack(Material.SPLASH_POTION);
        ItemMeta damageItemMeta = damage.getItemMeta();
        damageItemMeta.setDisplayName(ChatColor.DARK_RED + "Instant Damage");
        List<String> damageLore = new ArrayList<String>();
        damageLore.add(ChatColor.RED + "Gives everyone instant damage 2.");
        damageItemMeta.setLore(damageLore);
        damageItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        PotionMeta meta = ((PotionMeta) damageItemMeta);
        meta.setColor(Color.BLACK);
        meta.addCustomEffect(new PotionEffect(PotionEffectType.HARM, 0, 1), true);
        damage.setItemMeta(meta);
        Inv.setItem(10, damage);

        ItemStack silverfish = new ItemStack(Material.SILVERFISH_SPAWN_EGG);
        ItemMeta silverfishItemMeta = silverfish.getItemMeta();
        silverfishItemMeta.setDisplayName(ChatColor.DARK_RED + "Silverfish");
        List<String> silverfishLore = new ArrayList<String>();
        silverfishLore.add(ChatColor.RED + "Summons 10 Silverfish on every player.");
        silverfishItemMeta.setLore(silverfishLore);
        silverfishItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        silverfish.setItemMeta(silverfishItemMeta);
        Inv.setItem(8, silverfish);

        ItemStack roulette = new ItemStack(Material.GOLD_INGOT);
        ItemMeta rouletteItemMeta = roulette.getItemMeta();
        rouletteItemMeta.setDisplayName(ChatColor.DARK_RED + "Roulette");
        List<String> rouletteLore = new ArrayList<String>();
        rouletteLore.add(ChatColor.RED + "Random player instantly dies.");
        rouletteItemMeta.setLore(rouletteLore);
        rouletteItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        roulette.setItemMeta(rouletteItemMeta);
        Inv.setItem(7, roulette);

        ItemStack clear = new ItemStack(Material.TRAPPED_CHEST);
        ItemMeta clearItemMeta = clear.getItemMeta();
        clearItemMeta.setDisplayName(ChatColor.DARK_RED + "Clear Inventory");
        List<String> clearLore = new ArrayList<String>();
        clearLore.add(ChatColor.RED + "Clears everyone's inventory.");
        clearItemMeta.setLore(clearLore);
        clearItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        clear.setItemMeta(clearItemMeta);
        Inv.setItem(6, clear);

        ItemStack mining = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta miningItemMeta = mining.getItemMeta();
        miningItemMeta.setDisplayName(ChatColor.DARK_RED + "Mining Fatigue");
        List<String> miningLore = new ArrayList<String>();
        miningLore.add(ChatColor.RED + "Gives everyone mining fatigue for a minute.");
        miningItemMeta.setLore(miningLore);
        miningItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        mining.setItemMeta(miningItemMeta);
        Inv.setItem(5, mining);

        ItemStack wither = new ItemStack(Material.COAL);
        ItemMeta witherItemMeta = wither.getItemMeta();
        witherItemMeta.setDisplayName(ChatColor.DARK_RED + "Wither");
        List<String> witherLore = new ArrayList<String>();
        witherLore.add(ChatColor.RED + "Gives everyone wither 3 for 10 seconds.");
        witherItemMeta.setLore(witherLore);
        witherItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        wither.setItemMeta(witherItemMeta);
        Inv.setItem(4, wither);

        ItemStack hunger = new ItemStack(Material.ROTTEN_FLESH);
        ItemMeta hungerItemMeta = hunger.getItemMeta();
        hungerItemMeta.setDisplayName(ChatColor.DARK_RED + "Hunger");
        List<String> hungerLore = new ArrayList<String>();
        hungerLore.add(ChatColor.RED + "Gives everyone hunger 10 for a minute.");
        hungerItemMeta.setLore(hungerLore);
        hungerItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        hunger.setItemMeta(hungerItemMeta);
        Inv.setItem(3, hunger);

        ItemStack blind = new ItemStack(Material.POTION);
        ItemMeta blindItemMeta = blind.getItemMeta();
        blindItemMeta.setDisplayName(ChatColor.DARK_RED + "Blindness");
        List<String> blindLore = new ArrayList<String>();
        blindLore.add(ChatColor.RED + "Gives everyone blindness for a minute.");
        blindItemMeta.setLore(blindLore);
        blindItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        PotionMeta potionMeta = ((PotionMeta) blindItemMeta);
        potionMeta.addCustomEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 2), true);
        potionMeta.setColor(Color.GRAY);
        blind.setItemMeta(potionMeta);
        Inv.setItem(2, blind);

        ItemStack tnt = new ItemStack(Material.TNT);
        ItemMeta tntItemMeta = tnt.getItemMeta();
        tntItemMeta.setDisplayName(ChatColor.DARK_RED + "TNT");
        List<String> tntLore = new ArrayList<String>();
        tntLore.add(ChatColor.RED + "Summon tnt on all players.");
        tntItemMeta.setLore(tntLore);
        tntItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        tnt.setItemMeta(tntItemMeta);
        Inv.setItem(1, tnt);

        ItemStack bad = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta badMeta = bad.getItemMeta();
        badMeta.setDisplayName(ChatColor.RED + "Bad Scenarios");
        badMeta.addEnchant(Enchantment.SILK_TOUCH,1,true);
        badMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);
        bad.setItemMeta(badMeta);
        Inv.setItem(0, bad);
        Inv.setItem(9, bad);
    }

}

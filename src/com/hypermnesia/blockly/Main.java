package com.hypermnesia.blockly;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin implements Listener {

    FileConfiguration config = this.getConfig();
    public boolean gameIsRunning = false;
    public String targetBlock = "diamond_ore";

    int randomWithRange(int min, int max) {

        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;

    }

    public void executeScenario(String scenario) {
        if (scenario.equalsIgnoreCase("tnt")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                World world = Bukkit.getServer().getWorld("world");
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "TNT", ChatColor.RED + "TNT spawns on your feet.", 10, 20, 10);
                world.spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "TNT: " + ChatColor.RED + "TNT spawns on your feet.");
        }
        if (scenario.equalsIgnoreCase("blindness")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Blindness", ChatColor.RED + "Blindness for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 2));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Blindness: " + ChatColor.RED + "Blindness for 1 minute.");

        }
        if (scenario.equalsIgnoreCase("hunger")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Hunger", ChatColor.RED + "Hunger 10 for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 9));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Hunger: " + ChatColor.RED + "Hunger 10 for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("wither")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Wither", ChatColor.RED + "Wither for 10 seconds.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Wither: " + ChatColor.RED + "Wither for 10 seconds.");
        }
        if (scenario.equalsIgnoreCase("miningfatigue")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Mining Fatigue", ChatColor.RED + "Mining Fatigue for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 0));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Mining Fatigue: " + ChatColor.RED + "Mining fatigue for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("clearinv")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Clear Inventory", ChatColor.RED + "Inventory Cleared.", 10, 20, 10);
                player.getInventory().clear();
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Clear Inventory: " + ChatColor.RED + "Inventory Cleared.");
        }
        if (scenario.equalsIgnoreCase("roulette")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Roulette", ChatColor.RED + "Random player instantly dies.", 10, 20, 10);
            }
            Player randomplayer = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
            randomplayer.setHealth(0);
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Roulette: " + ChatColor.RED + "Random player instantly dies.");
        }
        if (scenario.equalsIgnoreCase("silverfish")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Silverfish", ChatColor.RED + "Spawns 10 silverfish at your feet.", 10, 20, 10);
                for (int x = 0; x < 11; x++) {
                    World world = Bukkit.getServer().getWorld("world");
                    world.spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Silverfish: " + ChatColor.RED + "Spawns 10 silverfish at your feet..");

        }
        if (scenario.equalsIgnoreCase("instantdamage")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "Instant Damage", ChatColor.RED + "Receive Instant Damage 2.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "Instant Damage: " + ChatColor.RED + "Receive Instant Damage 2.");
        }
        if (scenario.equalsIgnoreCase("mlg")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Location mlg = new Location(Bukkit.getWorld("world"), player.getLocation().getX(), 250, player.getLocation().getZ(), 0, 0);
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_RED + "MLG", ChatColor.RED + "Get teleported to Y=250.", 10, 20, 10);
                player.teleport(mlg);
            }
            Bukkit.broadcastMessage(ChatColor.DARK_RED + "MLG: " + ChatColor.RED + "Get teleported to Y=250.");
        }
        if (scenario.equalsIgnoreCase("gapple")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_GREEN + "Gapple", ChatColor.GREEN + "Receive the effects of a golden apple.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Gapple: " + ChatColor.GREEN + "Receive the effects of a golden apple.");
        }
        if (scenario.equalsIgnoreCase("maniacminer")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_GREEN + "Maniac Miner", ChatColor.GREEN + "Haste 3 and Speed 1 for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 2));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Manic Miner: " + ChatColor.GREEN + "Haste 3 and speed 1 for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("efficiency")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_GREEN + "Efficiency", ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.", 10, 20, 10);
                player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Efficiency: " + ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.");
        }
        if (scenario.equalsIgnoreCase("fireres")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_GREEN + "Fire Resistance", ChatColor.GREEN + "Fire Resistance for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Fire Resistance: " + ChatColor.GREEN + "Fire Resistance for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("invincibility")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_GREEN + "Invincibility", ChatColor.GREEN + "Immune to damage for 1 minute.", 10, 20, 10);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 4));
            }
            Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Invincibility: " + ChatColor.GREEN + "Immune to damage for 1 minute.");
        }
        if (scenario.equalsIgnoreCase("dog")) {
            Material itemType = Material.BONE;
            ItemStack itemStack = new ItemStack(itemType);
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
                player.sendTitle(ChatColor.DARK_BLUE + "Doggo", ChatColor.BLUE + "Spawns a cute doggo on your feet and gives you 3 bones. Aww.", 10, 20, 10);
                World world = Bukkit.getServer().getWorld("world");
                world.spawnEntity(player.getLocation(), EntityType.WOLF);
                for (int x = 0; x < 3; x++) {
                    if (player.getInventory().firstEmpty() == -1) {
                        Location loc = player.getLocation();
                        world.dropItemNaturally(loc, itemStack);
                    }
                    player.getInventory().addItem(itemStack);
                }
            }
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "Doggo: " + ChatColor.BLUE + "Spawns a cute doggo on your feet and gives you 3 bones. Aww.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }
        if (scenario.equalsIgnoreCase("nomoving")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0F, 1.0F);
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
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "No Moving: " + ChatColor.BLUE + "Moving is cringe anyway, Me and the boys use enderpearls.");
            Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "This is a special scenario! This can only be triggered via the host with a command.");
        }
    }


    @Override
    public void onEnable() {
        System.out.println("[Blockly] server here again :D");
        config.addDefault("extra-pain-mode", false);
        config.options().copyDefaults(true);
        saveConfig();
        this.getServer().getPluginManager().registerEvents(this, this);

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
                if (config.getBoolean("extra-pain-mode")) {
                    Bukkit.getServer().broadcastMessage(ChatColor.RED + "Friendly Reminder that " + ChatColor.DARK_RED + "Extra Pain Mode " + ChatColor.RED + "is currently active. ");
                    Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Extra Pain Mode: Take twice as much damage whenever you are damaged.");
                } else {
                    return;
                }

            }
        }, 4800, 4800);
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
                        List<String> list = new ArrayList<String>();
                        list.add("TNT");
                        list.add("BLINDNESS");
                        list.add("HUNGER");
                        list.add("WITHER");
                        list.add("MININGFATIGUE");
                        list.add("CLEARINV");
                        list.add("ROULETTE");
                        list.add("SILVERFISH");
                        list.add("INSTANTDAMAGE");
                        list.add("MLG");
                        list.add("GAPPLE");
                        list.add("MANIACMINER");
                        list.add("EFFICIENCY");
                        list.add("FIRERES");
                        list.add("INVINCIBILITY");
                        int randomX = this.randomWithRange(100, 1000);
                        int randomY = this.randomWithRange(100, 1000);
                        Location loc = new Location(Bukkit.getWorld("world"), randomX, 150, randomY, 0, 0);
                        BukkitTask task = new BukkitRunnable() {
                            int time = 10;

                            @Override
                            public void run() {
                                if (this.time == 0) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.teleport(loc);
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 400, 2));
                                        player.setHealth(20);
                                        player.setSaturation(20);
                                        player.setFoodLevel(20);
                                        player.setGameMode(GameMode.SURVIVAL);
                                    }
                                    cancel();
                                }

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0F, 1.0F);
                                    player.sendTitle(ChatColor.RED + "Teleporting in " + time, ChatColor.GREEN + "Made by hippo (Hypermnesia)", 10, 20, 10);
                                }
                                this.time--;
                            }
                        }.runTaskTimer(this, 0L, 20L);

                        new BukkitRunnable() {
                            public void run() {
                                World world = Bukkit.getServer().getWorld("world");
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                                    player.sendTitle(ChatColor.GOLD + "Event Started!", ChatColor.AQUA + "First one to mine diamonds wins! Good Luck!", 10, 20, 10);
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "Event Started! " + ChatColor.AQUA + "First one to mine diamonds wins! Good Luck!");
                                    world.setPVP(true);
                                    world.setSpawnLocation(loc);
                                }
                            }
                        }.runTaskLater(this, 260);

                        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
                            public void run() {
                                int index = (int) (Math.random() * list.size());
                                if (!gameIsRunning) {
                                    Bukkit.getServer().getScheduler().cancelTask(1);
                                }
                                if (list.get(index).equalsIgnoreCase("tnt")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        World world = Bukkit.getServer().getWorld("world");
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "TNT", ChatColor.RED + "TNT spawns on your feet.", 10, 20, 10);
                                        world.spawnEntity(player.getLocation(), EntityType.PRIMED_TNT);
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "TNT: " + ChatColor.RED + "TNT spawns on your feet.");
                                }
                                if (list.get(index).equalsIgnoreCase("blindness")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Blindness", ChatColor.RED + "Blindness for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 2));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Blindness: " + ChatColor.RED + "Blindness for 1 minute.");

                                }
                                if (list.get(index).equalsIgnoreCase("hunger")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Hunger", ChatColor.RED + "Hunger 10 for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 1200, 9));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Hunger: " + ChatColor.RED + "Hunger 10 for 1 minute.");
                                }
                                if (list.get(index).equalsIgnoreCase("wither")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Wither", ChatColor.RED + "Wither for 10 seconds.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 200, 0));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Wither: " + ChatColor.RED + "Wither for 10 seconds.");
                                }
                                if (list.get(index).equalsIgnoreCase("miningfatigue")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Mining Fatigue", ChatColor.RED + "Mining Fatigue for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 1200, 0));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Mining Fatigue: " + ChatColor.RED + "Mining fatigue for 1 minute.");
                                }
                                if (list.get(index).equalsIgnoreCase("clearinv")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Clear Inventory", ChatColor.RED + "Inventory Cleared.", 10, 20, 10);
                                        player.getInventory().clear();
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Clear Inventory: " + ChatColor.RED + "Inventory Cleared.");
                                }
                                if (list.get(index).equalsIgnoreCase("roulette")) {
                                    try {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                            player.sendTitle(ChatColor.RED + "Roulette", ChatColor.RED + "Random player instantly dies.", 10, 20, 10);
                                        }
                                        Player randomplayer = Bukkit.getOnlinePlayers().stream().skip((int) (Bukkit.getOnlinePlayers().size() * Math.random())).findFirst().orElse(null);
                                        randomplayer.setHealth(0);
                                        Bukkit.broadcastMessage(ChatColor.DARK_RED + "Roulette: " + ChatColor.RED + "Random player instantly dies.");
                                    } catch (NullPointerException e) {
                                        Bukkit.broadcastMessage(ChatColor.RED + "You guys got lucky. The random player was already dead.");
                                    }
                                }
                                if (list.get(index).equalsIgnoreCase("silverfish")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Silverfish", ChatColor.RED + "Spawns 10 silverfish at your feet.", 10, 20, 10);
                                        for (int x = 0; x < 11; x++) {
                                            World world = Bukkit.getServer().getWorld("world");
                                            world.spawnEntity(player.getLocation(), EntityType.SILVERFISH);
                                        }
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Silverfish: " + ChatColor.RED + "Spawns 10 silverfish at your feet..");

                                }
                                if (list.get(index).equalsIgnoreCase("instantdamage")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "Instant Damage", ChatColor.RED + "Receive Instant Damage 2.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "Instant Damage: " + ChatColor.RED + "Receive Instant Damage 2.");
                                }
                                if (list.get(index).equalsIgnoreCase("mlg")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Location mlg = new Location(Bukkit.getWorld("world"), player.getLocation().getX(), 250, player.getLocation().getZ(), 0, 0);
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.BLOCK_ANVIL_DESTROY, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.RED + "MLG", ChatColor.RED + "Get teleported to Y=250.", 10, 20, 10);
                                        player.teleport(mlg);
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_RED + "MLG: " + ChatColor.RED + "Get teleported to Y=250.");
                                }
                                if (list.get(index).equalsIgnoreCase("gapple")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.GREEN + "Gapple", ChatColor.GREEN + "Receive the effects of a golden apple.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Gapple: " + ChatColor.GREEN + "Receive the effects of a golden apple.");
                                }
                                if (list.get(index).equalsIgnoreCase("maniacminer")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.GREEN + "Maniac Miner", ChatColor.GREEN + "Haste 3 and Speed 1 for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 1200, 2));
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1200, 0));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Maniac Miner: " + ChatColor.GREEN + "Haste 3 and speed 1 for 1 minute.");
                                }
                                if (list.get(index).equalsIgnoreCase("efficiency")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.GREEN + "Efficiency", ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.", 10, 20, 10);
                                        player.getInventory().getItemInMainHand().addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Efficiency: " + ChatColor.GREEN + "Your current item held gets enchanted with efficiency 1.");
                                }
                                if (list.get(index).equalsIgnoreCase("fireres")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.GREEN + "Fire Resistance", ChatColor.GREEN + "Fire Resistance for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Fire Resistance: " + ChatColor.GREEN + "Fire Resistance for 1 minute.");
                                }
                                if (list.get(index).equalsIgnoreCase("invincibility")) {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                                        player.sendTitle(ChatColor.GREEN + "Invincibility", ChatColor.GREEN + "Immune to damage for 1 minute.", 10, 20, 10);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1200, 4));
                                    }
                                    Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "Invincibility: " + ChatColor.GREEN + "Immune to damage for 1 minute.");
                                }
                            }


                        }, 2400, 2400);
                    }
                    if (args[0].equalsIgnoreCase("end")) {
                        gameIsRunning = false;
                        World world = Bukkit.getWorld("world");
                        player.sendMessage(ChatColor.RED + "Challenge ended.");
                        for (Player playerEnd : Bukkit.getOnlinePlayers()) {
                            playerEnd.sendTitle(ChatColor.LIGHT_PURPLE + "Challenge Ended", ChatColor.LIGHT_PURPLE + "Host ended challenge prematurely.", 10, 20, 10);
                            Location lobby = new Location(Bukkit.getWorld("world"), 0, 240, 0, 0, 0);
                            playerEnd.teleport(lobby);
                            world.setSpawnLocation(lobby);
                            world.setPVP(false);
                            Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0F, 1.0F);
                            playerEnd.getInventory().clear();
                            playerEnd.setGameMode(GameMode.ADVENTURE);
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
                        this.executeScenario(args[0]);
                        if (args[0].equalsIgnoreCase("list")) {
                            player.sendMessage(ChatColor.GOLD + "Available list of scenarios: \n" + ChatColor.RED + "Bad scenarios: \nTNT,BLINDNESS,HUNGER,WITHER,MININGFATIGUE,CLEARINV,ROULETTE,SILVERFISH,INSTANTDAMAGE,MLG.\n" + ChatColor.GREEN + "Good scenarios: \nGAPPLE,MANIACMINER,EFFICIENCY,FIRERES,INVINCIBILITY. \n" + ChatColor.BLUE + "Special scenarios: \n DOG,NOMOVING");
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
                        World world = Bukkit.getServer().getWorld("world");
                        world.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0F, 1.0F);
                        player.sendMessage(ChatColor.RED + "Inventory Full! Free up some space and do the command again.");
                        return false;
                    }
                    player.getInventory().addItem(StickOfBan());
                    World world = Bukkit.getServer().getWorld("world");
                    world.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_SPAWN, 1.0F, 1.0F);
                    player.sendMessage(ChatColor.GOLD + "Be careful with that stick! It's real" + ChatColor.RED + " dangerous");
                    return true;
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
                        World world = Bukkit.getServer().getWorld("world");
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
                }
            }
        } catch (Exception e) {
        e.printStackTrace();
    }
        return false;
}


    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        if (event.getBlock().getType().equals(Material.matchMaterial(targetBlock))) {
            gameIsRunning = false;
            for (Player player : Bukkit.getOnlinePlayers()) {
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getDisplayName() + ChatColor.GREEN + " has found the target block! " + ChatColor.AQUA + "(" + Material.matchMaterial(targetBlock) + ")");
                Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
                player.sendTitle(ChatColor.GOLD + player.getDisplayName() + ChatColor.GREEN + " has found the target block!", ChatColor.RED + "Please Wait for the host.", 10, 20, 10);
                player.setGameMode(GameMode.SPECTATOR);
                event.getPlayer().setGameMode(GameMode.ADVENTURE);
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 255));
                player.getInventory().clear();
                new BukkitRunnable() {
                    public void run() {
                        Location loc = new Location(Bukkit.getWorld("world"), -0 ,76, 0, 0, 0);
                        player.teleport(loc);
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                    }
                }.runTaskLater(this, 60);

                BukkitTask task = new BukkitRunnable() {
                    int time = 10;
                    @Override
                    public void run() {
                        if (this.time == 0) {
                            cancel();
                        }

                        for (int time = 10; time < 10; this.time--) {
                            executeFireworkBarrage();
                        }
                    }
                }.runTaskTimer(this, 0L, 20L);

                new BukkitRunnable() {
                    public void run() {
                        gameIsRunning = false;
                        World world = Bukkit.getWorld("world");
                        Location lobby = new Location(Bukkit.getWorld("world"), 0, 241, 0, 0, 0);
                        player.teleport(lobby);
                        world.setSpawnLocation(lobby);
                        world.setPVP(false);
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                }.runTaskLater(this, 200);
            }
        }

    }
    @EventHandler
    public void onPlayerDamage (EntityDamageByEntityEvent event) {
        try {
            Player player = (Player) event.getEntity();
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                World world = Bukkit.getWorld("world");
                if (((Player) event.getDamager()).getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.GOLD + "" + ChatColor.BOLD + "Stick of Ban")) {
                    if (event.getDamager().isOp()) {
                        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), ChatColor.RED + "You were given a third degree burn by " + ChatColor.GOLD + "The Stick of Ban" + ChatColor.RED + ".", null, null);
                        player.kickPlayer(ChatColor.RED + "You were given a third degree burn by " + ChatColor.GOLD + "The Stick of Ban" + ChatColor.RED + ".");
                        world.spawnEntity(player.getLocation(), EntityType.LIGHTNING);
                    } else {
                        Bukkit.getServer().getWorlds().get(0).playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1.0F, 1.0F);
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
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "" + ChatColor.BOLD + "Moving Pearls") || player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().contains(ChatColor.BLUE + "" + ChatColor.BOLD + "Moving Pearls")) {
                    if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                        event.setCancelled(true);
                    }
            }
        } catch (ClassCastException | NullPointerException e) {
        }
    }

    @EventHandler
    public void extraPainMode (EntityDamageEvent event) {
        try {
            Player player = (Player) event.getEntity();
            if (config.getBoolean("extra-pain-mode")) {
                if (!(event.getEntity() instanceof Player))
                    return;
                double dmg = event.getDamage();
                event.setDamage(dmg * 2);
            } else {
                return;
            }
        } catch (ClassCastException | NullPointerException e) {
        }
    }

        public void executeFireworkBarrage() {
        new BukkitRunnable() {
            public void run() {
                World world = Bukkit.getWorld("world");
                Location f1 = new Location(Bukkit.getWorld("world"), -1, 98, 1, 0, 0);
                Location f2 = new Location(Bukkit.getWorld("world"), -1, 98, -1, 0, 0);
                Location f3 = new Location(Bukkit.getWorld("world"), 1, 98, -1, 0, 0);
                Location f4 = new Location(Bukkit.getWorld("world"), 1, 98, 1, 0, 0);
                world.spawnEntity(f1, EntityType.FIREWORK);
                world.spawnEntity(f2, EntityType.FIREWORK);
                world.spawnEntity(f3, EntityType.FIREWORK);
                world.spawnEntity(f4, EntityType.FIREWORK);
            }
        }.runTaskLater(this, 20);
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




}
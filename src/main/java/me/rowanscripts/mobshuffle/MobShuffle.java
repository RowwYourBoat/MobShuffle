package me.rowanscripts.mobshuffle;

import me.rowanscripts.mobshuffle.commands.AutoComplete;
import me.rowanscripts.mobshuffle.commands.ForceStop;
import me.rowanscripts.mobshuffle.commands.SettingsCommand;
import me.rowanscripts.mobshuffle.commands.Start;
import me.rowanscripts.mobshuffle.data.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class MobShuffle extends JavaPlugin {

    public static MobShuffle plugin;
    public static Map<UUID, String> assignments;
    public static BukkitScheduler bukkitScheduler;

    public static boolean gameInProgress;

    public static void assignRandomMob(Player player) {
        UUID playerUUID = player.getUniqueId();
        Random random = new Random();

        List<String> mobList = (List<String>) Settings.settingsYaml.get("mobList");
        int randomNumber = random.nextInt(mobList.size() - 1) + 1;
        String randomMobString = mobList.get(randomNumber);
        String[] splitRandomMobString = randomMobString.split(" : ");

        String entityName = splitRandomMobString[0];
        int minReqKills = Integer.parseInt(splitRandomMobString[1]);
        int maxReqKills = Integer.parseInt(splitRandomMobString[2]);

        int reqKills = random.nextInt(maxReqKills - minReqKills) + minReqKills;
        assignments.put(playerUUID, entityName + " : " + reqKills);
        player.sendMessage(ChatColor.DARK_GREEN + "You will have to kill " + ChatColor.GREEN + ChatColor.BOLD + reqKills + ChatColor.RESET + ChatColor.DARK_GREEN + " entities of the type " + ChatColor.GREEN + ChatColor.BOLD + entityName + "!");
    }

    @Override
    public void onEnable() {
        plugin = this;
        assignments = new HashMap<>();
        bukkitScheduler = Bukkit.getScheduler();

        gameInProgress = false;

        Bukkit.getPluginCommand("start").setExecutor(new Start());

        Bukkit.getPluginCommand("forcestop").setExecutor(new ForceStop());

        Bukkit.getPluginCommand("settings").setExecutor(new SettingsCommand());
        Bukkit.getPluginCommand("settings").setTabCompleter(new AutoComplete());

        Settings.loadDefaultSettings();
    }

    public static void newRound() {
        plugin.getLogger().info("newRound");
    }

    public static void endGame() {
        MobShuffle.gameInProgress = false;
    }

}
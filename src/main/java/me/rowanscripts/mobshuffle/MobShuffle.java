package me.rowanscripts.mobshuffle;

import jdk.internal.foreign.ArenaAllocator;
import me.rowanscripts.mobshuffle.commands.AutoComplete;
import me.rowanscripts.mobshuffle.commands.ForceStop;
import me.rowanscripts.mobshuffle.commands.SettingsCommand;
import me.rowanscripts.mobshuffle.commands.Start;
import me.rowanscripts.mobshuffle.data.Settings;
import me.rowanscripts.mobshuffle.listeners.RegisterMobKills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.security.Key;
import java.util.*;

public final class MobShuffle extends JavaPlugin {

    public static MobShuffle plugin;
    public static Map<UUID, String> assignments;
    public static List<UUID> playersInGame;
    public static BukkitScheduler bukkitScheduler;

    public static boolean gameInProgress;


    @Override
    public void onEnable() {
        plugin = this;
        assignments = new HashMap<>();
        playersInGame = new ArrayList<>();

        bukkitScheduler = Bukkit.getScheduler();

        gameInProgress = false;

        Bukkit.getPluginCommand("start").setExecutor(new Start());

        Bukkit.getPluginCommand("forcestop").setExecutor(new ForceStop());

        Bukkit.getPluginCommand("settings").setExecutor(new SettingsCommand());
        Bukkit.getPluginCommand("settings").setTabCompleter(new AutoComplete());

        Bukkit.getPluginManager().registerEvents(new RegisterMobKills(), MobShuffle.plugin);

        Settings.loadDefaultSettings();
    }

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
        int minMobsToKill = Settings.settingsYaml.getInt("minMobsToKill");
        int maxMobsToKill = Settings.settingsYaml.getInt("maxMobsToKill");
        if (reqKills < minMobsToKill)
            reqKills = minMobsToKill;
        else if (reqKills > maxMobsToKill)
            reqKills = maxMobsToKill;

        assignments.put(playerUUID, entityName + " : " + reqKills);
        player.sendMessage(ChatColor.DARK_GREEN + "You will have to kill " + ChatColor.GREEN + ChatColor.BOLD + reqKills + ChatColor.RESET + ChatColor.DARK_GREEN + " entities of the type " + ChatColor.GREEN + ChatColor.BOLD + entityName + "!");
        if (Settings.settingsYaml.getBoolean("soundEffects"))
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
    }

    public static void playerHasCompletedAssignment(Player player) {
        assignments.remove(player.getUniqueId());
        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + player.getName() + " has completed their assignment!");
        if (assignments.size() == 0)
            newRound(false);
    }

    public static void newRound(boolean timeIsUp) {
        if (timeIsUp) {
            bukkitScheduler.cancelTasks(MobShuffle.plugin);
            Set<UUID> failedToCompleteAssignmentSet = assignments.keySet();
            StringBuilder failedBroadcastString = new StringBuilder();
            failedBroadcastString.append(ChatColor.DARK_GREEN + "The following players have failed to complete their assignment:");
            for (UUID failedUUID : failedToCompleteAssignmentSet){
                playersInGame.remove(failedUUID);
                failedBroadcastString.append("\n" + ChatColor.GREEN + Bukkit.getOfflinePlayer(failedUUID).getName());
            }
            Bukkit.broadcastMessage(failedBroadcastString.toString());

            if (playersInGame.size() == 0) {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + ChatColor.BOLD.toString() + "Everyone has been eliminated!");
                if (Settings.settingsYaml.getBoolean("soundEffects"))
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);

                endGame();
            } else if (playersInGame.size() == 1) {
                Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + Bukkit.getOfflinePlayer(playersInGame.get(0)).getName() + ChatColor.RESET + ChatColor.DARK_GREEN + ChatColor.BOLD + " has won the game!");
                if (Settings.settingsYaml.getBoolean("soundEffects"))
                    for (Player player : Bukkit.getOnlinePlayers())
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);

                endGame();
            } else
                newRound(false);

        } else {
            bukkitScheduler.cancelTasks(MobShuffle.plugin);
            int roundDurationInSeconds = Settings.settingsYaml.getInt("roundDurationInSeconds");
            for (Player player : Bukkit.getOnlinePlayers()) {
                MobShuffle.assignRandomMob(player);
                RepeatingTask.createNewCountdownTask(player, roundDurationInSeconds);
            }

            MobShuffle.bukkitScheduler.runTaskLater(MobShuffle.plugin, () -> {
                MobShuffle.newRound(true);
            }, roundDurationInSeconds * 20L);
        }
    }

    public static void endGame() {
        MobShuffle.gameInProgress = false;
    }

}
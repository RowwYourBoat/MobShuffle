package me.rowanscripts.mobshuffle.commands;

import me.rowanscripts.mobshuffle.MobShuffle;
import me.rowanscripts.mobshuffle.RepeatingTask;
import me.rowanscripts.mobshuffle.data.Settings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Start implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "This is a player only command!");
            return true;
        }
        if (MobShuffle.gameInProgress){
            sender.sendMessage(ChatColor.RED + "A game is already in progress!");
            return true;
        }
        MobShuffle.gameInProgress = true;

        Player executor = (Player) sender;
        World world = executor.getWorld();
        List<Player> players = world.getPlayers();

        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + "A new game has been started!");

        int roundDurationInSeconds = Settings.settingsYaml.getInt("roundDurationInSeconds");
        for (Player player : players){
            MobShuffle.assignRandomMob(player);
            RepeatingTask.createNewCountdownTask(player, roundDurationInSeconds);
        }

        MobShuffle.bukkitScheduler.runTaskLater(MobShuffle.plugin, () -> {
            MobShuffle.newRound();
            MobShuffle.bukkitScheduler.cancelTasks(MobShuffle.plugin);
        }, roundDurationInSeconds * 20L);
        return true;
    }
}

package me.rowanscripts.mobshuffle.commands;

import me.rowanscripts.mobshuffle.data.Settings;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SettingsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("settings")) {
            if (args.length < 2)
                return false;

            if (args[0].equalsIgnoreCase("get")) {
                if (Settings.settingsYaml.isSet(args[1])) {
                    if (args[1].equalsIgnoreCase("mobList")) {
                        sender.sendMessage(ChatColor.DARK_RED + "This setting may only be changed in the settings file!\nChanges to the file may be loaded with /reloadsettings!");
                    }
                    String setValue = Settings.settingsYaml.get(args[1]).toString();
                    sender.sendMessage(ChatColor.DARK_GREEN + "The current value for " + ChatColor.GREEN + args[1] + ChatColor.DARK_GREEN + " is set to " + ChatColor.GREEN + setValue + "!");
                } else
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid setting!");
            } else if (args[0].equalsIgnoreCase("set")) {
                if (Settings.settingsYaml.isSet(args[1])) {
                    if (args[1].equalsIgnoreCase("mobList")) {
                        sender.sendMessage(ChatColor.DARK_RED + "This setting may only be changed in the settings file!\nChanges to the file may be loaded with /reloadsettings!");
                        return true;
                    }
                    if (args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("true")) {
                        boolean valueToSet = Boolean.parseBoolean(args[2]);
                        Settings.settingsYaml.set(args[1], valueToSet);
                        sender.sendMessage(ChatColor.DARK_GREEN + "The value of " + ChatColor.GREEN + args[1] + ChatColor.DARK_GREEN + " has been changed to " + ChatColor.GREEN + valueToSet + "!");
                    } else {
                        int valueToSet = -1;
                        try {
                            valueToSet = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid value!");
                            return true;
                        }
                        if (valueToSet > 0) {
                            Settings.settingsYaml.set(args[1], valueToSet);
                            sender.sendMessage(ChatColor.DARK_GREEN + "The value of " + ChatColor.GREEN + args[1] + ChatColor.DARK_GREEN + " has been changed to " + ChatColor.GREEN + valueToSet + "!");
                        } else
                            sender.sendMessage(ChatColor.DARK_RED + "Invalid value!");
                    }
                }
                Settings.saveSettingsFile();
            }
        } else if (command.getName().equalsIgnoreCase("reloadsettings")){
            try {
                Settings.settingsYaml.load(Settings.settingsFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
            sender.sendMessage(ChatColor.DARK_GREEN + "You've successfully reloaded the settings!");
        }
        return true;
    }
}

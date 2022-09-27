package me.rowanscripts.mobshuffle.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AutoComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> arguments = new ArrayList<>();
        if (command.getName().equalsIgnoreCase("settings")) {
            if (args.length == 1) {
                arguments.add("get");
                arguments.add("set");
            } else if (args.length == 2) {
                arguments.add("roundDurationInSeconds");
                arguments.add("minMobsToKill");
                arguments.add("maxMobsToKill");
                arguments.add("timeLeftCounter");
                arguments.add("soundEffects");
                arguments.add("mobList");
            }
        }
        return arguments;
    }
}

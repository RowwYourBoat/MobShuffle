package me.rowanscripts.mobshuffle;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class RepeatingTask {

    public static void createNewCountdownTask(Player player, int timeInSeconds) {
        AtomicInteger countdown = new AtomicInteger(timeInSeconds);
        MobShuffle.bukkitScheduler.scheduleSyncRepeatingTask(MobShuffle.plugin, () -> {
            countdown.getAndDecrement();
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.DARK_GREEN + ChatColor.BOLD.toString() + countdown));
        }, 0, 20);
    }

}

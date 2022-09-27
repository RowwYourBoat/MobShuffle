package me.rowanscripts.mobshuffle.listeners;

import me.rowanscripts.mobshuffle.MobShuffle;
import me.rowanscripts.mobshuffle.data.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.List;
import java.util.UUID;

public class RegisterMobKills implements Listener {

    public void registerKill(Player player, String entityName){
        UUID playerUUID = player.getUniqueId();
        if (MobShuffle.assignments.containsKey(playerUUID)){
            String[] assignment = MobShuffle.assignments.get(playerUUID).split(" : ");
            String assignedEntityName = assignment[0];
            int remainingReqKills = Integer.parseInt(assignment[1]);

            if (!assignedEntityName.equals(entityName))
                return;

            if (remainingReqKills - 1 == 0)
                MobShuffle.playerHasCompletedAssignment(player);
            else {
                remainingReqKills--;
                MobShuffle.assignments.replace(playerUUID, assignedEntityName + " : " + remainingReqKills);
                player.sendMessage(ChatColor.DARK_GREEN + "You will have to kill " + ChatColor.GREEN + ChatColor.BOLD + remainingReqKills + ChatColor.RESET + ChatColor.DARK_GREEN + " more " + ChatColor.GREEN + ChatColor.BOLD + entityName + "(S)!");
                if (Settings.settingsYaml.getBoolean("soundEffects"))
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 10, 1);
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event){
        if (!MobShuffle.gameInProgress)
            return;

        Entity entity = event.getEntity();
        List<Entity> nearbyEntities = entity.getNearbyEntities(5, 5, 5);

        for (Entity nearbyEntity : nearbyEntities){
            if (nearbyEntity.getType() == EntityType.PLAYER)
                registerKill((Player) nearbyEntity, entity.getType().name());
        }
    }

}

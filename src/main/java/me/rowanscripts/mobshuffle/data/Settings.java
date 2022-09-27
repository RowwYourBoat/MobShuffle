package me.rowanscripts.mobshuffle.data;

import me.rowanscripts.mobshuffle.MobShuffle;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Settings {

    public static File settingsFile;
    public static YamlConfiguration settingsYaml;

    public static List<String> getDefaultMobs() {
        List<String> mobList = new ArrayList<>();
        mobList.add("COW : 1 : 7");
        mobList.add("BAT : 1 : 3");
        mobList.add("SQUID : 3 : 10");
        mobList.add("BEE : 1 : 2");
        mobList.add("BLAZE : 1 : 3");
        mobList.add("CAVE_SPIDER : 1 : 5");
        mobList.add("CHICKEN : 3 : 6");
        mobList.add("COD : 5 : 10");
        mobList.add("CREEPER : 2 : 6");
        mobList.add("DOLPHIN : 1 : 2");
        mobList.add("DONKEY : 1 : 2");
        mobList.add("DROWNED : 1 : 3");
        mobList.add("ENDERMAN : 1 : 2");
        mobList.add("FOX : 1 : 2");
        mobList.add("GHAST : 1 : 2");
        mobList.add("GLOW_SQUID : 1 : 2");
        mobList.add("GOAT : 1 : 2");
        mobList.add("HOGLIN : 1 : 3");
        mobList.add("HORSE : 1 : 3");
        mobList.add("MAGMA_CUBE : 1 : 3");
        mobList.add("PIG : 2 : 5");
        mobList.add("PIGLIN : 2 : 4");
        mobList.add("SALMON : 5 : 10");
        mobList.add("SHEEP : 2 : 5");
        mobList.add("SKELETON : 3 : 7");
        mobList.add("SPIDER : 2 : 5");
        mobList.add("STRIDER : 1 : 3");
        mobList.add("TROPICAL_FISH : 2 : 10");
        mobList.add("VILLAGER : 1 : 3");
        mobList.add("WITHER_SKELETON : 1 : 5");
        mobList.add("WOLF : 1 : 3");
        mobList.add("ZOMBIE : 1 : 10");
        mobList.add("ZOMBIFIED_PIGLIN : 1 : 3");
        return mobList;
    }

    public static void loadDefaultSettings(){
        settingsFile = new File(MobShuffle.plugin.getDataFolder(), "settings.yml");
        settingsYaml = YamlConfiguration.loadConfiguration(settingsFile);
        if (!settingsFile.exists()){
            settingsYaml.set("roundDurationInSeconds", 450);
            settingsYaml.set("minMobsToKill", 1);
            settingsYaml.set("maxMobsToKill", 10);
            settingsYaml.set("timeLeftCounter", true);
            settingsYaml.set("soundEffects", true);

            settingsYaml.set("mobList", getDefaultMobs());
            List<String> comments = new ArrayList<>();
            comments.add("    ");
            comments.add("Mob List Format:");
            comments.add("NAME : minKillsReq : maxKillsReq");
            comments.add("If either the minMobsToKill or maxMobsToKill values are higher or lower than the values specified here, those values will be used instead.");
            settingsYaml.setComments("mobList", comments);

            saveSettingsFile();
            MobShuffle.plugin.getLogger().info("Successfully loaded the default settings!");
        }
    }

    public static void saveSettingsFile(){
        try {
            settingsYaml.save(settingsFile);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}

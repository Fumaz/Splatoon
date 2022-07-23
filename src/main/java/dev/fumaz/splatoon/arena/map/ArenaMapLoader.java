package dev.fumaz.splatoon.arena.map;

import dev.fumaz.commons.bukkit.config.YamlConfigurationFile;
import dev.fumaz.commons.bukkit.misc.Logging;
import dev.fumaz.commons.bukkit.world.EmptyChunkGenerator;
import dev.fumaz.commons.io.Files;
import dev.fumaz.commons.math.Randoms;
import dev.fumaz.splatoon.Splatoon;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class ArenaMapLoader {

    private final Splatoon plugin;
    private final Logger logger;
    private final Set<ArenaMapConfiguration> maps;

    private int mapID;

    public ArenaMapLoader(Splatoon plugin) {
        this.plugin = plugin;
        this.logger = Logging.of(plugin);
        this.maps = new HashSet<>();
        this.mapID = 0;

        load();
    }

    public ArenaMap createMap() {
        ArenaMapConfiguration configuration = Randoms.choice(maps);
        File directory = getAvailableWorldFolder();

        Files.copyFileOrFolder(configuration.getDirectory(), directory);

        World world = new WorldCreator(directory.getName())
                .generator(new EmptyChunkGenerator())
                .type(WorldType.FLAT)
                .createWorld();

        Map<String, Location> locations = new HashMap<>();
        for (String key : configuration.getLocations().keySet()) {
            Location location = configuration.getLocations().get(key).clone();
            location.setWorld(world);

            locations.put(key, location);
        }

        return new ArenaMap(configuration.getWorldName(), world, locations, configuration.getUnpaintableMaterials(), configuration.getRadius(), configuration.getMinY());
    }

    private File getAvailableWorldFolder() {
        return new File(Bukkit.getWorldContainer(), String.valueOf(mapID++));
    }

    private void load() {
        logger.info("Loading maps...");

        File directory = new File(plugin.getDataFolder(), "maps");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        for (File file : directory.listFiles()) {
            if (!file.isDirectory()) {
                continue;
            }

            File configurationFile = new File(file, "config.yml");

            if (!configurationFile.exists()) {
                continue;
            }

            YamlConfigurationFile configuration = new YamlConfigurationFile(plugin, configurationFile);
            YamlConfiguration yaml = configuration.getYaml();

            String worldName = yaml.getString("world");
            int radius = yaml.getInt("radius");
            int minY = yaml.getInt("minY", -100);

            Map<String, Location> locations = new HashMap<>();

            ConfigurationSection locationsSection = yaml.getConfigurationSection("locations");
            locationsSection.getKeys(false).forEach(key -> {
                locations.put(key, locationsSection.getLocation(key));
            });

            Set<Material> unpaintableMaterials = new HashSet<>();

            if (yaml.contains("unpaintable-materials")) {
                ConfigurationSection unpaintableMaterialsSection = yaml.getConfigurationSection("unpaintable-materials");
                unpaintableMaterialsSection.getKeys(false).forEach(key -> {
                    unpaintableMaterials.add(Material.matchMaterial(key));
                });
            }

            maps.add(new ArenaMapConfiguration(worldName, file, locations, unpaintableMaterials, radius, minY));
            logger.info("Loaded map " + worldName);
        }

        logger.info("Loaded " + maps.size() + " maps.");
    }

}

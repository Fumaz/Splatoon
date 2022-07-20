package dev.fumaz.splatoon.arena.map;

import org.bukkit.Location;

import java.io.File;
import java.util.Map;

public class ArenaMapConfiguration {

    private final String worldName;
    private final File directory;
    private final Map<String, Location> locations;
    private final int radius;

    public ArenaMapConfiguration(String worldName, File directory, Map<String, Location> locations, int radius) {
        this.worldName = worldName;
        this.directory = directory;
        this.locations = locations;
        this.radius = radius;
    }

    public String getWorldName() {
        return worldName;
    }

    public File getDirectory() {
        return directory;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public int getRadius() {
        return radius;
    }
}

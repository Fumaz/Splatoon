package dev.fumaz.splatoon.arena.map;

import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class ArenaMapConfiguration {

    private final String worldName;
    private final File directory;
    private final Map<String, Location> locations;
    private final Set<Material> unpaintableMaterials;
    private final int radius;
    private final int minY;

    public ArenaMapConfiguration(String worldName, File directory, Map<String, Location> locations, Set<Material> unpaintableMaterials, int radius, int minY) {
        this.worldName = worldName;
        this.directory = directory;
        this.locations = locations;
        this.unpaintableMaterials = unpaintableMaterials;
        this.radius = radius;
        this.minY = minY;
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

    public Set<Material> getUnpaintableMaterials() {
        return unpaintableMaterials;
    }

    public int getMinY() {
        return minY;
    }

}

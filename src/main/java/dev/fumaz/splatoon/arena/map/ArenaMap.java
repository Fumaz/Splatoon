package dev.fumaz.splatoon.arena.map;

import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Map;
import java.util.Set;

public class ArenaMap {

    private final String name;
    private final World world;
    private final Map<String, Location> locations;
    private final int radius;
    private final int minY;
    private final Set<Material> unpaintableMaterials;

    public ArenaMap(String name, World world, Map<String, Location> locations, Set<Material> unpaintableMaterials, int radius, int minY) {
        this.name = name;
        this.world = world;
        this.locations = locations;
        this.unpaintableMaterials = unpaintableMaterials;
        this.radius = radius;
        this.minY = minY;

        world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        world.setFullTime(1000);
        world.setAutoSave(false);
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Map<String, Location> getLocations() {
        return locations;
    }

    public int getRadius() {
        return radius;
    }

    public Location getLocation(String name) {
        return locations.get(name).clone();
    }

    public boolean isUnpaintable(Material material) {
        return unpaintableMaterials.contains(material);
    }

    public int getMinY() {
        return minY;
    }

}

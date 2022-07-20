package dev.fumaz.splatoon.arena.map;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Map;

public class ArenaMap {

    private final String name;
    private final World world;
    private final Map<String, Location> locations;
    private final int radius;

    public ArenaMap(String name, World world, Map<String, Location> locations, int radius) {
        this.name = name;
        this.world = world;
        this.locations = locations;
        this.radius = radius;
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
        return locations.get(name);
    }

}

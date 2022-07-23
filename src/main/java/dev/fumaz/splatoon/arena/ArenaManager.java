package dev.fumaz.splatoon.arena;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.map.ArenaMap;
import dev.fumaz.splatoon.arena.map.ArenaMapLoader;
import dev.fumaz.splatoon.arena.state.ArenaState;

import java.util.HashSet;
import java.util.Set;

public class ArenaManager {

    private final Splatoon plugin;
    private final ArenaMapLoader mapLoader;
    private final Set<Arena> arenas;

    public ArenaManager(Splatoon plugin, ArenaMapLoader mapLoader) {
        this.plugin = plugin;
        this.mapLoader = mapLoader;
        this.arenas = new HashSet<>();

        Scheduler.of(plugin).runTaskLater(() -> this.createArena(), 20L);
    }

    public Set<Arena> getArenas() {
        return arenas;
    }

    public Arena createArena() {
        ArenaMap map = mapLoader.createMap();
        Arena arena = new Arena(plugin, this, map);
        arenas.add(arena);
        return arena;
    }

    public Arena createArena(String map) {
        ArenaMap arenaMap = mapLoader.createMap(map);
        Arena arena = new Arena(plugin, this, arenaMap);
        arenas.add(arena);
        return arena;
    }

    public Arena getAvailableArena() {
        return arenas.stream()
                .filter(arena -> arena.getState() == ArenaState.WAITING)
                .findFirst()
                .orElseGet(this::createArena);
    }

    public void join(Account account) {
        Arena arena = getAvailableArena();
        arena.join(account);
    }

    public void remove(Arena arena) {
        arenas.remove(arena);
    }

}

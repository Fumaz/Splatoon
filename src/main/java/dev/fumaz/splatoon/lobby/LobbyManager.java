package dev.fumaz.splatoon.lobby;

import dev.fumaz.commons.bukkit.config.YamlConfigurationFile;
import dev.fumaz.commons.bukkit.world.EmptyChunkGenerator;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;

public class LobbyManager {


    private final Splatoon plugin;
    private final LobbyListener listener;

    private World world;
    private Location spawn;
    private Location entranceMin;
    private Location entranceMax;

    public LobbyManager(Splatoon plugin) {
        this.plugin = plugin;
        this.listener = new LobbyListener(plugin, this, plugin.getAccountManager());

        YamlConfigurationFile file = new YamlConfigurationFile(plugin, "lobby.yml");
        YamlConfiguration yaml = file.getYaml();

        this.world = Bukkit.getWorld(yaml.getString("world"));

        if (world == null) {
            world = new WorldCreator(yaml.getString("world"))
                    .type(WorldType.FLAT)
                    .generator(new EmptyChunkGenerator())
                    .createWorld();
        }

        this.spawn = yaml.getLocation("spawn");
        this.entranceMin = yaml.getLocation("entrance.min");
        this.entranceMax = yaml.getLocation("entrance.max");

        spawn.setWorld(world);
        entranceMin.setWorld(world);
        entranceMax.setWorld(world);
    }

    public World getWorld() {
        return world;
    }

    public Location getSpawn() {
        return spawn;
    }

    public Location getEntranceMin() {
        return entranceMin;
    }

    public Location getEntranceMax() {
        return entranceMax;
    }

    public void send(Account account) {
        account.clear();
        account.setCanMove(true);
        account.show();
        account.setArena(null);
        account.setTeam(null);
        account.setScoreboardType(ScoreboardType.LOBBY);
        account.getPlayer().setGameMode(GameMode.ADVENTURE);

        account.getPlayer().teleport(getSpawn());
    }

}

package dev.fumaz.splatoon.arena;

import dev.fumaz.commons.bukkit.math.Geometry;
import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.commons.io.Files;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.component.*;
import dev.fumaz.splatoon.arena.map.ArenaMap;
import dev.fumaz.splatoon.arena.state.ArenaState;
import dev.fumaz.splatoon.arena.task.types.InkTask;
import dev.fumaz.splatoon.arena.task.types.WeaponTask;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import dev.fumaz.splatoon.hotbar.HotbarItemCategory;
import dev.fumaz.splatoon.lobby.LobbyManager;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import org.bukkit.*;
import org.bukkit.block.Block;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Arena {

    private final Splatoon plugin;
    private final Scheduler scheduler;
    private final ArenaMap map;

    private final ArenaManager arenaManager;
    private final LobbyManager lobbyManager;

    private final ArenaListenerComponent listener;
    private final ArenaPlayerComponent players;
    private final ArenaSpectatorComponent spectators;
    private final ArenaTeamComponent teams;
    private final ArenaTaskComponent tasks;
    private final ArenaBlockComponent blocks;
    private final ArenaTimeComponent time;
    private final ArenaBossBarComponent bossBar;

    private ArenaState state;

    public Arena(Splatoon plugin, ArenaManager arenaManager, ArenaMap map) {
        this.plugin = plugin;
        this.scheduler = Scheduler.of(plugin);
        this.arenaManager = arenaManager;
        this.lobbyManager = plugin.getLobbyManager();
        this.map = map;

        this.state = ArenaState.WAITING;

        this.listener = new ArenaListenerComponent(plugin, this);
        this.players = new ArenaPlayerComponent(this);
        this.spectators = new ArenaSpectatorComponent(this);
        this.teams = new ArenaTeamComponent(this);
        this.tasks = new ArenaTaskComponent(plugin, this);
        this.blocks = new ArenaBlockComponent(this, plugin.getAccountManager());
        this.time = new ArenaTimeComponent(this);
        this.bossBar = new ArenaBossBarComponent(this);

        tasks.startTasks();
    }

    public void join(Account account) {
        if (state != ArenaState.WAITING) {
            account.sendMessage(ChatColor.RED + "You are now spectating this arena!");
            spectators.add(account);
            return;
        }

        players.add(account);
        account.clear();
        account.setArena(this);
        account.teleport(map.getLocation("waiting"));
        account.getPlayer().setGameMode(GameMode.ADVENTURE);
        account.setScoreboardType(ScoreboardType.WAITING);
        plugin.getHotbarItemManager().getHotbarItems(HotbarItemCategory.WAITING).forEach(item -> item.give(account));

        broadcast(ChatColor.LIGHT_PURPLE + account.getName() + ChatColor.YELLOW + " joined the game! (" + ChatColor.LIGHT_PURPLE + getPlayers().size() + ChatColor.YELLOW + "/" + ChatColor.LIGHT_PURPLE + "8" + ChatColor.YELLOW + ")");
    }

    public void leave(Account account) {
        players.remove(account);
        spectators.remove(account);
        lobbyManager.send(account);

        bossBar.remove(account);

        ArenaTeam team = teams.getTeam(account);

        if (team != null) {
            team.remove(account);
        }

        if (state == ArenaState.WAITING) {
            broadcast(ChatColor.LIGHT_PURPLE + account.getName() + ChatColor.YELLOW + " left the game! (" + ChatColor.LIGHT_PURPLE + getPlayers().size() + ChatColor.YELLOW + "/" + ChatColor.LIGHT_PURPLE + "8" + ChatColor.YELLOW + ")");
            return;
        }

        if (state == ArenaState.PLAYING) {
            broadcast(ChatColor.LIGHT_PURPLE + account.getName() + ChatColor.YELLOW + " left the game!");
        }
    }

    public void kill(Account account) {
        spectators.add(account);

        account.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "YOU GOT SPLATTED!", null);

        AtomicInteger seconds = new AtomicInteger(6);
        scheduler.runTaskTimer(task -> {
            if (seconds.decrementAndGet() <= 0) {
                respawn(account);
                task.cancel();
                return;
            }

            account.sendTitle(ChatColor.LIGHT_PURPLE + "" + seconds.get(), null);
        }, 0, 20);
    }

    public void respawn(Account account) {
        spectators.remove(account);

        account.clear();
        account.setScoreboardType(ScoreboardType.PLAYING);
        account.teleport(teams.getTeamSpawnLocation(teams.getTeam(account)));
        account.setCanMove(false);
        account.getPlayer().setGameMode(GameMode.ADVENTURE);
        account.getWeapon().apply(account);
        plugin.getHotbarItemManager().getHotbarItems(HotbarItemCategory.PLAYING).forEach(item -> item.give(account));
        bossBar.remove(account);
        bossBar.add(account);

        Queue<String> messages = new LinkedList<>(Arrays.asList("READY", "SET", "SPLAT!"));

        scheduler.runTaskTimer(task -> {
            String message = messages.poll();

            if (messages.isEmpty()) {
                task.cancel();
                account.setCanMove(true);
            }

            account.sendTitle(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + message, null);
        }, 0, 20);
    }

    public void broadcast(String message) {
        getAccounts().forEach(account -> account.sendMessage(message));
    }

    public void broadcastTitle(String title, String subtitle) {
        getAccounts().forEach(account -> account.sendTitle(title, subtitle));
    }

    public void broadcastSound(Sound sound) {
        getAccounts().forEach(account -> account.getPlayer().playSound(account.getPlayer().getLocation(), sound, 1f, 1f));
    }

    public void broadcastActionBar(String message) {
        getAccounts().forEach(account -> account.sendActionBar(message));
    }

    public void start() {
        if (state != ArenaState.WAITING) {
            return;
        }

        state = ArenaState.STARTING;
        teams.create();

        tasks.startTask(WeaponTask.class);

        startGame();
    }

    public void startGame() {
        if (state != ArenaState.STARTING) {
            return;
        }

        tasks.startTask(InkTask.class);

        state = ArenaState.PLAYING;
        getTeams().getTeams().forEach(team -> {
            Location teamSpawn = map.getLocation("team-spawn-" + getTeams().getTeams().indexOf(team));
            Geometry.cube(10, 1, vector -> {
                Location location = teamSpawn.clone().add(vector);
                Block block = location.getBlock();

                if (block.getType() != Material.BARRIER) {
                    return;
                }

                block.setType(Material.AIR, false);
            });
        });
        getPlayers().forEach(this::respawn);
    }

    public void stop() {
        state = ArenaState.ENDING;

        tasks.stopTasks();
        arenaManager.remove(this);
        getAccounts().forEach(this::leave);

        state = ArenaState.ENDED;

        Bukkit.unloadWorld(map.getWorld(), false);
        scheduler.runTaskLater(() -> Files.deleteDirectory(map.getWorld().getWorldFolder()), 15 * 20L);
    }

    public void win() {
        state = ArenaState.ENDING;

        tasks.stopTask(WeaponTask.class);
        tasks.stopTask(InkTask.class);

        Map<ArenaTeam, Double> percentages = calculatePercentages();
        ArenaTeam winner = percentages.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        AtomicInteger seconds = new AtomicInteger(4);

        getAccounts().forEach(account -> {
            account.clear();
            account.hide();
            account.getPlayer().setGameMode(GameMode.ADVENTURE);
            account.teleport(map.getLocation("end-game"));
        });

        scheduler.runTaskTimer(task -> {
            if (seconds.decrementAndGet() == 0) {
                teams.setWinner(winner);
                teams.getTeams().forEach(team -> {
                    team.getAccounts().forEach(account -> {
                        account.sendTitle(team == winner ? (ChatColor.GOLD + "" + ChatColor.BOLD + "YOU WIN!") : (ChatColor.RED + "" + ChatColor.BOLD + "YOU LOSE!"), null);
                        account.getPlayer().playSound(account.getPlayer().getLocation(), team == winner ? Sound.ENTITY_PLAYER_LEVELUP : Sound.ENTITY_PLAYER_DEATH, 1f, 1f);
                        account.sendMessage(team == winner ? (ChatColor.GOLD + "" + ChatColor.BOLD + "YOU WIN!") : (ChatColor.RED + "" + ChatColor.BOLD + "YOU LOSE!"));
                        account.sendMessage(team.getDisplayName() + ": " + ChatColor.LIGHT_PURPLE + percentages.get(team) + "%");
                        account.sendMessage(teams.getOppositeTeam(team).getDisplayName() + ": " + ChatColor.LIGHT_PURPLE + percentages.get(teams.getOppositeTeam(team)) + "%");
                    });
                });

                scheduler.runTaskLater(this::stop, 20 * 10);

                task.cancel();
                return;
            }

            broadcastActionBar(ChatColor.LIGHT_PURPLE + "CALCULATING...");
            broadcastSound(Sound.BLOCK_NOTE_BLOCK_PLING);
        }, 0, 20);
    }

    public boolean isAlive(Account account) {
        return getPlayers().contains(account) && teams.getTeam(account) != null && !getSpectators().getSpectators().contains(account);
    }

    public Set<Account> getAccounts() {
        Set<Account> accounts = new HashSet<>(players.getPlayers());
        accounts.addAll(spectators.getSpectators());

        return accounts;
    }

    public ArenaMap getMap() {
        return map;
    }

    public ArenaState getState() {
        return state;
    }

    public ArenaPlayerComponent getPlayers() {
        return players;
    }

    public ArenaSpectatorComponent getSpectators() {
        return spectators;
    }

    public ArenaTeamComponent getTeams() {
        return teams;
    }

    public ArenaBlockComponent getBlocks() {
        return blocks;
    }

    public ArenaTimeComponent getTime() {
        return time;
    }

    public ArenaBossBarComponent getBossBar() {
        return bossBar;
    }

    private Map<ArenaTeam, Double> calculatePercentages() {
        Map<ArenaTeam, Integer> teamBlocks = new HashMap<>();
        AtomicInteger totalBlocks = new AtomicInteger();

        Location center = map.getLocation("center");

        for (int x = -map.getRadius(); x <= map.getRadius(); ++x) {
            for (int z = -map.getRadius(); z <= map.getRadius(); ++z) {
                Location location = center.clone().add(x, 0, z);
                int y = map.getWorld().getHighestBlockYAt(location);
                location.setY(y);

                Block block = location.getBlock();

                if (block.isEmpty()) {
                    continue;
                }

                totalBlocks.getAndIncrement();
                ArenaTeam team = blocks.getTeamByBlock(block);

                if (team != null) {
                    teamBlocks.merge(team, 1, Integer::sum);
                }
            }
        }

        Map<ArenaTeam, Double> percentages = new HashMap<>();

        for (ArenaTeam team : teamBlocks.keySet()) {
            percentages.put(team, round((((double) teamBlocks.get(team) / totalBlocks.get()) * 100), 1));
        }

        return percentages;
    }

    private double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}

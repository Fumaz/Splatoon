package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.math.Randoms;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import dev.fumaz.splatoon.arena.team.TeamColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaTeamComponent extends ArenaComponent{

    private final List<ArenaTeam> teams;

    private ArenaTeam winner;

    public ArenaTeamComponent(Arena arena) {
        super(arena);

        this.teams = new ArrayList<>();
    }

    public ArenaTeam getTeam(Account account) {
        return teams.stream()
                .filter(team -> team.contains(account))
                .findFirst()
                .orElse(null);
    }

    public ArenaTeam getWinner() {
        return winner;
    }

    public void setWinner(ArenaTeam winner) {
        this.winner = winner;
    }

    public List<ArenaTeam> getTeams() {
        return teams;
    }

    public ArenaTeam getFirst() {
        return teams.get(0);
    }

    public ArenaTeam getSecond() {
        return teams.get(1);
    }

    public ArenaTeam getOppositeTeam(ArenaTeam team) {
        return teams.stream()
                .filter(t -> t != team)
                .findFirst()
                .orElse(null);
    }

    public void add(ArenaTeam team) {
        teams.add(team);
    }

    public void remove(ArenaTeam team) {
        teams.remove(team);
    }

    public int size() {
        return teams.size();
    }

    public Location getTeamSpawnLocation(ArenaTeam team) {
        return arena.getMap().getLocation("team-spawn-" + teams.indexOf(team));
    }

    public void create() {
        for (int i = 0; i < 2; i++) {
            List<TeamColor> colors = Arrays.stream(TeamColor.values())
                    .filter(color -> teams.stream().noneMatch(team -> team.getColor().equals(color)))
                    .collect(Collectors.toList());

            teams.add(new ArenaTeam(Randoms.choice(colors)));
        }

        arena.getPlayers().forEach(account -> {
            ArenaTeam team = teams.stream()
                    .min(Comparator.comparing(ArenaTeam::size))
                    .orElse(null);

            if (team == null) {
                return;
            }

            team.add(account);
            account.sendMessage(ChatColor.YELLOW + "You are in team " + team.getDisplayName() + ChatColor.YELLOW + "!");
        });
    }

}

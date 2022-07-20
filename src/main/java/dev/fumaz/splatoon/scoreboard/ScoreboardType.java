package dev.fumaz.splatoon.scoreboard;

import dev.fumaz.commons.bukkit.scoreboard.BlankScoreboardEntry;
import dev.fumaz.commons.bukkit.scoreboard.DynamicScoreboardEntry;
import dev.fumaz.commons.bukkit.scoreboard.StaticScoreboardEntry;
import dev.fumaz.commons.text.TimeFormatting;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import org.bukkit.ChatColor;

public enum ScoreboardType {
    LOBBY {
        @Override
        public void update(SplatoonScoreboard scoreboard) {
            Account account = scoreboard.getAccount();

            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new StaticScoreboardEntry(ChatColor.WHITE + "Coins: " + ChatColor.LIGHT_PURPLE + "0", true)); // TODO
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Weapon: "+ ChatColor.LIGHT_PURPLE + account.getWeapon().getName(), true));
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Gear: "+ ChatColor.LIGHT_PURPLE + "None", true)); // TODO
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new StaticScoreboardEntry(ChatColor.LIGHT_PURPLE + "fumaz.dev", true));
        }
    },
    WAITING {
        @Override
        public void update(SplatoonScoreboard scoreboard) {
            Account account = scoreboard.getAccount();
            Arena arena = account.getArena();

            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Starts in: " + ChatColor.YELLOW + arena.getTime().getWaitingTime() + "s", true));
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Players: " + ChatColor.YELLOW + arena.getPlayers().size(), true));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Map: " + ChatColor.YELLOW + arena.getMap().getName(), true));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new StaticScoreboardEntry(ChatColor.LIGHT_PURPLE + "fumaz.dev", true));
        }
    },
    PLAYING {
        @Override
        public void update(SplatoonScoreboard scoreboard) {
            Account account = scoreboard.getAccount();
            Arena arena = account.getArena();
            ArenaTeam team = account.getTeam();

            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Time left: " + ChatColor.YELLOW + TimeFormatting.formatSeconds(arena.getTime().getTimeLeft()), true));
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Players: " + ChatColor.YELLOW + arena.getPlayers().size(), true));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Team:", () -> ChatColor.YELLOW + team.getName()));
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Map:", () -> ChatColor.YELLOW + arena.getMap().getName()));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new StaticScoreboardEntry(ChatColor.LIGHT_PURPLE + "fumaz.dev", true));
        }
    },
    SPECTATING {
        @Override
        public void update(SplatoonScoreboard scoreboard) {
            Account account = scoreboard.getAccount();
            Arena arena = account.getArena();

            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Time left: " + ChatColor.YELLOW + TimeFormatting.formatSeconds(arena.getTime().getTimeLeft()), true));
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Players: " + ChatColor.YELLOW + arena.getPlayers().size(), true));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new DynamicScoreboardEntry(() -> ChatColor.WHITE + "Map: " + ChatColor.YELLOW + arena.getMap().getName(), true));
            scoreboard.addEntry(new BlankScoreboardEntry());
            scoreboard.addEntry(new StaticScoreboardEntry(ChatColor.LIGHT_PURPLE + "fumaz.dev", true));
        }
    };

    public abstract void update(SplatoonScoreboard scoreboard);

}

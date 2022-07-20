package dev.fumaz.splatoon.arena.component;

import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import org.bukkit.GameMode;

import java.util.HashSet;
import java.util.Set;

public class ArenaSpectatorComponent extends ArenaComponent{

    private final Set<Account> spectators;

    public ArenaSpectatorComponent(Arena arena) {
        super(arena);

        this.spectators = new HashSet<>();
    }

    public Set<Account> getSpectators() {
        return spectators;
    }

    public void add(Account account) {
        spectators.add(account);

        account.getPlayer().setAllowFlight(true);
        account.getPlayer().setFlying(true);
        account.setScoreboardType(ScoreboardType.SPECTATING);
        account.getPlayer().setGameMode(GameMode.ADVENTURE);

        if (account.getWorld() != arena.getMap().getWorld()) {
            account.teleport(arena.getMap().getLocation("spectating"));
        }
    }

    public void remove(Account account) {
        spectators.remove(account);

        account.getPlayer().setFlying(false);
        account.getPlayer().setAllowFlight(false);
    }


}

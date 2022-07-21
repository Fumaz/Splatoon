package dev.fumaz.splatoon.arena.component;

import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.hotbar.HotbarItemCategory;
import dev.fumaz.splatoon.hotbar.HotbarItemManager;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import org.bukkit.GameMode;

import java.util.HashSet;
import java.util.Set;

public class ArenaSpectatorComponent extends ArenaComponent{

    private final Set<Account> spectators;
    private final HotbarItemManager hotbarItemManager;

    public ArenaSpectatorComponent(Arena arena, HotbarItemManager hotbarItemManager) {
        super(arena);

        this.spectators = new HashSet<>();
        this.hotbarItemManager = hotbarItemManager;
    }

    public Set<Account> getSpectators() {
        return spectators;
    }

    public void add(Account account) {
        spectators.add(account);

        account.clear();
        account.getPlayer().setAllowFlight(true);
        account.getPlayer().setFlying(true);
        account.setScoreboardType(ScoreboardType.SPECTATING);
        account.getPlayer().setGameMode(GameMode.ADVENTURE);
        account.hide();

        if (account.getWorld() != arena.getMap().getWorld()) {
            account.teleport(arena.getMap().getLocation("spectating"));
        }

        hotbarItemManager.getHotbarItems(HotbarItemCategory.SPECTATING, HotbarItemCategory.LEAVE).forEach(item -> item.give(account));
    }

    public void remove(Account account) {
        spectators.remove(account);

        account.show();
        account.getPlayer().setFlying(false);
        account.getPlayer().setAllowFlight(false);
    }


}

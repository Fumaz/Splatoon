package dev.fumaz.splatoon.visibility;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementManager implements FListener {

    private final Splatoon plugin;
    private final AccountManager accountManager;

    public MovementManager(Splatoon plugin, AccountManager accountManager) {
        this.plugin = plugin;
        this.accountManager = accountManager;

        register(plugin);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }

        Account account = accountManager.getAccount(event.getPlayer());

        if (account.isCanMove()) {
            return;
        }

        Location to = event.getFrom();
        to.setYaw(event.getTo().getYaw());
        to.setPitch(event.getTo().getPitch());

        event.setTo(to);
    }

}

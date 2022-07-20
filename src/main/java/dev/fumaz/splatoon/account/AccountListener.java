package dev.fumaz.splatoon.account;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.bukkit.misc.Logging;
import dev.fumaz.splatoon.Splatoon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Logger;

public class AccountListener implements FListener {

    private final AccountManager accountManager;
    private final Logger logger;

    public AccountListener(Splatoon plugin, AccountManager accountManager) {
        this.accountManager = accountManager;
        this.logger = Logging.of(plugin);

        register(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Account account = accountManager.getAccount(event.getPlayer());
        logger.info("Account connected: " + account.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Account account = accountManager.getAccount(event.getPlayer());
        accountManager.remove(account);

        logger.info("Account disconnected: " + account.getPlayer().getName());
    }

}

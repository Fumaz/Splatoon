package dev.fumaz.splatoon.lobby;

import dev.fumaz.commons.bukkit.cache.PlayerCooldown;
import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.concurrent.TimeUnit;

public class LobbyListener implements FListener {

    private final Splatoon plugin;
    private final AccountManager accountManager;
    private final LobbyManager lobbyManager;
    private final ArenaManager arenaManager;

    private final PlayerCooldown cooldown;

    public LobbyListener(Splatoon plugin, LobbyManager lobbyManager, AccountManager accountManager) {
        this.plugin = plugin;
        this.lobbyManager = lobbyManager;
        this.accountManager = accountManager;
        this.arenaManager = plugin.getArenaManager();
        this.cooldown = new PlayerCooldown(15, TimeUnit.SECONDS);

        register(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Account account = accountManager.getAccount(event.getPlayer());
            lobbyManager.send(account);
        }, 5L);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) {
            return;
        }

        Location to = event.getTo();

        if (to.getBlockX() < lobbyManager.getEntranceMin().getBlockX() || to.getBlockX() > lobbyManager.getEntranceMax().getBlockX()) {
            return;
        }

        if (to.getBlockZ() < lobbyManager.getEntranceMin().getBlockZ() || to.getBlockZ() > lobbyManager.getEntranceMax().getBlockZ()) {
            return;
        }

        if (to.getBlockY() < lobbyManager.getEntranceMin().getBlockY() || to.getBlockY() > lobbyManager.getEntranceMax().getBlockY()) {
            return;
        }

        if (cooldown.has(event.getPlayer())) {
            return;
        }

        cooldown.put(event.getPlayer());
        arenaManager.join(accountManager.getAccount(event.getPlayer()));
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.CUSTOM) {
            return;
        }

        event.setCancelled(true);
    }

}

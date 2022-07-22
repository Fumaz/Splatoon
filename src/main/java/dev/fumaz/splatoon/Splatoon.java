package dev.fumaz.splatoon;

import dev.fumaz.commons.bukkit.misc.Logging;
import dev.fumaz.commons.io.Files;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.ArenaManager;
import dev.fumaz.splatoon.arena.map.ArenaMapLoader;
import dev.fumaz.splatoon.command.LeaveCommand;
import dev.fumaz.splatoon.command.PlayCommand;
import dev.fumaz.splatoon.command.StartCommand;
import dev.fumaz.splatoon.command.StopCommand;
import dev.fumaz.splatoon.hotbar.HotbarItemManager;
import dev.fumaz.splatoon.lobby.LobbyManager;
import dev.fumaz.splatoon.visibility.MovementManager;
import dev.fumaz.splatoon.visibility.VisibilityManager;
import dev.fumaz.splatoon.weapon.WeaponManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Splatoon extends JavaPlugin {

    private AccountManager accountManager;
    private LobbyManager lobbyManager;
    private ArenaMapLoader mapLoader;
    private ArenaManager arenaManager;
    private WeaponManager weaponManager;
    private VisibilityManager visibilityManager;
    private MovementManager movementManager;
    private HotbarItemManager hotbarItemManager;

    @Override
    public void onEnable() {
        Logging.splash(this, ChatColor.YELLOW.asBungee());
        cleanWorlds();

        this.accountManager = new AccountManager(this);
        this.mapLoader = new ArenaMapLoader(this);
        this.arenaManager = new ArenaManager(this, mapLoader);
        this.weaponManager = new WeaponManager(this);
        this.lobbyManager = new LobbyManager(this);
        this.visibilityManager = new VisibilityManager(this, accountManager);
        this.movementManager = new MovementManager(this, accountManager);
        this.hotbarItemManager = new HotbarItemManager(this);

        getCommand("play").setExecutor(new PlayCommand(accountManager, arenaManager));
        getCommand("start").setExecutor(new StartCommand(accountManager));
        getCommand("leave").setExecutor(new LeaveCommand(accountManager));
        getCommand("arenastop").setExecutor(new StopCommand(accountManager));
    }

    public AccountManager getAccountManager() {
        return accountManager;
    }

    public LobbyManager getLobbyManager() {
        return lobbyManager;
    }

    public ArenaMapLoader getMapLoader() {
        return mapLoader;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public WeaponManager getWeaponManager() {
        return weaponManager;
    }

    public HotbarItemManager getHotbarItemManager() {
        return hotbarItemManager;
    }

    private void cleanWorlds() {
        for (File world : Bukkit.getWorldContainer().listFiles()) {
            try {
                int i = Integer.parseInt(world.getName());
                Files.deleteDirectory(world);
            } catch (NumberFormatException ignored) {
            }
        }
    }
}

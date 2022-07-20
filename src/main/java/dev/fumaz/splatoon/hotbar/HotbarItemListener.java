package dev.fumaz.splatoon.hotbar;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class HotbarItemListener implements FListener {

    private final AccountManager accountManager;
    private final HotbarItemManager manager;

    public HotbarItemListener(Splatoon plugin, HotbarItemManager manager) {
        this.accountManager = plugin.getAccountManager();
        this.manager = manager;

        register(plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HotbarItem current = manager.getHotbarItem(event.getCurrentItem());
        HotbarItem cursor = manager.getHotbarItem(event.getCursor());

        if (current == null && cursor == null) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        HotbarItem item = manager.getHotbarItem(event.getItem());

        if (item == null) {
            return;
        }

        Account account = accountManager.getAccount(event.getPlayer());
        item.click(account, event);
    }

}

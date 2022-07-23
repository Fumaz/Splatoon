package dev.fumaz.splatoon.hotbar;

import dev.fumaz.splatoon.account.Account;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

public class HotbarItem {

    private final ItemStack item;
    private final int slot;
    private final BiConsumer<Account, PlayerInteractEvent> onClick;

    public HotbarItem(ItemStack item, int slot, BiConsumer<Account, PlayerInteractEvent> onClick) {
        this.item = item;
        this.slot = slot;
        this.onClick = onClick;
    }

    public ItemStack getItem() {
        return item;
    }

    public void click(Account account, PlayerInteractEvent event) {
        event.setCancelled(true);
        onClick.accept(account, event);
    }

    public void give(Account account) {
        account.getInventory().setItem(slot, item);
    }

    public void give(Account account, int slot) {
        account.getInventory().setItem(slot, item);
    }

}

package dev.fumaz.splatoon.weapon;

import dev.fumaz.commons.bukkit.cache.PlayerCooldown;
import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.commons.bukkit.text.ColorFormatting;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class Weapon implements FListener {

    protected final Splatoon plugin;

    protected final PlayerCooldown abilityCooldown;
    protected final PlayerCooldown ultimateCooldown;

    public Weapon(Splatoon plugin) {
        this.plugin = plugin;
        this.abilityCooldown = new PlayerCooldown(getAbilityCooldown());
        this.ultimateCooldown = new PlayerCooldown(getUltimateCooldown());

        register(plugin);
    }

    public abstract String getName();

    public abstract String getDisplayName();

    public abstract List<String> getDescription();

    protected abstract ItemStack getItemStack();

    public abstract String getAbilityName();

    public abstract String getUltimateName();

    public abstract List<String> getAbilityDescription();

    public abstract List<String> getUltimateDescription();

    public abstract int getAbilityCooldown();

    public abstract int getUltimateCooldown();

    public abstract int getAbilityInk();

    protected void onTick(Account account, int ticks) {
    }

    protected void onAbility(Account account, PlayerInteractEvent event) {
    }

    protected void onUltimate(Account account, PlayerInteractEvent event) {
    }

    public ItemStack getIcon() {
        return ItemBuilder.copy(getItemStack())
                .displayName(getDisplayName())
                .lore(getDescription())
                .addToLore(ChatColor.GRAY.toString())
                .addToLore(ChatColor.RED + "" + ChatColor.BOLD + getAbilityName().toUpperCase() + ChatColor.GRAY + " (right-click) [" + (getAbilityCooldown() / 1000) + "s]")
                .addToLore(ColorFormatting.prefix(ChatColor.WHITE, getAbilityDescription()))
                .addToLore(ChatColor.GRAY.toString())
                .addToLore(ChatColor.GOLD + "" + ChatColor.BOLD + getUltimateName().toUpperCase() + ChatColor.GRAY + " (left-click) [" + (getUltimateCooldown() / 1000) + "s]")
                .addToLore(ColorFormatting.prefix(ChatColor.WHITE, getUltimateDescription()))
                .build();
    }

    public void ability(Account account, PlayerInteractEvent event) {
        if (abilityCooldown.has(account.getPlayer())) {
            return;
        }

        if (!account.hasInk(getAbilityInk())) {
            account.getPlayer().sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "NO INK!", ChatColor.WHITE + "Refill it using squid mode!", 0, 20, 0);
            return;
        }

        account.useInk(getAbilityInk());
        abilityCooldown.put(account.getPlayer());
        onAbility(account, event);
    }

    public void ultimate(Account account, PlayerInteractEvent event) {
        if (ultimateCooldown.has(account.getPlayer())) {
            return;
        }

        ultimateCooldown.put(account.getPlayer());
        onUltimate(account, event);
    }

    public void tick(Account account, int ticks) {
        int ultimateSeconds = (int) ultimateCooldown.getSeconds(account.getPlayer().getUniqueId());

        if (ultimateSeconds > 0) {
            int ultimateMillis = (int) ultimateCooldown.getMillis(account.getPlayer().getUniqueId());
            account.sendActionBar(ChatColor.RED + "ULTIMATE RECHARGING");
        } else {
            account.sendActionBar(((ticks / 10) % 2 == 0 ? ChatColor.WHITE : ChatColor.YELLOW) + "ULTIMATE READY");
        }

        onTick(account, ticks);
    }

    public void apply(Account account) {
        account.getInventory().setItem(0, getIcon());
        // TODO
    }

    public void unapply(Account account) {
        // TODO
    }

    protected boolean isItem(ItemStack item) {
        return getIcon().isSimilar(item);
    }

}

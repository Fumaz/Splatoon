package dev.fumaz.splatoon.weapon.types;

import com.google.common.collect.ImmutableList;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class Roller extends Weapon {

    public Roller(Splatoon plugin) {
        super(plugin);
    }

    @Override
    public String getName() {
        return "Roller";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.AQUA + "" + ChatColor.BOLD + getName().toUpperCase();
    }

    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }

    @Override
    protected ItemStack getItemStack() {
        return new ItemStack(Material.DIAMOND_AXE);
    }

    @Override
    public String getAbilityName() {
        return "Roll";
    }

    @Override
    public String getUltimateName() {
        return "Explode";
    }

    @Override
    public List<String> getAbilityDescription() {
        return ImmutableList.of(
                "Leave ink wherever you go",
                "instantly killing any players!"
        );
    }

    @Override
    public List<String> getUltimateDescription() {
        return ImmutableList.of(
                "Explode ink everywhere",
                "in a huge radius!"
        );
    }

    @Override
    public int getAbilityCooldown() {
        return 0;
    }

    @Override
    public int getUltimateCooldown() {
        return 60;
    }

    @Override
    public int getAbilityInk() {
        return 1;
    }

    @Override
    public void ability(Account account, PlayerInteractEvent event) {
    }

    @Override
    protected void onUltimate(Account account, PlayerInteractEvent event) {
        account.getArena().getBlocks().splat(account.getTeam(), account.getPlayer().getLocation(), 15, 100);
    }

    @Override
    protected void onTick(Account account, int ticks) {
        if (ticks % 4 != 0) {
            return;
        }

        if (!account.hasInk(getAbilityInk())) {
            return;
        }

        if (!account.isCanMove() || account.isHidden()) {
            return;
        }

        if (account.isSquid()) {
            return;
        }

        account.useInk(getAbilityInk());
        account.getArena().getBlocks().splat(account.getTeam(), account.getPlayer().getLocation(), 3, 100);
    }

}

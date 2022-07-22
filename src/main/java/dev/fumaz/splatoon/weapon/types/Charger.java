package dev.fumaz.splatoon.weapon.types;

import com.google.common.collect.ImmutableList;
import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Charger extends Weapon {

    private final Map<Arrow, ArenaInfo> projectiles;

    public Charger(Splatoon plugin) {
        super(plugin);

        this.projectiles = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Charger";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GOLD + "" + ChatColor.BOLD + getName().toUpperCase();
    }

    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }

    @Override
    protected ItemStack getItemStack() {
        return ItemBuilder.of(Material.BOW)
                .addItemFlags(ItemFlag.HIDE_ENCHANTS)
                .enchant(Enchantment.ARROW_INFINITE, 1)
                .unbreakable()
                .build();
    }

    @Override
    public String getAbilityName() {
        return "Sniper";
    }

    @Override
    public String getUltimateName() {
        return "Rapid Fire";
    }

    @Override
    public List<String> getAbilityDescription() {
        return ImmutableList.of(
                "Charge your gun to shoot",
                "a charged ink bullet!"
        );
    }

    @Override
    public List<String> getUltimateDescription() {
        return ImmutableList.of(
                "Unleash your fury with",
                "a storm of bullets!"
        );
    }

    @Override
    public int getAbilityCooldown() {
        return 2;
    }

    @Override
    public int getUltimateCooldown() {
        return (int) TimeUnit.SECONDS.toMillis(60);
    }

    @Override
    public int getAbilityInk() {
        return 10;
    }

    @Override
    public void apply(Account account) {
        super.apply(account);
        account.getInventory().setItem(8, new ItemStack(Material.ARROW));
    }

    @Override
    public void ability(Account account, PlayerInteractEvent event) {
        event.setCancelled(false);
    }

    @EventHandler
    public void onBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!(event.getProjectile() instanceof Arrow arrow)) {
            return;
        }

        Account account = plugin.getAccountManager().getAccount(player);

        if (account.getWeapon() != this) {
            return;
        }

        if (!account.hasInk(getAbilityInk())) {
            return;
        }

        account.useInk(getAbilityInk());
        projectiles.put(arrow, new ArenaInfo(account.getArena(), account, event.getForce()));
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) {
            return;
        }

        if (!projectiles.containsKey(arrow)) {
            return;
        }

        ArenaInfo info = projectiles.remove(arrow);
        float power = info.force * 20;

        arrow.remove();

        info.arena.getBlocks().splat(info.account, arrow.getLocation(), 3, power);
    }

    @Override
    protected void onUltimate(Account account, PlayerInteractEvent event) {
        AtomicInteger arrows = new AtomicInteger(100);
        Scheduler.of(plugin).runTaskTimer(task -> {
            if (arrows.decrementAndGet() < 0) {
                task.cancel();
                return;
            }

            Arrow arrow = account.getPlayer().launchProjectile(Arrow.class);
            projectiles.put(arrow, new ArenaInfo(account.getArena(), account, 1));
            account.getPlayer().playSound(account.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        }, 0, 1);
    }

    private static class ArenaInfo {
        private final Arena arena;
        private final Account account;
        private final float force;

        private ArenaInfo(Arena arena, Account account, float force) {
            this.arena = arena;
            this.account = account;
            this.force = force;
        }
    }
}

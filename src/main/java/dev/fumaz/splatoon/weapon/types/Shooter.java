package dev.fumaz.splatoon.weapon.types;

import com.google.common.collect.ImmutableList;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import dev.fumaz.splatoon.weapon.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Shooter extends Weapon {

    private final Map<Snowball, ArenaInfo> projectiles;
    private final Map<TNTPrimed, ArenaInfo> bombs;

    public Shooter(Splatoon plugin) {
        super(plugin);

        this.projectiles = new HashMap<>();
        this.bombs = new HashMap<>();
    }

    @Override
    public String getName() {
        return "Shooter";
    }

    @Override
    public String getDisplayName() {
        return ChatColor.GREEN + "" + ChatColor.BOLD + getName().toUpperCase();
    }

    @Override
    public List<String> getDescription() {
        return Collections.emptyList();
    }

    @Override
    protected ItemStack getItemStack() {
        return new ItemStack(Material.STICK);
    }

    @Override
    public String getAbilityName() {
        return "Shoot";
    }

    @Override
    public String getUltimateName() {
        return "Bomb";
    }

    @Override
    public List<String> getAbilityDescription() {
        return ImmutableList.of(
                "Shoot and paint the ground!"
        );
    }

    @Override
    public List<String> getUltimateDescription() {
        return ImmutableList.of(
                "Launch a bomb that will explode in",
                "a huge blast of ink!"
        );
    }

    @Override
    public int getAbilityCooldown() {
        return 0;
    }

    @Override
    public int getUltimateCooldown() {
        return (int) TimeUnit.SECONDS.toMillis(60);
    }

    @Override
    public int getAbilityInk() {
        return 1;
    }

    @Override
    protected void onAbility(Account account, PlayerInteractEvent event) {
        Player player = account.getPlayer();
        Snowball snowball = player.launchProjectile(Snowball.class);

        projectiles.put(snowball, new ArenaInfo(account.getArena(), account.getTeam()));
        player.playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1f, 1f);
    }

    @Override
    protected void onUltimate(Account account, PlayerInteractEvent event) {
        Player player = account.getPlayer();
        Vector velocity = player.getEyeLocation().getDirection().multiply(1);

        TNTPrimed tnt = player.getWorld().spawn(player.getEyeLocation(), TNTPrimed.class);
        tnt.setVelocity(velocity);

        bombs.put(tnt, new ArenaInfo(account.getArena(), account.getTeam()));
    }

    @EventHandler
    protected void onSnowballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball snowball)) {
            return;
        }

        ArenaInfo info = projectiles.remove(snowball);

        if (info == null) {
            return;
        }

        info.arena.getBlocks().splat(info.team, snowball.getLocation(), 2, 5);
        event.setCancelled(true);
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed tnt)) {
            return;
        }

        ArenaInfo info = bombs.remove(tnt);

        if (info == null) {
            return;
        }

        info.arena.getBlocks().splat(info.team, tnt.getLocation(), 5, 15);
        event.setCancelled(true);
    }

    private static class ArenaInfo {
        private final Arena arena;
        private final ArenaTeam team;

        private ArenaInfo(Arena arena, ArenaTeam team) {
            this.arena = arena;
            this.team = team;
        }
    }
}

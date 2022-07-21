package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.bukkit.interfaces.FListener;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.state.ArenaState;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;

public class ArenaListenerComponent extends ArenaComponent implements FListener {

    private final AccountManager accountManager;

    public ArenaListenerComponent(Splatoon plugin, Arena arena) {
        super(arena);

        this.accountManager = plugin.getAccountManager();

        register(plugin);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) {
            return;
        }

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (!player.getInventory().getItemInMainHand().isSimilar(account.getWeapon().getIcon())) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        account.getWeapon().ability(account, event);
    }

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) {
            return;
        }

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (!player.getInventory().getItemInMainHand().isSimilar(account.getWeapon().getIcon())) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        account.getWeapon().ultimate(account, event);
    }

    @EventHandler
    public void onVoid(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        account.getArena().kill(account);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Account account = accountManager.getAccount(event.getPlayer());

        if (!arena.isAlive(account)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        account.getArena().kill(account);
    }

    @EventHandler
    public void onWalkOnEnemyInk(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        Block block = player.getLocation().subtract(0, 1, 0).getBlock();
        ArenaTeam team = arena.getBlocks().getTeamByBlock(block);

        if (team == null || team == account.getTeam()) {
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false));
    }

    @EventHandler
    public void onSquidMove(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!account.isSquid()) {
            return;
        }

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        Block block = player.getLocation().subtract(0, 1, 0).getBlock();
        ArenaTeam team = arena.getBlocks().getTeamByBlock(block);

        if (team == null || team != account.getTeam()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 2, false, false, false));
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10, 3, false, false, false));
    }

    @EventHandler
    public void onSquidClimbWall(PlayerMoveEvent event) {
        if (!event.hasChangedPosition()) {
            return;
        }

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!account.isSquid()) {
            return;
        }

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        if (!isNearTeamBlock(account)) {
            return;
        }

        if (!player.getEyeLocation().add(0, 1, 0).getBlock().isEmpty()) {
            return;
        }

        Vector velocity = player.getVelocity();

        if (velocity.getY() < 0) {
            velocity.setY(velocity.getY() + .10);
            player.setFallDistance(0);
        } else {
            velocity.setY(.35);
        }

        player.setVelocity(velocity);
    }

    @EventHandler
    public void onSquidMode(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        if (event.getNewSlot() != 1) {
            account.removeSquid();
            return;
        }

        if (account.isSquid()) {
            return;
        }

        Squid squid = player.getWorld().spawn(player.getLocation(), Squid.class);
        squid.setInvulnerable(true);
        squid.setCustomName(account.getDisplayName());
        squid.setCustomNameVisible(true);
        squid.setSilent(true);
        squid.setCollidable(false);

        account.setSquid(squid);

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false));
        player.setCollidable(false);
        account.hide();
    }

    @EventHandler
    public void onSquidModeMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player);

        if (!arena.isAlive(account)) {
            return;
        }

        if (arena.getState() != ArenaState.PLAYING) {
            return;
        }

        if (!account.isSquid()) {
            return;
        }

        Squid squid = account.getSquid();
        squid.teleport(player.getLocation());
    }

    private boolean isNearTeamBlock(Account account) {
        ArenaBlockComponent blocks = arena.getBlocks();

        Location location = account.getPlayer().getLocation();
        int y = location.getBlockY();
        double x = location.getX();
        double z = location.getZ();

        World world = account.getPlayer().getWorld();
        Block b1 = world.getBlockAt(new Location(world, x + .5, y, z));
        Block b2 = world.getBlockAt(new Location(world, x - .5, y, z));
        Block b3 = world.getBlockAt(new Location(world, x, y, z + .5));
        Block b4 = world.getBlockAt(new Location(world, x, y, z - .5));

        return (blocks.getTeamByBlock(b1) == account.getTeam()) || (blocks.getTeamByBlock(b2) == account.getTeam()) || (blocks.getTeamByBlock(b3) == account.getTeam()) || (blocks.getTeamByBlock(b4) == account.getTeam());
    }

}

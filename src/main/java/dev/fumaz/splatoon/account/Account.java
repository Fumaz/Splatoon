package dev.fumaz.splatoon.account;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import dev.fumaz.splatoon.scoreboard.SplatoonScoreboard;
import dev.fumaz.splatoon.weapon.Weapon;
import dev.fumaz.splatoon.weapon.types.Shooter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Account {

    private final Player player;
    private final SplatoonScoreboard scoreboard;

    private Arena arena;
    private Weapon weapon;
    private ArenaTeam team;

    private boolean hidden = false;
    private boolean canMove = true;

    public Account(Player player) {
        this.player = player;
        this.weapon = JavaPlugin.getPlugin(Splatoon.class).getWeaponManager().get(Shooter.class);
        this.scoreboard = new SplatoonScoreboard(this);
    }

    public String getName() {
        return player.getName();
    }

    public String getDisplayName() {
        return getName(); // TODO: Change to display name
    }

    public void clear() {
        getInventory().clear();
        getPlayer().getActivePotionEffects().forEach(potionEffect -> getPlayer().removePotionEffect(potionEffect.getType()));
        getPlayer().clearTitle();
        getPlayer().setExp(0);
        getPlayer().setLevel(0);
        getPlayer().setInvisible(false);
        getPlayer().setCollidable(true);
        show();
    }

    public void teleport(Location location) {
        player.teleport(location);
    }

    public World getWorld() {
        return player.getWorld();
    }

    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    public Player getPlayer() {
        return player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void sendTitle(String title, String subtitle) {
        player.sendTitle(title, subtitle);
    }

    public void sendActionBar(String message) {
        player.sendActionBar(message);
    }

    public ArenaTeam getTeam() {
        return team;
    }

    public void setTeam(ArenaTeam team) {
        this.team = team;
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public SplatoonScoreboard getScoreboard() {
        return scoreboard;
    }

    public void updateScoreboard() {
        scoreboard.update();
    }

    public void setScoreboardType(ScoreboardType type) {
        scoreboard.setType(type);
    }

    public void hide() {
        hidden = true;
    }

    public void show() {
        hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

}

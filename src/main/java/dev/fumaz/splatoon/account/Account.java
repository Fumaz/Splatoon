package dev.fumaz.splatoon.account;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import dev.fumaz.splatoon.scoreboard.ScoreboardType;
import dev.fumaz.splatoon.scoreboard.SplatoonScoreboard;
import dev.fumaz.splatoon.weapon.Weapon;
import dev.fumaz.splatoon.weapon.types.Shooter;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class Account {

    private final Player player;
    private final SplatoonScoreboard scoreboard;

    private Arena arena;
    private Weapon weapon;
    private ArenaTeam team;

    private boolean hidden = false;
    private boolean canMove = true;
    private Squid squid = null;

    private int ink = 100;

    public Account(Player player) {
        this.player = player;
        this.weapon = JavaPlugin.getPlugin(Splatoon.class).getWeaponManager().get(Shooter.class);
        this.scoreboard = new SplatoonScoreboard(this);
    }

    public String getName() {
        return player.getName();
    }

    public String getDisplayName() {
        if (team == null) {
            return getName();
        }

        return team.getColor().getChatColor() + getName();
    }

    public void updateName() {
        player.setDisplayName(getDisplayName());
        player.setPlayerListName(getDisplayName());

        JavaPlugin.getPlugin(Splatoon.class).getAccountManager().getAccounts().forEach(account -> {
            Scoreboard scoreboard = account.getScoreboard().getScoreboard();
            Team scoreboardTeam = scoreboard.getTeam(team != null ? team.getName() : getName());

            if (scoreboardTeam == null) {
                scoreboardTeam = scoreboard.registerNewTeam(team != null ? team.getName() : getName());
                scoreboardTeam.setPrefix(team != null ? team.getColor().getChatColor().toString() : ChatColor.WHITE.toString());
                scoreboardTeam.setColor(team != null ? team.getColor().getChatColor() : ChatColor.WHITE);
            }

            if (scoreboardTeam.hasPlayer(player)) {
                return;
            }

            scoreboardTeam.addPlayer(player);
        });
    }

    public void clear() {
        getInventory().clear();
        getPlayer().getActivePotionEffects().forEach(potionEffect -> getPlayer().removePotionEffect(potionEffect.getType()));
        getPlayer().clearTitle();
        getPlayer().setExp(0);
        getPlayer().setLevel(0);
        getPlayer().setInvisible(false);
        getPlayer().setCollidable(true);
        setInk(100);
        removeSquid();
        show();
        getPlayer().setHealth(getPlayer().getMaxHealth());
        getPlayer().setFoodLevel(20);
        getPlayer().setGlowing(false);

        updateName();
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

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public UUID getUUID() {
        return player.getUniqueId();
    }

    public int getInk() {
        return ink;
    }

    public void setInk(int ink) {
        this.ink = ink;
    }

    public boolean hasInk(int amount) {
        return ink >= amount;
    }

    public void useInk(int amount) {
        ink -= amount;
    }

    public void rechargeInk(int amount) {
        if (ink >= 100) {
            return;
        }

        ink += amount;
    }

    public boolean isSquid() {
        return squid != null;
    }

    public Squid getSquid() {
        return squid;
    }

    public void setSquid(Squid squid) {
        this.squid = squid;
    }

    public void removeSquid() {
        if (squid == null) {
            return;
        }

        squid.remove();
        show();
        player.setInvisible(false);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.setCollidable(true);
        squid = null;
    }

}

package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.bukkit.math.Geometry;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArenaBlockComponent extends ArenaComponent {

    private final AccountManager accountManager;
    private final Map<Block, ArenaTeam> blocks;

    public ArenaBlockComponent(Arena arena, AccountManager accountManager) {
        super(arena);

        this.accountManager = accountManager;
        this.blocks = new HashMap<>();
    }

    public void add(ArenaTeam team, Block block) {
        if (block.getType() == Material.BARRIER || block.isLiquid() || block.isEmpty() || arena.getMap().isUnpaintable(block.getType())) {
            return;
        }

        if (arena.getMap().getMinY() >= block.getY()) {
            return;
        }

        block.setType(team.getColor().getMaterial());
        blocks.put(block, team);
    }

    public void splat(Account account, Location location, int radius, double damage) {
        ArenaTeam team = arena.getTeams().getTeam(account);

        Geometry.sphere(radius, vector -> {
            Block block = location.clone().add(vector).getBlock();
            add(team, block);
        });

        location.getNearbyEntitiesByType(Player.class, radius).forEach(player -> {
            Account target = accountManager.getAccount(player);

            if (team.contains(target) || (target.isHidden() && !target.isSquid())) {
                return;
            }

            if (arena.getTeams().getTeamSpawnLocation(arena.getTeams().getTeam(target)).distanceSquared(player.getLocation()) <= 5 * 5) {
                return;
            }

            player.setKiller(account.getPlayer());
            player.damage(damage);

            if (target.isSquid()) {
                target.getSquid().playEffect(EntityEffect.HURT);
            }
        });
    }

    public ArenaTeam getTeamByBlock(Block block) {
        return blocks.get(block);
    }

}

package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.bukkit.math.Geometry;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.team.ArenaTeam;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArenaBlockComponent extends ArenaComponent{

    private final AccountManager accountManager;
    private final Map<Block, ArenaTeam> blocks;

    public ArenaBlockComponent(Arena arena, AccountManager accountManager) {
        super(arena);

        this.accountManager = accountManager;
        this.blocks = new HashMap<>();
    }

    public void add(ArenaTeam team, Block block) {
        if (block.getType() == Material.BARRIER || block.isEmpty()) {
            return;
        }

        block.setType(team.getColor().getMaterial());
        blocks.put(block, team);
    }

    public void splat(ArenaTeam team, Location location, int radius, double damage) {
        Geometry.circle(radius, vector -> {
            for (int y = -radius; y <= radius; ++y) {
                Block block = location.clone().add(vector).add(0, y, 0).getBlock();
                add(team, block);
            }
        });

        location.getNearbyEntitiesByType(Player.class, radius).forEach(player -> {
            Account account = accountManager.getAccount(player);

            if (team.contains(account) || account.isHidden()) {
                return;
            }

            player.damage(damage);
        });
    }

    public ArenaTeam getTeamByBlock(Block block) {
        return blocks.get(block);
    }

}

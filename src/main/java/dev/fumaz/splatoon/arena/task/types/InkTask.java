package dev.fumaz.splatoon.arena.task.types;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.task.ArenaTask;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InkTask extends ArenaTask {

    protected InkTask(Splatoon plugin, Arena arena) {
        super(plugin, arena);
    }

    @Override
    public void tick(int ticks) {
        arena.getPlayers().forEach(account -> {
            if (!account.isSquid()) {
                account.getPlayer().setExp(Math.min(1f, account.getInk() / 100f));
                return;
            }

            if (account.getPlayer().getLocation().subtract(0, 1, 0).getBlock().getType() != account.getTeam().getColor().getMaterial()) {
                account.getPlayer().setExp(Math.min(1f, account.getInk() / 100f));
                return;
            }

            account.rechargeInk(3);
            account.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10, 3, false, false, false));
            account.getPlayer().setExp(Math.min(1f, account.getInk() / 100f));
        });
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public boolean shouldStartAutomatically() {
        return false;
    }

}

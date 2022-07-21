package dev.fumaz.splatoon.arena.task.types;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.task.ArenaTask;

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

            account.rechargeInk(1);
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

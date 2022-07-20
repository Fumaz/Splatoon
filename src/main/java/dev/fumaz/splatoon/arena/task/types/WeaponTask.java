package dev.fumaz.splatoon.arena.task.types;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.task.ArenaTask;

public class WeaponTask extends ArenaTask {

    public WeaponTask(Splatoon plugin, Arena arena) {
        super(plugin, arena);
    }

    @Override
    public void tick(int ticks) {
        arena.getPlayers().forEach(account -> account.getWeapon().tick(account, ticks));
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

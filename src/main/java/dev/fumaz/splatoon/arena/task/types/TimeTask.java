package dev.fumaz.splatoon.arena.task.types;

import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.state.ArenaState;
import dev.fumaz.splatoon.arena.task.ArenaTask;

public class TimeTask extends ArenaTask {

    public TimeTask(Splatoon plugin, Arena arena) {
        super(plugin, arena);
    }

    @Override
    public void tick(int ticks) {
        if (arena.getState() == ArenaState.WAITING) {
            if (arena.getPlayers().size() < 2) {
                arena.getTime().resetWaitingTime();
                return;
            }

            arena.getTime().decrementWaitingTime();

            if (arena.getTime().getWaitingTime() == 0) {
                arena.start();
            }

            arena.getAccounts().forEach(Account::updateScoreboard);
            return;
        }

        if (arena.getState() == ArenaState.PLAYING) {
            arena.getTime().incrementGameTime();
            arena.getBossBar().update();
            arena.getAccounts().forEach(Account::updateScoreboard);

            if (arena.getTime().getTimeLeft() <= 0) {
                arena.win();
            }
        }
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public int getDelay() {
        return 20;
    }

    @Override
    public boolean shouldStartAutomatically() {
        return true;
    }

}

package dev.fumaz.splatoon.arena.component;

import dev.fumaz.splatoon.arena.Arena;

public class ArenaTimeComponent extends ArenaComponent{

    public final static int MATCH_TIME = 3 * 60;

    private int waitingTime;
    private int gameTime;

    public ArenaTimeComponent(Arena arena) {
        super(arena);

        this.gameTime = 0;
        this.waitingTime = 60;
    }

    public void incrementGameTime() {
        if (gameTime >= MATCH_TIME) {
            return;
        }

        gameTime++;
    }

    public void decrementWaitingTime() {
        waitingTime--;
    }

    public void resetWaitingTime() {
        waitingTime = 60;
    }

    public int getGameTime() {
        return gameTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public boolean isMatchTime() {
        return gameTime >= MATCH_TIME;
    }

    public int getTimeLeft() {
        return MATCH_TIME - gameTime;
    }

}

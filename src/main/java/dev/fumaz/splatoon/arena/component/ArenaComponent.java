package dev.fumaz.splatoon.arena.component;

import dev.fumaz.splatoon.arena.Arena;

public abstract class ArenaComponent {

    protected final Arena arena;

    protected ArenaComponent(Arena arena) {
        this.arena = arena;
    }

    public Arena getArena() {
        return arena;
    }

}

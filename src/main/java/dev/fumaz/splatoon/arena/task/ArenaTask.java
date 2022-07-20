package dev.fumaz.splatoon.arena.task;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class ArenaTask {

    protected final Splatoon plugin;
    protected final Arena arena;
    private BukkitTask task;
    private int ticks;

    protected ArenaTask(Splatoon plugin, Arena arena) {
        this.plugin = plugin;
        this.arena = arena;
    }

    public abstract void tick(int ticks);

    public abstract boolean isAsync();

    public abstract int getDelay();

    public abstract boolean shouldStartAutomatically();

    public void start() {
        if (task != null) {
            return;
        }

        if (!isAsync()) {
            task = Scheduler.of(plugin).runTaskTimer(() -> {
                ticks++;
                tick(ticks);
            }, getDelay(), getDelay());
        } else {
            task = Scheduler.of(plugin).runTaskTimerAsynchronously(() -> {
                ticks++;
                tick(ticks);
            }, getDelay(), getDelay());
        }
    }

    public void stop() {
        task.cancel();
        task = null;
    }

}

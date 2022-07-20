package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.reflection.Reflections;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.task.ArenaTask;

import java.util.HashSet;
import java.util.Set;

public class ArenaTaskComponent extends ArenaComponent {

    private final Splatoon plugin;

    private final Set<Class<? extends ArenaTask>> tasks;
    private final Set<ArenaTask> running;

    public ArenaTaskComponent(Splatoon plugin, Arena arena) {
        super(arena);

        this.plugin = plugin;
        this.tasks = Reflections.getConcreteClassesInPackage(plugin.getClass().getClassLoader(), "dev.fumaz.splatoon.arena.task.types", ArenaTask.class, true);
        this.running = new HashSet<>();
    }

    public void startTasks() {
        tasks.forEach(clazz -> {
            ArenaTask task = Reflections.construct(clazz, plugin, arena);

            if (!task.shouldStartAutomatically()) {
                return;
            }

            running.add(task);
            task.start();
        });
    }

    public void stopTasks() {
        running.forEach(ArenaTask::stop);
        running.clear();
    }

    public void startTask(Class<? extends ArenaTask> clazz) {
        ArenaTask task = Reflections.construct(clazz, plugin, arena);
        running.add(task);
        task.start();
    }

    public void stopTask(Class<? extends ArenaTask> clazz) {
        running.stream()
                .filter(t -> t.getClass().equals(clazz))
                .findFirst()
                .ifPresent(task -> {
                    task.stop();
                    running.remove(task);
                });
    }

}

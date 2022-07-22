package dev.fumaz.splatoon.visibility;

import dev.fumaz.commons.bukkit.misc.Scheduler;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.AccountManager;

public class VisibilityManager {

    private final Splatoon plugin;
    private final AccountManager accountManager;

    public VisibilityManager(Splatoon plugin, AccountManager accountManager) {
        this.plugin = plugin;
        this.accountManager = accountManager;

        Scheduler.of(plugin).runTaskTimer(this::update, 0, 5);
    }

    private void update() {
        accountManager.getAccounts().forEach(victim -> {
            accountManager.getAccounts().forEach(target -> {
                if (victim.getArena() != target.getArena()) {
                    if (target.getPlayer().canSee(victim.getPlayer())) {
                        target.getPlayer().hidePlayer(victim.getPlayer());
                    }
                } else {
                    if (!target.getPlayer().canSee(victim.getPlayer())) {
                        target.getPlayer().showPlayer(victim.getPlayer());
                    }
                }

                boolean hidden = victim.isHidden();

                if (hidden) {
                    if (target.getPlayer().canSee(victim.getPlayer())) {
                        target.getPlayer().hidePlayer(plugin, victim.getPlayer());
                    }
                } else if (!target.getPlayer().canSee(victim.getPlayer())) {
                    target.getPlayer().showPlayer(plugin, victim.getPlayer());
                }
            });
        });
    }

}

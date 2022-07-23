package dev.fumaz.splatoon.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.ArenaManager;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayCommand implements PlayerCommandExecutor {

    private final AccountManager accountManager;
    private final ArenaManager arenaManager;

    public PlayCommand(AccountManager accountManager, ArenaManager arenaManager) {
        this.accountManager = accountManager;
        this.arenaManager = arenaManager;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Account account = accountManager.getAccount(player);

        if (strings.length > 0) {
            String map = strings[0];
            Arena arena = arenaManager.createArena(map);
            arena.join(account);
            return;
        }

        arenaManager.join(account);
    }

}

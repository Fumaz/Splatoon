package dev.fumaz.splatoon.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
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
        arenaManager.join(account);
    }

}

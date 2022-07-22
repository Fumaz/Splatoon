package dev.fumaz.splatoon.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements PlayerCommandExecutor {

    private final AccountManager accountManager;

    public StopCommand(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Account account = accountManager.getAccount(player);

        if (account.getArena() == null) {
            player.sendMessage(ChatColor.RED + "You are not in an arena!");
            return;
        }

        account.getArena().stop();
    }

}

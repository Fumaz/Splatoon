package dev.fumaz.splatoon.command;

import dev.fumaz.commons.bukkit.command.PlayerCommandExecutor;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.account.AccountManager;
import dev.fumaz.splatoon.arena.Arena;
import dev.fumaz.splatoon.arena.ArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StopCommand implements PlayerCommandExecutor {

    private final AccountManager accountManager;
    private final ArenaManager arenaManager;

    public StopCommand(AccountManager accountManager, ArenaManager arenaManager) {
        this.accountManager = accountManager;
        this.arenaManager = arenaManager;
    }

    @Override
    public void onCommand(@NotNull Player player, @NotNull Command command, @NotNull String[] strings) {
        Account account = accountManager.getAccount(player);

        if (strings.length > 0 && strings[0].equalsIgnoreCase("all")) {
            arenaManager.getArenas().forEach(Arena::stop);
            return;
        }

        if (account.getArena() == null) {
            player.sendMessage(ChatColor.RED + "You are not in an arena!");
            return;
        }

        account.getArena().stop();
    }

}

package dev.fumaz.splatoon.account;

import dev.fumaz.splatoon.Splatoon;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class AccountManager {

    private final Splatoon plugin;
    private final AccountListener listener;
    private final Set<Account> accounts;

    public AccountManager(Splatoon plugin) {
        this.plugin = plugin;
        this.listener = new AccountListener(plugin, this);

        this.accounts = new HashSet<>();
    }

    public void remove(Account account) {
        accounts.remove(account);
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Account getAccount(Player player) {
        return accounts.stream()
                .filter(account -> account.getUUID().equals(player.getUniqueId()))
                .findFirst()
                .orElseGet(() -> createAccount(player));
    }

    private Account createAccount(Player player) {
        Account account = new Account(player);
        accounts.add(account);

        return account;
    }

}

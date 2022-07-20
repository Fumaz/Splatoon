package dev.fumaz.splatoon.arena.team;

import dev.fumaz.splatoon.account.Account;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ArenaTeam {

    private final TeamColor color;
    private final List<Account> accounts;

    public ArenaTeam(TeamColor color) {
        this.color = color;
        this.accounts = new ArrayList<>();
    }

    public String getDisplayName() {
        return color.getChatColor() + color.getName();
    }

    public String getName() {
        return color.getName();
    }

    public TeamColor getColor() {
        return color;
    }

    public void add(Account account) {
        accounts.add(account);
        account.setTeam(this);
    }

    public void remove(Account account) {
        accounts.remove(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public int size() {
        return accounts.size();
    }

    public boolean contains(Account account) {
        return accounts.contains(account);
    }

    public boolean contains(Player player) {
        return accounts.stream()
                .map(Account::getPlayer)
                .anyMatch(player::equals);
    }

    public void broadcast(String message) {
        accounts.forEach(account -> account.sendMessage(message));
    }

}

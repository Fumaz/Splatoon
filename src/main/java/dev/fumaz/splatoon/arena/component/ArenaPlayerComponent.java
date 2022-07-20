package dev.fumaz.splatoon.arena.component;

import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class ArenaPlayerComponent extends ArenaComponent {

    private final Set<Account> players;

    public ArenaPlayerComponent(Arena arena) {
        super(arena);

        this.players = new HashSet<>();
    }

    public Set<Account> getPlayers() {
        return players;
    }

    public void add(Account account) {
        players.add(account);
    }

    public void remove(Account account) {
        players.remove(account);
    }

    public int size() {
        return players.size();
    }

    public void forEach(Consumer<Account> action) {
        players.forEach(action);
    }

    public boolean contains(Account account) {
        return players.contains(account);
    }

}

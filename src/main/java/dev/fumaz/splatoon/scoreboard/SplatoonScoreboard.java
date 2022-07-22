package dev.fumaz.splatoon.scoreboard;

import dev.fumaz.commons.bukkit.scoreboard.FScoreboard;
import dev.fumaz.splatoon.account.Account;
import org.bukkit.ChatColor;

public class SplatoonScoreboard extends FScoreboard {

    private final Account account;
    private ScoreboardType type;

    public SplatoonScoreboard(Account account) {
        this.account = account;

        setTitle(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "SPLATOON");
        setType(ScoreboardType.LOBBY);

        show(account.getPlayer());
    }

    public ScoreboardType getType() {
        return type;
    }

    public void setType(ScoreboardType type) {
        this.type = type;

        clear();
        type.update(this);
        update();
    }

    public Account getAccount() {
        return account;
    }

}

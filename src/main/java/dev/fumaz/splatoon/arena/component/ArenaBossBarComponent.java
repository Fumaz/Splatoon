package dev.fumaz.splatoon.arena.component;

import dev.fumaz.commons.text.TimeFormatting;
import dev.fumaz.splatoon.account.Account;
import dev.fumaz.splatoon.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

public class ArenaBossBarComponent extends ArenaComponent{

    private final BossBar bossBar;

    public ArenaBossBarComponent(Arena arena) {
        super(arena);

        this.bossBar = Bukkit.createBossBar(null, BarColor.PINK, BarStyle.SOLID);
    }

    public void update() {
        int timeLeft = arena.getTime().getTimeLeft();
        bossBar.setTitle(ChatColor.WHITE + "Time Left: " + ChatColor.LIGHT_PURPLE + TimeFormatting.formatSeconds(timeLeft));
        bossBar.setProgress((float) timeLeft / ArenaTimeComponent.MATCH_TIME);
        bossBar.setVisible(true);
    }

    public void add(Account account) {
        bossBar.addPlayer(account.getPlayer());
    }

    public void remove(Account account){
        bossBar.removePlayer(account.getPlayer());
    }

}

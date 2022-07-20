package dev.fumaz.splatoon.arena.team;

import dev.fumaz.commons.text.Strings;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

import java.awt.*;

public enum TeamColor {
    WHITE(ChatColor.WHITE, Material.WHITE_CONCRETE),
    ORANGE(ChatColor.of(Color.ORANGE), Material.ORANGE_CONCRETE),
    MAGENTA(ChatColor.of(Color.MAGENTA), Material.MAGENTA_CONCRETE),
    LIGHT_BLUE(ChatColor.of(Color.BLUE), Material.LIGHT_BLUE_CONCRETE),
    YELLOW(ChatColor.YELLOW, Material.YELLOW_CONCRETE),
    LIME(ChatColor.of(Color.GREEN), Material.LIME_CONCRETE),
    PINK(ChatColor.of(Color.PINK), Material.PINK_CONCRETE);

    private final ChatColor chatColor;
    private final Material material;

    TeamColor(ChatColor chatColor, Material material) {
        this.chatColor = chatColor;
        this.material = material;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return Strings.capitalizeFully(name().replace("_", " "));
    }
}

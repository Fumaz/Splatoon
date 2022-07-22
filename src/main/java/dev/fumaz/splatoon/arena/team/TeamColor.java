package dev.fumaz.splatoon.arena.team;

import dev.fumaz.commons.text.Strings;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.awt.*;

public enum TeamColor {
    // WHITE(ChatColor.WHITE, Material.WHITE_CONCRETE),
    ORANGE(ChatColor.GOLD, Material.ORANGE_CONCRETE),
    PURPLE(ChatColor.DARK_PURPLE, Material.MAGENTA_CONCRETE),
    AQUA(ChatColor.AQUA, Material.LIGHT_BLUE_CONCRETE),
    YELLOW(ChatColor.YELLOW, Material.YELLOW_CONCRETE),
    LIME(ChatColor.GREEN, Material.LIME_CONCRETE),
    PINK(ChatColor.LIGHT_PURPLE, Material.PINK_CONCRETE);

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

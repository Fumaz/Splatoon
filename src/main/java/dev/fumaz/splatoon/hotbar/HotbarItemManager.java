package dev.fumaz.splatoon.hotbar;

import dev.fumaz.commons.bukkit.item.ItemBuilder;
import dev.fumaz.splatoon.Splatoon;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HotbarItemManager {

    private final HotbarItemListener listener;
    private final Map<HotbarItem, HotbarItemCategory> items;

    public HotbarItemManager(Splatoon plugin) {
        this.listener = new HotbarItemListener(plugin, this);
        this.items = new HashMap<>();

        addHotbarItem(new HotbarItem(ItemBuilder.of(Material.INK_SAC)
                .displayName(ChatColor.AQUA + "" + ChatColor.BOLD + "SQUID MODE")
                .build(), 1, (account, event) -> {}), HotbarItemCategory.PLAYING);
    }

    public void addHotbarItem(HotbarItem item, HotbarItemCategory category) {
        items.put(item, category);
    }

    public HotbarItem getHotbarItem(ItemStack item) {
        return items.keySet()
                .stream()
                .filter(hotbar -> hotbar.getItem().isSimilar(item))
                .findFirst()
                .orElse(null);
    }

    public List<HotbarItem> getHotbarItems(HotbarItemCategory... categories) {
        return items.entrySet()
                .stream()
                .filter(entry -> Arrays.asList(categories).contains(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}

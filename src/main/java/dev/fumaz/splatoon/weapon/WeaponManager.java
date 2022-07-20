package dev.fumaz.splatoon.weapon;

import dev.fumaz.commons.bukkit.gui.Gui;
import dev.fumaz.commons.bukkit.gui.item.ClickableGuiItem;
import dev.fumaz.commons.bukkit.gui.item.GuiItemBuilder;
import dev.fumaz.commons.bukkit.gui.page.ListPage;
import dev.fumaz.commons.bukkit.misc.Logging;
import dev.fumaz.commons.reflection.Reflections;
import dev.fumaz.splatoon.Splatoon;
import dev.fumaz.splatoon.account.Account;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class WeaponManager {

    private final Splatoon plugin;
    private final Logger logger;
    private final Set<Weapon> weapons;

    public WeaponManager(Splatoon plugin) {
        this.plugin = plugin;
        this.logger = Logging.of(plugin);
        this.weapons = new HashSet<>();

        load();
    }

    public Weapon get(Class<? extends Weapon> clazz) {
        return weapons.stream()
                .filter(weapon -> weapon.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    public void showGUI(Account account) {
        Gui gui = new Gui(plugin, "Weapons", 3);
        ListPage page = new ListPage(gui);

        weapons.forEach(weapon -> {
            page.addItem(new GuiItemBuilder()
                    .item(weapon.getIcon())
                    .onClick(event -> {
                        account.setWeapon(weapon);
                    })
                    .build());
        });

        gui.setPage(page);
        gui.show(account.getPlayer());
    }
    private void load() {
        logger.info("Loading weapons...");

        Reflections.consumeInstance(Splatoon.class.getClassLoader(), "dev.fumaz.splatoon.weapon.types", Weapon.class, true, weapons::add, plugin);

        logger.info("Loaded " + weapons.size() + " weapons.");
    }

}

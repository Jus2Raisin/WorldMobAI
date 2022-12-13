package fr.jus2raisin.enumerations;

import fr.jus2raisin.WorldMobAIPlugin;

import java.util.List;

public enum Configuration {

    MENU_NAME("menu.name"),
    ITEM_LORE("menu.lore");

    final String path;

    Configuration(String path) {
        this.path = path;
    }

    public List<String> getStringList() {
        return WorldMobAIPlugin.INSTANCE.getConfig().getStringList(path);
    }
    public String getString() {
        return WorldMobAIPlugin.INSTANCE.getConfig().getString(path);
    }

    public Integer getInt() {
        return Integer.parseInt(getString());
    }
}

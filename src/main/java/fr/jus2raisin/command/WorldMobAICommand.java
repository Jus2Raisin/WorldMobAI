package fr.jus2raisin.command;

import fr.jus2raisin.WorldMobAIPlugin;
import fr.jus2raisin.enumerations.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class WorldMobAICommand implements CommandExecutor {

    WorldMobAIPlugin worldMobAIPlugin;

    public WorldMobAICommand(WorldMobAIPlugin worldMobAIPlugin) {
        this.worldMobAIPlugin = worldMobAIPlugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("§cVous devez être un joueur.");
            return false;
        }
        ((Player)commandSender).openInventory(this.getInventory());
        commandSender.sendMessage("§aVous avez ouvert WorldMobAI !");
        return true;
    }

    Inventory getInventory() {
        final Inventory inventory = Bukkit.createInventory(null, 54, Configuration.MENU_NAME.getString());
        this.worldMobAIPlugin.getWorldAIMap().forEach((worldName, status) -> inventory.addItem(getItemStack(worldName, status)));
        return inventory;
    }


    ItemStack getItemStack(String worldName, boolean status) {
        final ItemStack itemStack = new ItemStack(Material.NETHER_STAR);
        final ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(worldName);
        itemMeta.setLore(Collections.singletonList(Configuration.ITEM_LORE.getString().replace("{status}", ((status) == Boolean.TRUE ? "§aActivé" : "§cDésactivé"))));
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}

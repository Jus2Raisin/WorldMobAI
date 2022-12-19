package fr.jus2raisin.listeners;

import fr.jus2raisin.WorldMobAIPlugin;
import fr.jus2raisin.enumerations.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class WorldMobAIListener implements Listener {

    WorldMobAIPlugin worldMobAIPlugin;

    public WorldMobAIListener(WorldMobAIPlugin worldMobAIPlugin) {
        this.worldMobAIPlugin = worldMobAIPlugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreateWorld(WorldLoadEvent event) {
        if (!this.worldMobAIPlugin.getWorldAIMap().containsKey(event.getWorld().getName())) this.worldMobAIPlugin.getWorldAIMap().put(event.getWorld().getName(), Boolean.TRUE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        if (!this.worldMobAIPlugin.getWorldAIMap().get(event.getEntity().getWorld().getName())) ((Monster) event.getEntity()).setTarget(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!this.worldMobAIPlugin.getWorldAIMap().get(event.getEntity().getWorld().getName())) event.setCancelled(true);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equalsIgnoreCase(Configuration.MENU_NAME.getString())) {
            final ItemStack itemStack = event.getCurrentItem();

            if (itemStack == null || itemStack.getType() == Material.AIR) return;
            if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) return;

            event.setCancelled(true);

            final String worldName = itemStack.getItemMeta().getDisplayName();
            final Boolean status = this.worldMobAIPlugin.getWorldAIMap().get(worldName);

            this.worldMobAIPlugin.getWorldAIMap().replace(worldName, !status);
            event.getInventory().setItem(event.getSlot(), this.changeStatus(event.getCurrentItem(), !status));

            Bukkit.getWorld(itemStack.getItemMeta().getDisplayName()).getEntitiesByClasses(Spider.class, Skeleton.class, Zombie.class, Creeper.class, PigZombie.class, Enderman.class, Slime.class, Blaze.class, Ghast.class, Endermite.class).forEach(entity -> ((Monster)entity).setTarget(null));

        }
    }

    private ItemStack changeStatus(ItemStack itemStack, Boolean status) {
        final ItemStack item = itemStack.clone();
        final ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setLore(Collections.singletonList(Configuration.ITEM_LORE.getString().replace("{status}", ((status) == Boolean.TRUE ? "§aActivé" : "§cDésactivé"))));
        item.setItemMeta(itemMeta);

        return item;
    }

}

package fr.jus2raisin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.jus2raisin.command.WorldMobAICommand;
import fr.jus2raisin.listeners.WorldMobAIListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;

public final class WorldMobAIPlugin extends JavaPlugin {

    public static WorldMobAIPlugin INSTANCE;
    HashMap<String, Boolean> worldAIMap;
    Gson gson;

    @Override
    public void onEnable() {

        getConfig().options().copyDefaults(true);
        saveConfig();

        this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        (INSTANCE = this).getCommand("worldmobai").setExecutor(new WorldMobAICommand(this));
        this.worldAIMap = load();
        getServer().getPluginManager().registerEvents(new WorldMobAIListener(this),this);
        getServer().getConsoleSender().sendMessage("§6§lWorldMobAI §f- §7Plugin now §a§lON");
    }

    HashMap<String, Boolean> load() {
        final File file = new File(this.getDataFolder().getAbsolutePath(), "worlds.json");
        HashMap<String, Boolean> worldsIA = new HashMap<>();
        if (file.exists()) {
            try {
                worldsIA = gson.fromJson(new FileReader(file), new TypeToken<HashMap<String, Boolean>>(){}.getType());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            HashMap<String, Boolean> finalWorldsIA = worldsIA;
            Bukkit.getWorlds().forEach(world -> finalWorldsIA.put(world.getName(), Boolean.TRUE));
        }
        return worldsIA;
    }

    public HashMap<String, Boolean> getWorldAIMap() {
        return worldAIMap;
    }

    @Override
    public void onDisable() {
        try {
            final File file = new File(this.getDataFolder().getAbsolutePath(), "worlds.json");
            file.getParentFile().mkdir();
            file.createNewFile();

            final Writer writer = new FileWriter(file, false);
            gson.toJson(this.worldAIMap, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getServer().getConsoleSender().sendMessage("§6§lWorldMobAI §f- §7Plugin now §c§lOFF");
    }
}

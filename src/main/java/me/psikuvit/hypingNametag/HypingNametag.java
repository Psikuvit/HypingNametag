package me.psikuvit.hypingNametag;

import org.bukkit.plugin.java.JavaPlugin;

public final class HypingNametag extends JavaPlugin {

    private static NametagManager nametagManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        nametagManager = new NametagManager(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static NametagManager getNametagManager() {
        return nametagManager;
    }
}

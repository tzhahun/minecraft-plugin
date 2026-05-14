package com.example.cowlauncher;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Entry point: registers cow launch and instant-break as separate features.
 */
public final class CowLauncherPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        InstaBreakState instaBreakState = new InstaBreakState();
        getServer().getPluginManager().registerEvents(new InstaBreakListener(instaBreakState), this);

        Objects.requireNonNull(getCommand("cowlaunch"), "cowlaunch missing from plugin.yml")
                .setExecutor(new CowLaunchCommand());
        Objects.requireNonNull(getCommand("instabreak"), "instabreak missing from plugin.yml")
                .setExecutor(new InstaBreakCommand(instaBreakState));

        getLogger().info("CowLauncher enabled (/cowlaunch, /instabreak).");
    }

    @Override
    public void onDisable() {
        getLogger().info("CowLauncher disabled.");
    }
}

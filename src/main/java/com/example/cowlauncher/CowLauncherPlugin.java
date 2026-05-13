package com.example.cowlauncher;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class CowLauncherPlugin extends JavaPlugin implements Listener {
    private final Set<UUID> instaBreakEnabled = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("CowLauncher enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("CowLauncher disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }

        String cmd = command.getName().toLowerCase(Locale.ROOT);
        if (cmd.equals("instabreak")) {
            if (!player.hasPermission("cowlauncher.instabreak")) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use this.");
                return true;
            }

            boolean enabled = instaBreakEnabled.contains(player.getUniqueId());
            String mode = (args.length >= 1 ? args[0].toLowerCase(Locale.ROOT) : "toggle");
            switch (mode) {
                case "on", "enable", "true" -> enabled = true;
                case "off", "disable", "false" -> enabled = false;
                case "toggle" -> enabled = !enabled;
                default -> {
                    player.sendMessage(ChatColor.RED + "Usage: /instabreak [on|off|toggle]");
                    return true;
                }
            }

            if (enabled) instaBreakEnabled.add(player.getUniqueId());
            else instaBreakEnabled.remove(player.getUniqueId());

            player.sendMessage(ChatColor.AQUA + "Instant break is now " + (enabled ? "ON" : "OFF") + " (survival only).");
            return true;
        }

        if (!cmd.equals("cowlaunch")) {
            return false;
        }

        if (!player.hasPermission("cowlauncher.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this.");
            return true;
        }

        double radius = 10.0;
        double power = 1.2;

        if (args.length >= 1) {
            radius = parseDoubleOr(args[0], radius);
        }
        if (args.length >= 2) {
            power = parseDoubleOr(args[1], power);
        }

        if (radius < 1) radius = 1;
        if (radius > 64) radius = 64;
        if (power < 0.1) power = 0.1;
        if (power > 5.0) power = 5.0;

        int launched = 0;
        Vector velocity = new Vector(0, power, 0);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof Cow cow) {
                cow.setVelocity(velocity);
                launched++;
            }
        }

        player.sendMessage(ChatColor.GREEN + "Launched " + launched + " cow(s) upward. (radius=" + radius + ", power=" + power + ")");
        return true;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (!instaBreakEnabled.contains(player.getUniqueId())) return;
        if (player.getGameMode() != GameMode.SURVIVAL) return;
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType().isAir()) return;

        event.setInstaBreak(true);
    }

    private static double parseDoubleOr(String raw, double fallback) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
}


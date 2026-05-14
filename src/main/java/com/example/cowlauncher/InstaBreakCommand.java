package com.example.cowlauncher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

final class InstaBreakCommand implements CommandExecutor {
    private final InstaBreakState state;

    InstaBreakCommand(InstaBreakState state) {
        this.state = state;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }

        if (!player.hasPermission("cowlauncher.instabreak")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this.");
            return true;
        }

        boolean enabled = state.isEnabled(player.getUniqueId());
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

        state.setEnabled(player.getUniqueId(), enabled);
        player.sendMessage(ChatColor.AQUA + "Instant break is now " + (enabled ? "ON" : "OFF") + " (survival only).");
        return true;
    }
}

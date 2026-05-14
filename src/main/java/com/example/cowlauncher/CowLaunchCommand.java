package com.example.cowlauncher;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.util.Vector;
import org.bukkit.entity.EntityType;

import java.util.Locale;

/**
 * Launches nearby entities upward. Default: all {@link LivingEntity} except the player.
 * Optional first argument: Minecraft entity type (e.g. cow, pig, zombie) or {@code all}.
 */
final class CowLaunchCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used in-game.");
            return true;
        }

        if (!player.hasPermission("cowlauncher.use")) {
            player.sendMessage(ChatColor.RED + "You don't have permission to use this.");
            return true;
        }

        int index = 0;
        EntityType filter = null;
        boolean filterAllLiving = true;

        if (args.length > 0 && !isNumeric(args[0])) {
            if (args[0].equalsIgnoreCase("all")) {
                filterAllLiving = true;
                filter = null;
            } else {
                filter = parseEntityType(args[0]);
                if (filter == null) {
                    player.sendMessage(ChatColor.RED + "Unknown entity type: " + args[0]
                            + ". Use a type like cow, pig, zombie, or all.");
                    return true;
                }
                filterAllLiving = false;
            }
            index = 1;
        }

        double radius = 10.0;
        double power = 1.2;
        if (args.length > index) {
            radius = parseDoubleOr(args[index], radius);
        }
        if (args.length > index + 1) {
            power = parseDoubleOr(args[index + 1], power);
        }

        if (radius < 1) {
            radius = 1;
        }
        if (radius > 64) {
            radius = 64;
        }
        if (power < 0.1) {
            power = 0.1;
        }
        if (power > 5.0) {
            power = 5.0;
        }

        int launched = 0;
        Vector velocity = new Vector(0, power, 0);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity == player) {
                continue;
            }
            if (!matchesFilter(entity, filter, filterAllLiving)) {
                continue;
            }
            entity.setVelocity(velocity);
            launched++;
        }

        String filterLabel = filterAllLiving ? "living entities" : filter.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        player.sendMessage(ChatColor.GREEN + "Launched " + launched + " " + filterLabel + " upward. (radius=" + radius + ", power=" + power + ")");
        return true;
    }

    private static boolean matchesFilter(Entity entity, EntityType filter, boolean filterAllLiving) {
        if (filterAllLiving) {
            if (entity instanceof LivingEntity) {
                return true;
            }
            return entity instanceof Vehicle;
        }
        return entity.getType() == filter;
    }

    private static EntityType parseEntityType(String raw) {
        String key = raw.toUpperCase(Locale.ROOT).replace(' ', '_');
        try {
            return EntityType.valueOf(key);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static boolean isNumeric(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static double parseDoubleOr(String raw, double fallback) {
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }
}

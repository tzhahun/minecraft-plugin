package com.example.cowlauncher;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

final class InstaBreakListener implements Listener {
    private final InstaBreakState state;

    InstaBreakListener(InstaBreakState state) {
        this.state = state;
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (!state.isEnabled(player.getUniqueId())) {
            return;
        }
        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }

        Block block = event.getBlock();
        if (block.getType().isAir()) {
            return;
        }

        event.setInstaBreak(true);
    }
}

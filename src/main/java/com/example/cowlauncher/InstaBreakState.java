package com.example.cowlauncher;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Holds per-player toggles for instant block breaking. Used only by the instabreak feature.
 */
final class InstaBreakState {
    private final Set<UUID> enabledPlayers = new HashSet<>();

    boolean isEnabled(UUID playerId) {
        return enabledPlayers.contains(playerId);
    }

    void setEnabled(UUID playerId, boolean enabled) {
        if (enabled) {
            enabledPlayers.add(playerId);
        } else {
            enabledPlayers.remove(playerId);
        }
    }

    boolean toggle(UUID playerId) {
        if (enabledPlayers.contains(playerId)) {
            enabledPlayers.remove(playerId);
            return false;
        }
        enabledPlayers.add(playerId);
        return true;
    }
}

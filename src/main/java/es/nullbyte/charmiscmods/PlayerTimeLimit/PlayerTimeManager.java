package es.nullbyte.charmiscmods.PlayerTimeLimit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the playtime of multiple players
 */
public class PlayerTimeManager {
    private final Map<UUID, PlayerTimeTracker> playerMap = new HashMap<>();
    private final int dailyTimeLimit; // The daily time limit in seconds

    public PlayerTimeManager(int dailyTimeLimit) {
        this.dailyTimeLimit = dailyTimeLimit;
    }

    public void addPlayer(UUID playerUUID) {
        playerMap.put(playerUUID, new PlayerTimeTracker());
    }

    public void removePlayer(UUID playerUUID) {
        playerMap.remove(playerUUID);
    }

    public PlayerTimeTracker getTracker(UUID playerUUID) {
        return playerMap.get(playerUUID);
    }

    public void updatePlayerTime(UUID playerUUID) {
        PlayerTimeTracker player = getTracker(playerUUID);
        if (player != null && !player.isCurrentlyTimeOut()) {
            player.addTimePlayed(1);
            if (player.getSecsPlayed() >= dailyTimeLimit) {
                player.setTimeoutState(true);
            }
        }
    }

    public boolean hasPlayer(UUID playerUUID) {
        return playerMap.containsKey(playerUUID);
    }

    public void serializePlayers() {
        // Write player data to file or database
    }

    public void deserializePlayers() {
        // Read player data from file or database
    }
}
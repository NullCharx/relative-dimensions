package es.nullbyte.charmiscmods.PlayerTimeLimit;

import java.time.LocalTime;
import java.util.*;

/**
 * Manages the playtime of multiple players
 */
public class PlayerTimeManager {
    private final Map<UUID, PlayerTimeTracker> playerMap = new HashMap<>();//Hashmap of individual player trackers
    private final int dailyTimeLimit; // The daily time limit in seconds
    private final LocalTime resetTime; // The time of day in which the timers reset


    //Time for reset.

    //Constructor for the time manager. It takes both the daily time limit in seconds and the time of the day in which the timers reset.
    public PlayerTimeManager(int dailyTimeLimit, int resetHour) {
        this.dailyTimeLimit = dailyTimeLimit;
        this.resetTime = LocalTime.of(resetHour, 0);  //Resets at resethour:00
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
    public boolean hasPlayer(UUID playerUUID) {
        return playerMap.containsKey(playerUUID);
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

    public void updatePlayerTime(UUID playerUUID, int seconds) {
        PlayerTimeTracker player = getTracker(playerUUID);
        if (player != null && !player.isCurrentlyTimeOut()) {
            if (seconds > 0) {
                player.addTimePlayed(seconds);
                if (player.getSecsPlayed() >= dailyTimeLimit) {
                    player.setTimeoutState(true);
                }
            } else {
                player.removeTimePlayed(Math.abs(seconds));
                if (player.getSecsPlayed() >= dailyTimeLimit) {
                    player.setTimeoutState(true);
                }

            }

        }
    }

    public boolean isOnTimeout (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null ) {
            return playerTimeTracker.isCurrentlyTimeOut();
        }

        throw new IllegalArgumentException("No player found under specified UUID");
    }

    public void resetPlayerTime (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null ) {
            playerTimeTracker.resetTimePlayed();
            playerTimeTracker.setTimeoutState(false);
        }

    }
    public void resetAllTime () {
        for (PlayerTimeTracker player : playerMap.values()) {
            player.resetTimePlayed();
            player.setTimeoutState(false);
        }
    }

    public void playerLogOn(UUID playerUUID) {
        getTracker(playerUUID).setLastLoginEpoch();
    }
    public long getPlayerLogon(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getLastLoginEpoch();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    public void timeOutPlayer (UUID playerUUID){
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            playerTimeTracker.setTimeoutState(true);
        }
    }
    public int getPlayerTime (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getSecsPlayed();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    public boolean checkForTimeout (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getSecsPlayed() <= dailyTimeLimit;
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    public List<UUID> getAllPlayers() {
        return new ArrayList<>(playerMap.keySet());
    }

    public void serializePlayers() {
        // Write player data to file or database
    }

    public void deserializePlayers() {
        // Read player data from file or database
    }

    public boolean isResetTime() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.equals(resetTime);
    }

}
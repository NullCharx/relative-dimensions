package es.nullbyte.charmiscmods.PlayerTimeLimit;

import java.time.LocalTime;
import java.util.*;

/**
 * Manages the playtime of multiple players
 */
public class PlayerTimeManager {
    private final Map<UUID, PlayerTimeTracker> playerMap = new HashMap<>();//Hashmap of individual player trackers
    private final long dailyTimeLimit; // The daily time limit in seconds
    private final LocalTime resetTime; // The time of day in which the timers reset


    //Time for reset.

    /*
    Constructor for the time manager.
    It takes both the daily time limit in seconds and the time of the day in which the timers reset.
     */
    public PlayerTimeManager(int dailyTimeLimit, int resetHour) {
        this.dailyTimeLimit = dailyTimeLimit;
        this.resetTime = LocalTime.of(resetHour, 0);  //Resets at resethour:00
    }

    /*
    Adds a new player to the tracking system given their Minecraft UUID
     */
    public void addPlayer(UUID playerUUID) {
        playerMap.put(playerUUID, new PlayerTimeTracker());
    }

    /*
    Removes a player from  the tracking system given their Minecraft UUID
   */
    public void removePlayer(UUID playerUUID) {
        playerMap.remove(playerUUID);
    }

    /*
    Gets the individual tracker object of a player given their Minecraft UUID
   */
    public PlayerTimeTracker getTracker(UUID playerUUID) {
        return playerMap.get(playerUUID);
    }
    /*
    Check if the manager currently tracks a player (has an individual tracker with their UUID)
    */
    public boolean hasPlayer(UUID playerUUID) {
        return playerMap.containsKey(playerUUID);
    }

    /*
    Adds one second to a player tracker (unnecessary if the individual player tracker is already available)
    */
    public void updatePlayerTime(UUID playerUUID) {
        PlayerTimeTracker player = getTracker(playerUUID);
        if (player != null && !player.isCurrentlyTimeOut()) {
            player.addTimePlayed(1);
            if (player.getSecsPlayed() >= dailyTimeLimit) {
                player.setTimeoutState(true);
            }
        }
    }

    /*
    Adds or susbtracts the specified seconds to a player tracker (unnecessary if the individual player tracker is already available)
    */
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

    /*
    Check if the specified player has spent all their daily time already
    */
    public boolean isOnTimeout (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null ) {
            return playerTimeTracker.isCurrentlyTimeOut();
        }

        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
    Resets time of a player (un-timeouts them and reset the playtime to 0)
    Banned players must be unbanned outside this class
    */
    public void resetPlayerTime (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null ) {
            playerTimeTracker.resetTimePlayed();
            playerTimeTracker.setTimeoutState(false);
        }

    }

    /*
     Resets time of all tracked players (un-timeouts them and reset the playtime to 0)
     Banned players must be unbanned outside this class
     */
    public void resetAllTime () {
        for (PlayerTimeTracker player : playerMap.values()) {
            player.resetTimePlayed();
            player.setTimeoutState(false);
        }
    }

    /*
     Sets the login time of a player to now
     */
    public void playerLogOn(UUID playerUUID) {
        getTracker(playerUUID).setLastLoginEpoch();
    }

    /*
     Gets the last login time of a player. Works in conjuction with isPlayerOnline.
     */
    public long getPlayerLogon(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getLastLoginEpoch();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
     Internally timesout a player. The player must be banned from the minecraft server outside this class
     */
    public void timeOutPlayer (UUID playerUUID){
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            playerTimeTracker.setTimeoutState(true);
        }
    }

    /*
     gets the current playtime in seconds of a plauyer
     */
    public long getPlayerTime (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getSecsPlayed();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
     Checks if the specified player should be timeouted.
     */
    public boolean checkForTimeout (UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getSecsPlayed() <= dailyTimeLimit || playerTimeTracker.hasTimePlayed(dailyTimeLimit);
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
     Check if the specified player is online
     */
    public boolean isPlayerOnline(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.getPlayerOnlineState();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
     Internally swaps the player online state
     */
    public void setPlayerOnlineState(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            playerTimeTracker.setPlayerOnlineState();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }

    /*
     Check if it is the reset time (between XX:00 and XX:03)
     */
    public boolean isResetTime() {
        LocalTime currentTime = LocalTime.now();
        return currentTime.equals(resetTime) || (currentTime.getHour() == 0 && (currentTime.getMinute() >= 0 && currentTime.getMinute() <= 3));
    }

    /*
     Returns a list of the UUIDS of all tracked players.
     */
    public List<UUID> getAllPlayers() {
        return new ArrayList<>(playerMap.keySet());
    }

    public void serializePlayers() {
        // Write player data to file or database
    }

    public void deserializePlayers() {
        // Read player data from file or database
    }


}
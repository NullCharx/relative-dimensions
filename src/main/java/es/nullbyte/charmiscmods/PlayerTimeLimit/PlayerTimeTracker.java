package es.nullbyte.charmiscmods.PlayerTimeLimit;

import java.util.UUID;

/**
 * Track the time played by a player. THe identifying UUID is in the PlayerTimeManer hashmap of players
 *
 */
public class PlayerTimeTracker {

    private int secsPlayed; //Time played in seconds for the current day (Holds between sessions)
    private boolean isCurrentlyTimeOut; //Is the player currently timed out? (i.e they reached the daily time limit)
    //date and time of the last login
    private long lastLoginEpoch;  //The epoch time of the last login
    private boolean isCurrentlyPlaying;
    public PlayerTimeTracker() {
        this.secsPlayed = 0;
        this.isCurrentlyPlaying = false;
        this.isCurrentlyTimeOut = false;
        this.lastLoginEpoch = 0;
    }


    public long getLastLoginEpoch() {
        return lastLoginEpoch;
    }

    public void setLastLoginEpoch() {
        this.lastLoginEpoch = System.currentTimeMillis();
    }
    public boolean isCurrentlyTimeOut() {
        return isCurrentlyTimeOut;
    }

    public void setTimeoutState (boolean isCurrentlyTimeOut) {
        this.isCurrentlyTimeOut = isCurrentlyTimeOut;
    }

    public long getSecsPlayed() {
        return secsPlayed;
    }

    public void setSecsPlayed(int secsPlayed) {
        this.secsPlayed = secsPlayed;
    }

    public void resetTimePlayed() {
        this.secsPlayed = 0;
    }

    public void addTimePlayed(int timePlayed) {
        this.secsPlayed += timePlayed;
    }

    public void removeTimePlayed(int timePlayed) {
        this.secsPlayed -= timePlayed;
    }

    public boolean hasTimePlayed() {
        return this.secsPlayed > 0;
    }

    public boolean hasTimePlayed(int timePlayed) {
        return this.secsPlayed >= timePlayed;
    }

}

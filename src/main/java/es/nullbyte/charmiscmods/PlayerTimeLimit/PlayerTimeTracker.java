package es.nullbyte.charmiscmods.PlayerTimeLimit;

import java.util.UUID;

public class PlayerTimeTracker {

    private final UUID playerUUID;
    private int secsPlayed;

    public PlayerTimeTracker(UUID playerUUID, int secsPlayed) {
        this.playerUUID = playerUUID;
        this.secsPlayed = secsPlayed;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
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

    public void removeTimePlayed() {
        this.secsPlayed--;
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

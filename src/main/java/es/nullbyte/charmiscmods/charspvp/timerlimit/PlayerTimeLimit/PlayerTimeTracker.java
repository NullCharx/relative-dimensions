package es.nullbyte.charmiscmods.charspvp.timerlimit.PlayerTimeLimit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;


/**
 * Track the time played by a player. THe identifying UUID is in the PlayerTimeManer hashmap of players
 */
public class PlayerTimeTracker {

    private static final Logger LOGGER = LogUtils.getLogger();
    private long secsPlayed; //Time played in seconds for the current day (Holds between sessions)
    private boolean isCurrentlyTimeOut; //Is the player currently timed out? (i.e they reached the daily time limit)
    //date and time of the last login
    public PlayerTimeTracker() {
        this.secsPlayed = 0;
        this.isCurrentlyTimeOut = false;
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

    public void resetTimePlayed() {
        this.secsPlayed = 0;
    }

    public void setTimePlayed(long timePlayed) {
        this.secsPlayed = timePlayed;
    }
    public void addTimePlayed(long timePlayed) {
        this.secsPlayed += timePlayed;
    }

    public void removeTimePlayed(long timePlayed) {
        this.secsPlayed -= timePlayed;
    }

    public boolean hasTimePlayed(long timePlayed) {
        return this.secsPlayed >= timePlayed;
    }

    // Serialize the PlayerTimeData to a JSON string
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    // Deserialize a PlayerTimeData object from a JSON string
    public static PlayerTimeTracker fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, PlayerTimeTracker.class);
    }

    // Write PlayerTimeData for a player to a JSON file
    public void saveToFile(UUID playerUUID) throws IOException {
        Path directoryPath = Paths.get(".", "charmscmods", "playtimelimiter", "playerdata");
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        Path filePath = Paths.get(".", "charmscmods", "playtimelimiter", "playerdata", playerUUID + "_timerData.json");

        File file = new File("./charmscmods/playtimelimiter/playerdata/" + playerUUID + "_timerData.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.error("Error while creating new file: " + e.getMessage());
            }
        }

        try {
            Files.write(filePath, this.toJson().getBytes());
        } catch (NoSuchFileException e) {
            Files.createFile(filePath);
            Files.write(filePath, this.toJson().getBytes());
        }
    }




}

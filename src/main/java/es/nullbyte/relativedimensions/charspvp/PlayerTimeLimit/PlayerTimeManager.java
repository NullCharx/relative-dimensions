package es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.ancillar.LocalDateTimeAdapter;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.mgrcmds.PvpDamageGameRule;
import es.nullbyte.relativedimensions.charspvp.network.DailyTimeLimitHandler;
import es.nullbyte.relativedimensions.charspvp.network.RemainingTimeHandler;
import es.nullbyte.relativedimensions.charspvp.network.packet.S2CRemainingTime;
import es.nullbyte.relativedimensions.charspvp.network.packet.S2CDailyTimeLimit;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages the playtime of multiple players
 */
public class PlayerTimeManager {

    private static final Map<UUID, PlayerTimeTracker> playerMap = new HashMap<>();//Hashmap of individual player trackers
    private static long dailyTimeLimit; // The daily time limit in seconds
    private static LocalDateTime resetTime; // The time of day in which the timers reset

    private static boolean isEnabled;
    private static final Logger LOGGER = LogUtils.getLogger();

    public PlayerTimeManager() {

    }
    //Thread executor for periodic backup
    private static final ScheduledExecutorService backupExecutor = Executors.newSingleThreadScheduledExecutor();

    //Time for reset.

    /*
    Constructor for the time manager.
    It takes both the daily time limit in seconds and the time of the day in which the timers reset.
     */

    public PlayerTimeManager(int dailyTimeLimit, int resetHour) {
        PlayerTimeManager.dailyTimeLimit = dailyTimeLimit;
        //Resets at resethour:00
        LocalTime time = LocalTime.of(resetHour, 33);
        resetTime = LocalDateTime.of(LocalDate.now(), time);
        isEnabled = false;
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called

        //Add listeners for the events we want to listen to. Since this is not an item or blocck, that are managed in
        //The main class, we need to add the listeners here
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onPlayerLoggedOut);
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onServerTick);
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onPlayerRespawn);
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onServerStopping);
        MinecraftForge.EVENT_BUS.addListener(PlayerTimeManager::onPlayerDeath);

        // Schedule the periodic backup task
        backupExecutor.scheduleAtFixedRate(this::backupAllPlayerData, 1, 15, TimeUnit.MINUTES);
    }

    protected static LocalDateTime getResetTime() {
        return resetTime;
    }

    protected static void setResetTime(int hour, int min) {
        LocalTime time = LocalTime.of(hour, min);
        LocalDateTime timeDate = LocalDateTime.of(LocalDate.now(), time);
        if (time.isBefore(LocalTime.now()) && timeDate.isBefore(LocalDateTime.now().plusDays(1))) {
            timeDate = timeDate.plusDays(1);
        }
        resetTime = timeDate;
    }

    protected static void setDailyTimeLimit(int seconds) {
        PlayerTimeManager.dailyTimeLimit = seconds;
    }

    public static long getDailyTimeLimit() {
        return dailyTimeLimit;
    }

    /*
    Adds a new player to the tracking system given their Minecraft UUID
     */
    public static void addPlayer(UUID playerUUID) {
        playerMap.put(playerUUID, new PlayerTimeTracker());
    }

    /*
    Gets the individual tracker object of a player given their Minecraft UUID
   */
    public static PlayerTimeTracker getTracker(UUID playerUUID) {
        return playerMap.get(playerUUID);
    }

    /*
    Check if the manager currently tracks a player (has an individual tracker with their UUID)
    */
    public static boolean hasPlayer(UUID playerUUID) {
        return playerMap.containsKey(playerUUID);
    }

    /*
    Adds one second to a player tracker (unnecessary if the individual player tracker is already available)
    */
    public static long updatePlayerTime(UUID playerUUID) {
        PlayerTimeTracker player = getTracker(playerUUID);
        if (player != null && !player.isCurrentlyTimeOut()) {
            player.addTimePlayed(1);
            if (player.getSecsPlayed() >= dailyTimeLimit) {
                player.setTimeoutState(true);
            }
            return player.getSecsPlayed();
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }


    /*
     Resets time of all tracked players (un-timeouts them and reset the playtime to 0)
     Banned players must be unbanned outside this class
     */
    public static void resetAllTime() {
        for (UUID playeruuid : playerMap.keySet()) {
            PlayerTimeTracker tt = getTracker(playeruuid);
            tt.resetTimePlayed();
            tt.setTimeoutState(false);
            try {
                tt.saveToFile(playeruuid);
            } catch (IOException e) {
                LOGGER.error("Error while trying to reset playerdata");
            }
        }
    }


    /*
     Checks if the specified player should be timeouted.
     */
    public static boolean checkForTimeout(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            return playerTimeTracker.hasTimePlayed(dailyTimeLimit);
        }
        throw new IllegalArgumentException("No player found under specified UUID");
    }


    /*
     Check if it is the reset time (between XX:00 and XX:03)
     */
    public static boolean isResetTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(resetTime)) {
            //Add 1 day to reset time
            resetTime = resetTime.plusDays(1);
            return true;
        }
        return false;
    }

    public static UUID playerUUIDbyName(String name, Level level) {
        for (UUID uuid : playerMap.keySet()) {
            for (Player p : level.players()) {
                if (p.getName().getString().equals(name) && p.getUUID().equals(uuid)) {
                    return uuid;
                }
            }
        }
        return null;
    }

    public static boolean isTimerEnabled() {
        return isEnabled;
    }

    public static void toggleTimer() {
        isEnabled = !isEnabled;
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUUID();
        //Check for data of the player
        if (!hasPlayer(playerUUID)) {
            addPlayer(playerUUID);
            LOGGER.info("[PLAYTIME LIMITER] " + player.getName() + "Logged in for the first time and is being added to the list");

        } else {
            LOGGER.info("[PLAYTIME LIMITER] " + player.getName() + "logged in but has already been added to the list.");
        }

        //Correctly render the local GUI
        if (isEnabled) {
            RemainingTimeHandler.sendToPlayer(new S2CRemainingTime(getTracker(playerUUID).getSecsPlayed()), (ServerPlayer) player);
        } else {
            RemainingTimeHandler.sendToPlayer(new S2CRemainingTime(45296), (ServerPlayer) player);
        }
        DailyTimeLimitHandler.sendToPlayer(new S2CDailyTimeLimit(dailyTimeLimit), (ServerPlayer) player); //Syncronize daily limit

        PlayerTimeTracker playerData = getTracker(playerUUID);

        // Load existing player data from JSON file if it exists
        try {
            File file = new File("./charmscmods/playtimelimiter/playerdata/" + playerUUID + "_timerData.json");
            if (file.exists()) {
                LOGGER.info("[PLAYTIME LIMITER] " + player.getName() + "Data file detected. Loading.");
                String json = FileUtils.readFileToString(file, Charset.defaultCharset());
                playerData = PlayerTimeTracker.fromJson(json);
                playerMap.put(playerUUID, playerData);
            } else {
                LOGGER.info("[PLAYTIME LIMITER] " + player.getName() + "Data file not found! (Must be first time?)");
            }
        } catch (IOException e) {
            LOGGER.info("[PLAYTIME LIMITER] " + player.getName() + "Data file not found! (Must be first time?)");
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        // Load existing player data from JSON file if it exists
        try {
            getTracker(event.getEntity().getUUID()).saveToFile(event.getEntity().getUUID());
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("[PLAYTIME LIMITER] ERROR while trying to save player timer data on logoff.");

        }
        LOGGER.info("[PLAYTIME LIMITER] Wrote playertimer to: " + event.getEntity().getUUID() + "_timerdata.json");
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppedEvent event) {
        for (Player p : event.getServer().getPlayerList().getPlayers()) {
            try {
                getTracker(p.getUUID()).saveToFile(p.getUUID());
            } catch (IOException e) {
                LOGGER.error("[PLAYTIME LIMITER] ERROR while trying to save player timer data on server shutdown");
            }
            LOGGER.info("[PLAYTIME LIMITER] Wrote playertimer to: " + p.getUUID() + "_timerdata.json");
        }

        backupThreadShutdown();
    }

    //Periodic backup of player data
    private void backupAllPlayerData() {
        for (Map.Entry<UUID, PlayerTimeTracker> entry : playerMap.entrySet()) {
            try {
                UUID playerUUID = entry.getKey();
                PlayerTimeTracker tracker = entry.getValue();
                tracker.saveToFile(playerUUID);
                LOGGER.info("[PLAYTIME LIMITER] Periodic backup data saved for player: " + playerUUID);
            } catch (IOException e) {
                LOGGER.error("[PLAYTIME LIMITER] Error while saving periodic backup data for player: " + entry.getKey(), e);
            }
        }
    }
    //Gracefully shutdown the backup executor thread
    public static void backupThreadShutdown() {
        backupExecutor.shutdown();
        try {
            if (!backupExecutor.awaitTermination(15, TimeUnit.SECONDS)) {
                backupExecutor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            backupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    private static int tickCount = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) { //Manage player time and reset
        if (event.phase == TickEvent.Phase.END) {
            if (tickCount % 20 == 0 && tickCount != 0) {
                if (isEnabled) { //Count if the timer is enabled (toggleTimer)
                    //Do player time managing
                    for (ServerPlayer p : event.getServer().getPlayerList().getPlayers()) { //For each player:
                        try {
                            long updatedTime = updatePlayerTime(p.getUUID()); //Add one to the player time
                            RemainingTimeHandler.sendToPlayer(new S2CRemainingTime(updatedTime), p); //Update player GUI
                        }
                        catch (IllegalArgumentException e) {
                            LOGGER.error("[PLAYTIME LIMITER] ERROR while trying to update player time: " + e.getMessage());
                        }
                        if (checkForTimeout(p.getUUID())) { //Check if the player timed out
                            LOGGER.info("[PLAYTIME LIMITER] " + p.getName() + "Has been timed out");
                            //Ban and disconnect the player
                            p.getServer().getPlayerList().getBans().add(new UserBanListEntry(p.getGameProfile(), null, "TIMEOUT_LOOP_CHECK", null, "Tiempo diario agotado! Vuelve mañana"));
                            p.connection.disconnect(Component.translatable("system.disconnectmsg.exceededplaytime"));
                        }
                    }
                }
                if (isResetTime()) { //Checl if the reset time for today has been surpassed
                    LOGGER.info("[PLAYTIME LIMITER] Is ban reset time");
                    resetAllTime(); //Reset the timers for every player registered

                    for (UserBanListEntry p : event.getServer().getPlayerList().getBans().getEntries()) { //For each banned player:
                        LOGGER.info("[PLAYTIME LIMITER] " + p.getDisplayName() + " grabbed!");

                        // Check the source of the ban and unban (remove from list) if it matches the source for a timeout (TIMEOUT_LOOP_CHECK)
                        if (p.getSource().equals("TIMEOUT_LOOP_CHECK")) {
                            event.getServer().getPlayerList().getBans().remove(p);
                            LOGGER.info("[PLAYTIME LIMITER]" + p.getDisplayName().getString() + " Is banned due to time out. Unbanning");
                        }
                    }
                }
                tickCount = 0;
            } else {
                tickCount++;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {//Add effects when the player dies.
        if (event.getEntity() instanceof Player player) {
            Level level = player.level();
            //Entity killer = event.getSource().getEntity();

            //Cast thunder particle and sound over player death location
            Vec3 deathPos = player.getPosition(1);
            BlockPos deathBP = new BlockPos((int) deathPos.x, (int) deathPos.y, (int) deathPos.z);
            level.playSound(null, deathBP, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 1.0f, 1.0f);

            MutableComponent marqueeup;
            MutableComponent space1;
            MutableComponent message;
            MutableComponent space2;
            MutableComponent marqueedown;


            marqueeup = Component.translatable("system.announcer.playerdeath.header");
            space1 = Component.translatable("system.announcer.playerdeath.emptyline");
            message = Component.translatable("system.announcer.playerdeath.id" + player.getName().getString());
            space2 = Component.translatable("system.announcer.playerdeath.emptyline");
            marqueedown = Component.translatable("system.announcer.playerdeath.footer");


            MutableComponent finalMessage = Component.translatable("system.announcer.playerdeath.final");
            finalMessage.append(marqueeup);
            finalMessage.append(space1);
            finalMessage.append(message);
            finalMessage.append(space2);
            finalMessage.append(marqueedown);


            marqueeup.withStyle(ChatFormatting.BLUE);
            message.withStyle(ChatFormatting.DARK_GRAY);
            marqueedown.withStyle(ChatFormatting.BLUE);

            for (ServerPlayer p : Objects.requireNonNull(player.getServer()).getPlayerList().getPlayers()) {
                p.sendSystemMessage(finalMessage, false);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) { //Eliminate the player when they respawn
        if (event.getEntity() != null) {
            // Get the player who just respawned
            Player player = event.getEntity();
            LOGGER.info("[PLAYTIME LIMITER] " + player.getName().getString() + " has died. Moved to spectator mode and banned from server");
            ServerPlayer serverPlayer = (ServerPlayer) player;
            //Move player to spectator mode
            serverPlayer.setGameMode(GameType.SPECTATOR);
            //Ban and add to dead players list (Ban with ELIMINATION_ON_DEATH" identificator)
            player.getServer().getPlayerList().getBans().add(new UserBanListEntry(player.getGameProfile(), null, "ELIMINATION_ON_DEATH", null, "Has sido eliminado! Por favor, espera a que un asoociado de pruebas te de más instrucciones"));
            serverPlayer.connection.disconnect(Component.translatable("system.disconnectmsg.playerdeath"));

        }
    }

    public void loadManagerData() {
        try {
            Path dataPath = Paths.get("./charmscmods/playtimelimiter/manager_config.json");
            if (Files.exists(dataPath)) {
                String dataString = new String(Files.readAllBytes(dataPath));
                Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
                ServerState serverState = gson.fromJson(dataString, ServerState.class);
                dailyTimeLimit = serverState.dailyTimeLimit;
                resetTime = serverState.resetTime;
                isEnabled = serverState.isToggled;
                PvpDamageGameRule.set(serverState.pvpToggle);
                PvpManager.setPVPstate(serverState.pvpLevel);
            }
        } catch (IOException e) {
            LOGGER.error("Error loading manager data: " + e.getMessage());
        }
    }

    public void saveManagerData() {
        ServerState serverState = new ServerState(dailyTimeLimit, resetTime, isEnabled, PvpManager.getPVPstate(), PvpDamageGameRule.get());
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String json = gson.toJson(serverState);
        try {
            Path dataPath = Paths.get("./charmscmods/playtimelimiter/manager_config.json");
            Files.createDirectories(dataPath.getParent());
            File dataFile = dataPath.toFile();
            if (!dataFile.exists()) {
                dataFile.createNewFile();
            }
            FileWriter writer = new FileWriter(dataFile);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            LOGGER.error("Error saving manager data: " + e.getMessage());
        }
    }

    public static class ServerState {
        private final long dailyTimeLimit;
        private final LocalDateTime resetTime;
        private final boolean isToggled;
        private final int pvpLevel;
        private final boolean pvpToggle;

        public ServerState(long dailyTimeLimit, LocalDateTime resetTime, boolean isToggled, int pvpLevel, boolean pvpToggle) {
            this.dailyTimeLimit = dailyTimeLimit;
            this.resetTime = resetTime;
            this.isToggled = isToggled;
            this.pvpLevel = pvpLevel;
            this.pvpToggle = pvpToggle;
        }

    }

}

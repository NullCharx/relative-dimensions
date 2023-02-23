package es.nullbyte.charmiscmods.PlayerTimeLimit;

import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.GUI.LocalState;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.RemainingTimeHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet.S2CRemainingTime;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * Manages the playtime of multiple players
 */
public class PlayerTimeManager {
    private static final Map<UUID, PlayerTimeTracker> playerMap = new HashMap<>();//Hashmap of individual player trackers
    private static long dailyTimeLimit; // The daily time limit in seconds
    private static LocalDateTime resetTime; // The time of day in which the timers reset

    private static boolean isEnabled;
    private static final Logger LOGGER = LogUtils.getLogger();


    //Time for reset.

    /*
    Constructor for the time manager.
    It takes both the daily time limit in seconds and the time of the day in which the timers reset.
     */

    public PlayerTimeManager(int dailyTimeLimit, int resetHour)  {
        PlayerTimeManager.dailyTimeLimit = dailyTimeLimit;
        //Resets at resethour:00
        LocalTime  time = LocalTime.of(resetHour, 33);
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
    * Sets an specified amount of time for a player
     */
    public int setPlayerTime (UUID playerUUID, long secs) {
        if(playerMap.get(playerUUID) != null){
            playerMap.get(playerUUID).setTimePlayed(secs);
            return 0;
        }
        return 1;
    }

    public int addPlayerTime (UUID playerUUID, long secs) {
        if(playerMap.get(playerUUID) != null){
            playerMap.get(playerUUID).addTimePlayed(secs);
            return 0;
        }
        return 1;
    }

    public int substractPlayerTime (UUID playerUUID, long secs) {
        if(playerMap.get(playerUUID) != null){
            playerMap.get(playerUUID).removeTimePlayed(secs);
            return 0;
        }
        return 1;
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
        for (PlayerTimeTracker player : playerMap.values()) {
            player.resetTimePlayed();
            player.setTimeoutState(false);
        }
    }

    /*
     Sets the login time of a player to now
     */
    public static void playerLogOn(UUID playerUUID) {
        PlayerTimeTracker playerTimeTracker = getTracker(playerUUID);
        if (playerTimeTracker != null) {
            playerTimeTracker.setLastLoginEpoch();
            playerTimeTracker.playerConnected();
        } else {
            throw new IllegalArgumentException("No player found under specified UUID");
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

    public static UUID playerUUIDbyName (String name,  Level level){
        for (UUID uuid : playerMap.keySet()){
            for (Player p : level.players()) {
                if (p.getName().getString().equals(name)) {
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


    public void serializePlayers() {
        // Write player data to file or database
    }

    public void deserializePlayers() {
        // Read player data from file or database
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUUID();
        if (!hasPlayer(playerUUID)){
            addPlayer(playerUUID);
            playerLogOn(playerUUID);
            RemainingTimeHandler.sendToPlayer(new S2CRemainingTime((long) 45296), (ServerPlayer) player);

            LOGGER.info(player.getName() + "Logged in for the first time and is being added to the list");

        } else {
            playerLogOn(playerUUID);
            LOGGER.info(player.getName() + "logged in but has already been added to the list. Changing to online");
        }

    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        //TODO Find a way to persistently store player data regarding time played when a player leaves. Probably on a JSON file
        //Also check if vanilla teams are persistent across reboots!
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppedEvent event) {
        //TODO Find a way to persistently store player data regarding time played when the server shuts down or crashes

    }
    private static int tickCount = 0;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (isEnabled) { //Count if the timer is enabled
                if (tickCount % 20 == 0 && tickCount != 0) {
                    //Do player time managing
                    for (Player p : event.getServer().getPlayerList().getPlayers()) {
                        Long updatedTime = updatePlayerTime(p.getUUID());
                        RemainingTimeHandler.sendToPlayer(new S2CRemainingTime(updatedTime), (ServerPlayer) p);
                        if (checkForTimeout(p.getUUID())) {
                            LOGGER.info(p.getName() + "Has been timed out");
                            p.getServer().getPlayerList().getBans().add(new UserBanListEntry(p.getGameProfile(), null, "TIMEOUT_LOOP_CHECK", null, "Tiempo diario agotado! Vuelve mañana"));
                            ServerPlayer serverplayer = (ServerPlayer) p;
                            serverplayer.connection.disconnect(Component.translatable("Tu tiempo de juego diario ha sido excedido. Vuelve mañana!"));
                        }
                    }
                    if (isResetTime()) { //Se ha alcanzado la hora de reseteo y se procede a resetear
                        LOGGER.info("Is ban reset time");
                        resetAllTime();
                        for (UserBanListEntry p : event.getServer().getPlayerList().getBans().getEntries()) { //Iterar por
                            LOGGER.info(p.getDisplayName() + " ");

                            // todos los jugadores baneados y desbanear aquellas que no estén baneados por muerte
                            if (p.getSource().equals("TIMEOUT_LOOP_CHECK")) {
                                event.getServer().getPlayerList().getBans().remove(p);
                            }
                        }
                    }
                    tickCount = 0;
                } else {
                    tickCount++;
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof Player) {
            // Get the player who just respawned
            Player player = event.getEntity();
            LOGGER.info(player.getName().getString() + " has died. Moved to spectator mode and banned from server");

            //Ban and add to dead players list
            player.getServer().getPlayerList().getBans().add(new UserBanListEntry(player.getGameProfile(), null, "ELIMINATION_ON_DEATH", null, "Has sido eliminado! Por favor, espera a que un asoociado de pruebas te de más instrucciones"));
            ServerPlayer serverplayer = (ServerPlayer) player;
            serverplayer.connection.disconnect(Component.translatable("Has sido eliminado! Gracias por jugar con nosotros"));
        }
    }
}

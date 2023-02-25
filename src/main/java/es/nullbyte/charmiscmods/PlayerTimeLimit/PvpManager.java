package es.nullbyte.charmiscmods.PlayerTimeLimit;


import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.PVPStateHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet.S2CPVPState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;

public class PvpManager {
    private static int PVPstate; //-1 PVP off, 0 PVP on, 1 ULTRA
    private static final String PVPOFFTEAMNAME = "EQUIPO_GLOBAL_PVPoff";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PvpManager() {
        PVPstate = -1;
        registerEvents();
    }
    public PvpManager(int initialState) {
        if (initialState >= -1 && initialState <= 1){
            PVPstate = initialState;
        } else {
            PVPstate = -1;
        }
        registerEvents();
    }

    private void registerEvents() {
        MinecraftForge.EVENT_BUS.register(this); //Register the class on the event bus so any events it has will be called

        //Add listeners for the events we want to listen to. Since this is not an item or blocck, that are managed in
        //The main class, we need to add the listeners here
        MinecraftForge.EVENT_BUS.addListener(PvpManager::onPlayerLoggedIn);
    }

    public static void bypassSetPVPstte(int state) {
        PVPstate = state;
    }

    public static void syncronizeState(Level level) {
        if(PVPstate >= -1 && PVPstate <= 1) { //If the state is valid and it's not the same as the current state:
            if (isPVPultra()){ //If PVP is ultra, it means it can only decrease so:
                enableNaturalRegen(level); //enable natural regen (normal and non-PVP)
                if(PVPstate == -1){ //Check if the target state is PVP off and if it is:
                    disableGlobalDamage(level); //disable global damage too
                }
            } else if (isPVPon()){ //If the PVP is on, it means it can both increase and decrease so:
                if (PVPstate == 1){ //If it increases:
                    disableNaturalRegen(level); //Disable natural regen (ULTA PVP). Global damage is already disabled
                } else if (PVPstate == -1){//if it decreases:
                    disableGlobalDamage(level); //disable global damage
                }
            } else { //If the PVP is off, it means it can only increase so:
                enableGlobalDamage(level); //enable global damage
                if (PVPstate == 1){ //Check if the target state is ULTRA PVP and if it is:
                    disableNaturalRegen(level); //disable natural regen too
                }
            }
        }
        //Update state for all online players
        for(Player p: level.players()) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) p);
        }
    }
    public static void setPVPstate(int state, Level level) {
        if(state >= -1 && state <= 1) { //If the state is valid and it's not the same as the current state:
            if (isPVPultra()){ //If PVP is ultra, it means it can only decrease so:
                enableNaturalRegen(level); //enable natural regen (normal and non-PVP)
                if(state == -1){ //Check if the target state is PVP off and if it is:
                    disableGlobalDamage(level); //disable global damage too
                }
            } else if (isPVPon()){ //If the PVP is on, it means it can both increase and decrease so:
                if (state == 1){ //If it increases:
                    disableNaturalRegen(level); //Disable natural regen (ULTA PVP). Global damage is already disabled
                } else if (state == -1){//if it decreases:
                    disableGlobalDamage(level); //disable global damage
                }
            } else { //If the PVP is off, it means it can only increase so:
                enableGlobalDamage(level); //enable global damage
                if (state == 1){ //Check if the target state is ULTRA PVP and if it is:
                    disableNaturalRegen(level); //disable natural regen too
                }
            }
            PVPstate = state;
        }
        //Update state for all online players
        for(Player p: level.players()) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) p);
        }
    }

    public static void increasePVPstate(Level level){
        if (PVPstate != 1){ //If the PVP state is not ULTRA PVP, it can increase
            PVPstate++; //Increase the PVP state
            if(isPVPon()) { //if its currently off, it means it goes to PVP on:
                enableGlobalDamage(level); //enable global damage
            } else if (isPVPultra()){ //if its currently on, it means it goes to ULTRA PVP:
                disableNaturalRegen(level); //Disable natural regen (ULTA PVP)
            }
        }

        //Update state for all online players
        for(Player p: level.players()) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) p);
        }
    }

    public static void decreasePVPstate(Level level) {
        if (PVPstate != -1){
            PVPstate--; //Decrease the PVP state
            if(isPVPon()) { //if its currently ultra, it means it goes to PVP on:
                enableNaturalRegen(level); //enable natural regen
            } else if (isPVPoff()){ //if its currently on, it means it goes to  PVP off:
                disableGlobalDamage(level); //Disable global damage
            }
        }

        //Update state for all online players
        for(Player p: level.players()) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) p);
        }
    }

    public static int getPVPstate() {
        return PVPstate;
    } //return current pvp sate

    public static boolean isPVPon() {
        return PVPstate == 0;
    } //Check if pvp is on

    public static boolean isPVPultra() {
        return PVPstate == 1;
    } //Check if pvp is ultra

    public static boolean isPVPoff() {
        return PVPstate == -1;
    } //Check if pvp is off

    public static void disableGlobalDamage(Level level) { //Disables global damage by:

        PlayerTeam team = getOrCreateTeam(level.getScoreboard()); //Getting or creating a team with no collisions
        for (Player player : level.players()) { //Adding all online players to the team
            level.getScoreboard().addPlayerToTeam(player.getName().getString(),team);
        }
    }
    public static void enableGlobalDamage(Level level) { //Renables  global damage by:

        PlayerTeam team = getOrCreateTeam(level.getScoreboard()); //Getting the no damage team
        level.getScoreboard().removePlayerTeam(team); //Deleting it, thus, making every connected and non-connected player leave it
    }

    private static PlayerTeam getOrCreateTeam(Scoreboard scoreboard) { //Gets or creates the team that will have no collisions
        PlayerTeam team = scoreboard.getPlayerTeam(PVPOFFTEAMNAME); //Check if the team already exists
        if (team == null) { //If it doesnt, add it with the CollisionRule set to NEVER
            team = scoreboard.addPlayerTeam(PVPOFFTEAMNAME);
            team.setAllowFriendlyFire(false);
        }
        return team; //Return the no collision team
    }

    private static void disableNaturalRegen(Level level) { //Disable natural regeneration by setting the correspondent gamreule to false
        //Set naturalRegeneration gamerule to false
        level.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, level.getServer());

    }

    private static void enableNaturalRegen(Level level) {//Disable natural regeneration by setting the correspondent gamreule to true
        level.getGameRules().getRule(GameRules.RULE_NATURAL_REGENERATION).set(true, level.getServer());
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) { //Check, on each player login:
        syncronizeState(event.getEntity().level);
        if(isPVPoff()){ //if the pvp is disabled and if it is, add the player to the noncolliding global group
            PlayerTeam team = getOrCreateTeam(event.getEntity().getScoreboard());
            event.getEntity().getScoreboard().addPlayerToTeam(event.getEntity().getName().getString(),team);
        }
        //Update the local render on login
        if(!event.getEntity().level.isClientSide) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) event.getEntity());
        }
        LOGGER.info("[PLAYTIME LIMITER]  pvp state synced on logon!");
    }
}

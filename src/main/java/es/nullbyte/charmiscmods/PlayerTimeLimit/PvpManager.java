package es.nullbyte.charmiscmods.PlayerTimeLimit;


import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds.PvpDamageGameRule;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.PVPStateHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.packet.S2CPVPState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;

public class PvpManager {
    private static int PVPstate; //-1 PVP off, 0 PVP on, 1 ULTRA
    private static final Logger LOGGER = LogUtils.getLogger();


    public static void setPVPstate(int state) {
        if (state >= -1 && state <= 1) { //If the state is valid and it's not the same as the current state:
            if (state == 1) { //If PVP target is ultra:
                disableNaturalRegen(); //disable natural regen ((ultra))
                enableGlobalDamage(); //disable global damage too
            } else if (state == 0) { //If PVP target is on:
                enableNaturalRegen(); //enable natural regen (normal and non-PVP)
                enableGlobalDamage(); //disable global damage too
            } else { //If PVP target is on:
                enableNaturalRegen(); //enable natural regen (normal and non-PVP)
                disableGlobalDamage(); //disable global damage too
            }
            PVPstate = state;
            syncrhonizeLocalPVP();
        }
    }

    public static void increasePVPstate() {
        if (PVPstate != 1) { //If the PVP state is not ULTRA PVP, it can increase
            PVPstate++; //Increase the PVP state
            if (isPVPon()) { //if its currently off, it means it goes to PVP on:
                enableGlobalDamage(); //enable global damage
            } else if (isPVPultra()) { //if its currently on, it means it goes to ULTRA PVP:
                disableNaturalRegen(); //Disable natural regen (ULTA PVP)
            }
        }
        syncrhonizeLocalPVP();
    }

    public static void decreasePVPstate() {
        if (PVPstate != -1) {
            PVPstate--; //Decrease the PVP state
            if (isPVPon()) { //if its currently ultra, it means it goes to PVP on:
                enableNaturalRegen(); //enable natural regen
            } else if (isPVPoff()) { //if its currently on, it means it goes to  PVP off:
                disableGlobalDamage(); //Disable global damage
            }
        }
        syncrhonizeLocalPVP();
    }

    public static void syncrhonizeLocalPVP () {
        //Update state for all online players
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), p);
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

    public static void disableGlobalDamage() { //Disables global damage by:
        PvpDamageGameRule.set(false);
    }

    public static void enableGlobalDamage() { //Renables  global damage by:
        PvpDamageGameRule.set(true);
    }

    private static void disableNaturalRegen() { //Disable natural regeneration by setting the correspondent gamreule to false
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        GameRules gameRules = server.getGameRules();

        gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(false, server);

    }

    private static void enableNaturalRegen() {//Disable natural regeneration by setting the correspondent gamreule to true
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        GameRules gameRules = server.getGameRules();

        gameRules.getRule(GameRules.RULE_NATURAL_REGENERATION).set(true, server);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) { //Check, on each player login:
        setPVPstate(getPVPstate());
        //Update the local render on login
        if (!event.getEntity().level.isClientSide) {
            PVPStateHandler.sendToPlayer(new S2CPVPState(getPVPstate()), (ServerPlayer) event.getEntity());
        }
        LOGGER.info("[PLAYTIME LIMITER]  pvp state synced on logon!");

    }

    //Also check team persistance (if it saves the default one then it is ok to leave it like this)
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player && event.getSource().getEntity() instanceof Player) {
            if (!PvpDamageGameRule.get()) {
                event.setCanceled(true);
            }
        }
    }
}


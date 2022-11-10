package es.nullbyte.charmiscmods.event;

import es.nullbyte.charmiscmods.transmatstate.PlayerTransmatstate;
import es.nullbyte.charmiscmods.transmatstate.PlayerTransmatstateProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class ModEvents {

    //GET PLAYER CAPABILITY
    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof Player) {
            if(!event.getObject().getCapability(PlayerTransmatstateProvider.TRANSMATSTATE_CAPABILITY).isPresent()) {
                event.addCapability(new ResourceLocation(MOD_ID, "properties"), new PlayerTransmatstateProvider());
            }
        }
    }

    //COPY CAPABILITY DATA AFTER DEATH
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerTransmatstateProvider.TRANSMATSTATE_CAPABILITY).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerTransmatstateProvider.TRANSMATSTATE_CAPABILITY).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    //REGISTER CAPABILITIES
    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerTransmatstate.class);
    }

    /*
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(event.side == LogicalSide.SERVER) {
            event.player.getCapability(PlayerTransmatstateProvider.TRANSMATSTATE_CAPABILITY).ifPresent(thirst -> {
                if(thirst.getTransmatstate() > 0 && event.player.getRandom().nextFloat() < 0.005f) { // Once Every 10 Seconds on Avg
                    thirst.subState(1);
                    event.player.sendSystemMessage(Component.literal("Subtracted Thirst"));
                }
            });
        }
    }*/
}

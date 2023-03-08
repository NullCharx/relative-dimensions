package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import es.nullbyte.charmiscmods.items.*;

public class ItemInit {
    //https://moddingtutorials.org/basic-items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CharMiscModsMain.MOD_ID);
    //Test 1
    public static final RegistryObject<Item> testitem1 = ITEMS.register("testitem1",
            () -> new Item(new Item.Properties()));

    //AVID short distance matter particle transmitter
    public static final RegistryObject<Item> AVID_SDPT = ITEMS.register("avidsdpt",
            () -> new AvidShortDistanceParticleTransmitter(new Item.Properties().durability(1000)));

    //Transmat beam generator
    public static final RegistryObject<Item> TRANSMAT_BEAM_EMITTER = ITEMS.register("transmatbeamemt",
            () -> new TransmatBeamEmitter(new Item.Properties().durability(1000)));

    //Detector compass
    public static final RegistryObject<Item> PLAYER_TRACKER_COMPASS = ITEMS.register("trackercompass",
            () -> new PlayerTrackerCompass(new Item.Properties().durability(1000)));

    public static final RegistryObject<Item> TEAM_TRACKER_COMPASS = ITEMS.register("teamtrackercompass",
            () -> new TeamTrackerCompass(new Item.Properties().durability(1000)));

    //BLOCK ITEMS


}

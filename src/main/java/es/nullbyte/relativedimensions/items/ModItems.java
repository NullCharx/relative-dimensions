package es.nullbyte.relativedimensions.items;

import es.nullbyte.relativedimensions.RelativeDimensionsMain;
import es.nullbyte.relativedimensions.items.aberrant.AberrantAxe;
import es.nullbyte.relativedimensions.items.aberrant.AberrantPickaxe;
import es.nullbyte.relativedimensions.items.aberrant.AberrantSword;
import es.nullbyte.relativedimensions.items.tp.AvidShortDistanceParticleTransmitter;
import es.nullbyte.relativedimensions.items.tp.TransmatBeamEmitter;
import es.nullbyte.relativedimensions.items.tracking.PlayerTrackerCompass;
import es.nullbyte.relativedimensions.items.tracking.TeamTrackerCompass;
import es.nullbyte.relativedimensions.items.utils.ModToolTiers;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    //https://moddingtutorials.org/basic-items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RelativeDimensionsMain.MOD_ID);
    //Test 1
    public static final RegistryObject<Item> TESTITEM1 = ITEMS.register("testitem1",
            () -> new Item(new Item.Properties()));

    //Aberrant shard
    public static final RegistryObject<Item> ABERRANT_SHARD = ITEMS.register("aberrant_shard",
            () -> new Item(new Item.Properties().fireResistant()));

    //Aberrant ingot
    public static final RegistryObject<Item> ABERRANT_INGOT = ITEMS.register("aberrant_ingot",
            () -> new Item(new Item.Properties().fireResistant()));

    //Aberrant stick
    public static final RegistryObject<Item> ABERRANT_STICK = ITEMS.register("aberrant_stick",
            () -> new Item(new Item.Properties().fireResistant()));
    //ABERRANT SWORD
    public static final RegistryObject<Item> ABERRANT_SWORD = ITEMS.register("aberrant_sword",
            () -> new AberrantSword(ModToolTiers.ABERRANT,0, 10, new Item.Properties()));

    //ABERRANT PICKAXE
    public static final RegistryObject<Item> ABERRANT_PICK= ITEMS.register("aberrant_pickaxe",
            () -> new AberrantPickaxe(ModToolTiers.ABERRANT,3, 3, new Item.Properties()));
   public static final RegistryObject<Item> ABERRANT_AXE = ITEMS.register("aberrant_axe",
            () -> new AberrantAxe(ModToolTiers.ABERRANT,2, 2, new Item.Properties()));
    /** public static final RegistryObject<Item> ABERRANT_SHOVEL = ITEMS.register("aberrant_shovel",
            () -> new AberrantShovel(ModToolTiers.ABERRANT,3, 3, new Item.Properties()));
   public static final RegistryObject<Item> ABERRANT_HOE = ITEMS.register("aberrant_hoe",
            () -> new AberrantHoe(ModToolTiers.ABERRANT,3, 3, new Item.Properties()));
**/
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


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

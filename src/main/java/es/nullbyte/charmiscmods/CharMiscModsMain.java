package es.nullbyte.charmiscmods;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PvpManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds.modPVPcmd;
import es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds.modTimercmd;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.PVPStateHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.RemainingTimeHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.DailyTimeLimitHandler;
import es.nullbyte.charmiscmods.init.*;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static es.nullbyte.charmiscmods.init.ItemInit.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CharMiscModsMain.MOD_ID)
@Mod.EventBusSubscriber(modid = CharMiscModsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CharMiscModsMain {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "chrmscmds";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int DEF_TIMELIMIT = 4*60*60; //4 hours
    public static final int DEF_RESETTIME = 06; //6am 35 minutes
    private final Path TMConfigPath = Paths.get(".", "charmscmods", "playtimelimiter", "manager_config.json");

    public static PlayerTimeManager timeManager;
    public static final PvpManager pvpManger = new PvpManager(-1);



    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public CharMiscModsMain() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::setup);
        ItemInit.ITEMS.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);

        //Register custom creative tab
        modEventBus.addListener(this::buildContents);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);



        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void setup(final FMLCommonSetupEvent event) {
        //Registrar eventos encolados, includos los paquetes de red
        event.enqueueWork(() -> {
            RemainingTimeHandler.register();
            PVPStateHandler.register();
            DailyTimeLimitHandler.register();
            //ModMessages.register();
        });
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }



    @SubscribeEvent
    public void addCreative(CreativeModeTabEvent.BuildContents event) {
        //Use this event to add items to a default creative tab
        // Add to ingredients tab
        //if (event.getTab() == CreativeModeTabs) {

        //}
    }

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "example"), builder ->
        // Set name of tab to display
        builder.title(Component.translatable("item_group." + MOD_ID + ".example"))
        // Set icon of creative tab
        .icon(() -> new ItemStack(testitem1.get()))
        // Add default items to tab
        //Add all items to tab through here
        .displayItems((enabledFlags, populator, hasPermissions) -> {
            populator.accept(testitem1.get());
            populator.accept(testitem1.get());
            populator.accept(AVID_SDPT.get());
            populator.accept(TRANSMAT_BEAM_EMITTER.get());
            populator.accept(PLAYER_TRACKER_COMPASS.get());
            populator.accept(TEAM_TRACKER_COMPASS.get());

        })
        );
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("--->CHAR'S MISCELANEOUS MODIFICATIONS.");
        LOGGER.info("Here's a list of what this version of the mod contains");
        LOGGER.info("->Short distance teleporter item");
        LOGGER.info("->Delayed long distance teleporter item");
        LOGGER.info("->Functioning player tracking compass");
        LOGGER.info("->Functioning team player tracking compass");
        LOGGER.info("->Custom PVP GUI");

        LOGGER.info("->Thank you for using this mod!");
        LOGGER.info("--------------------------------");


        LOGGER.info("[CHARMISCMODS - MAIN] Registering commands");

        //Register commands with server dispatcher on server startup. Call common method to resgister all commands
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
        registerCommands(dispatcher);

        LOGGER.info("[CHARMISCMODS - MAIN] Trying to load manager configuration");
        PlayerTimeManager tempTM = new PlayerTimeManager(DEF_TIMELIMIT,DEF_RESETTIME);
        try {
            File file = new File("./charmscmods/playtimelimiter/manager_config.json");
            if (file.exists()) {
                tempTM.loadData(file);
            }
            LOGGER.info("[PLAYTIMELIMITER - MAIN] Succesfully loaded saved manager parameters");

        } catch (IOException e) {
            LOGGER.error("[PLAYTIMELIMITER - MAIN] Failed to load saved manager parameters");
        }
        timeManager = tempTM;

    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        try {
            File file = new File("./charmscmods/playtimelimiter/manager_config.json");
            timeManager.saveData(file);
            LOGGER.info("[PLAYTIMELIMITER - MAIN] Succesfully saved current manager parameters");

        } catch (IOException e) {
            LOGGER.error("[PLAYTIMELIMITER - MAIN] Failed to save manager parameters");
        }
    }

    //register buildcontents event to the event bus

    public static void registerCommands (CommandDispatcher<CommandSourceStack> dispatcher) {
        //REGISTER THE COMMANDS HERE!
        modPVPcmd.register(dispatcher);
        modTimercmd.register(dispatcher);
    }




}
    /*
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

----------------------------------------

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }*/


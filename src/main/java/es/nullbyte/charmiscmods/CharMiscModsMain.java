package es.nullbyte.charmiscmods;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PvpManager;
import es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds.modPVPcmd;
import es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds.modTimercmd;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.DailyTimeLimitHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.PVPStateHandler;
import es.nullbyte.charmiscmods.PlayerTimeLimit.network.RemainingTimeHandler;
import es.nullbyte.charmiscmods.init.ItemInit;
import es.nullbyte.charmiscmods.init.TileEntityInit;
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

import static es.nullbyte.charmiscmods.init.ItemInit.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CharMiscModsMain.MOD_ID)
@Mod.EventBusSubscriber(modid = CharMiscModsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CharMiscModsMain {
    // Define mod id in a common place for everything to reference.
    //TEst forGUI mac.
    public static final String MOD_ID = "chrmscmds";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int DEF_TIMELIMIT = 4*60*60; //4 hours
    public static final int DEF_RESETTIME = 6; //6am 35 minutes

    public static final PlayerTimeManager timeManager = new PlayerTimeManager(DEF_TIMELIMIT,DEF_RESETTIME);

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

        MinecraftForge.EVENT_BUS.register(PvpManager.class); //Register the class on the event bus so any events it has will be called

        //Add listeners for the events we want to listen to. Since this is not an item or blocck, that are managed in
        //The main class, we need to add the listeners here
        MinecraftForge.EVENT_BUS.addListener(PvpManager::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(PvpManager::onLivingAttack);

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
        LOGGER.info("[CHARMISCMODS - MAIN] Loading manager registry");
        timeManager.loadManagerData();


    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("[CHARMISCMODS - MAIN] Saving manager registry");
        timeManager.saveManagerData();
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


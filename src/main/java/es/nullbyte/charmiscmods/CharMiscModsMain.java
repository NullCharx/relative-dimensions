package es.nullbyte.charmiscmods;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.commands.modTeam;
import es.nullbyte.charmiscmods.commands.teams.TeamMgr;
import es.nullbyte.charmiscmods.init.*;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import static es.nullbyte.charmiscmods.init.ItemInit.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CharMiscModsMain.MOD_ID)
@Mod.EventBusSubscriber(modid = CharMiscModsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CharMiscModsMain {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "chrmscmds";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public CreativeModeTab CUSTOM_TAB;

    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public CharMiscModsMain() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::setup);
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);

        //Register custom creative tab
        modEventBus.addListener(this::buildContents);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
    }


    @SubscribeEvent
    public void addCreative(CreativeModeTabEvent.BuildContents event) {
        //Use this event to add items to a default creative tab
        // Add to ingredients tab
        if (event.getTab() == CUSTOM_TAB) {
            event.accept(AVID_SDPT.get());
            event.accept(TRANSMAT_BEAM_EMITTER.get());
            event.accept(PLAYER_TRACKER_COMPASS.get());
            event.accept(TEAM_TRACKER_COMPASS.get());

            //event.accept(TESTBLOCK1.get());
            //event.accept(ADVANCEDTESTBLOCK.get());
            //event.accept(STRAIGHTRAIL.get());
        }
    }

    @SubscribeEvent
    public void buildContents(CreativeModeTabEvent.Register event) {
        CUSTOM_TAB = event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "example"), builder ->
        // Set name of tab to display
        builder.title(Component.translatable("item_group." + MOD_ID + ".example"))
        // Set icon of creative tab
        .icon(() -> new ItemStack(testitem1.get()))
        // Add default items to tab
        //Add all items to tab through here
        .displayItems((enabledFlags, populator, hasPermissions) -> {
            populator.accept(testitem1.get());

        })
        );
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("--->CHAR'S MISCELANEOUS MODIFICATIONS.");
        LOGGER.info("Here's a list of what this version of the mod contains");
        LOGGER.info("->Short distance teleporter item");
        LOGGER.info("->Delayed long distance teleporter item");
        LOGGER.info("->Functioning player tracking compass");
        LOGGER.info(" ");
        LOGGER.info("Here's a list of what is in development");
        LOGGER.info("Custom teams");
        LOGGER.info("Team player tracking compass");
        LOGGER.info(" ");
        LOGGER.info(" ");

        LOGGER.info("Thank you for using this mod!");

        //Register commands with server dispatcher on server startup. Call common method to resgister all commands
        CommandDispatcher<CommandSourceStack> dispatcher = event.getServer().getCommands().getDispatcher();
        registerCommands(dispatcher);

    }
    public static void registerCommands (CommandDispatcher<CommandSourceStack> dispatcher) {
        modTeam.register(dispatcher);
    }
}


/*
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


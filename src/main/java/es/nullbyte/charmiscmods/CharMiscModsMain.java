package es.nullbyte.charmiscmods;

import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.init.*;
import es.nullbyte.charmiscmods.init.*;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import static es.nullbyte.charmiscmods.init.BlockInit.*;
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
            event.accept(TESTBLOCK1.get());
            event.accept(ADVANCEDTESTBLOCK.get());
            event.accept(STRAIGHTRAIL.get());
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

    //register buildcontents event to the event bus



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


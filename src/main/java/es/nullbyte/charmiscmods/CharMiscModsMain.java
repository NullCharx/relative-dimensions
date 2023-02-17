package es.nullbyte.charmiscmods;

import com.mojang.logging.LogUtils;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.init.*;
import es.nullbyte.charmiscmods.init.*;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static final int TIMELIMIT = 3600*4; //4 hours
    public static final int RESETTIME = 7; //6am
    public static final PlayerTimeManager timeManager = new PlayerTimeManager(TIMELIMIT,RESETTIME);

    public static final List<Component> deadPlayers = new ArrayList<>();

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
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUUID();
        if (!timeManager.hasPlayer(playerUUID)){
            timeManager.addPlayer(playerUUID);
            timeManager.playerLogOn(playerUUID);
            LOGGER.info(player.getName() + "Logged in for the first time and is being added to the list");

        } else {
            timeManager.playerLogOn(playerUUID);
            LOGGER.info(player.getName() + "logged in but has already been added to the list. Changing to online");
        }
    }

    private static int tickCount = 0;
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if(tickCount % 20 == 0) {
                //Do player time managing
                for(Player p : event.getServer().getPlayerList().getPlayers()){
                    timeManager.updatePlayerTime(p.getUUID());
                    if(timeManager.checkForTimeout(p.getUUID())) {
                        LOGGER.info(p.getName() + "Has been timed out");
                        p.getServer().getPlayerList().getBans().add(new UserBanListEntry(p.getGameProfile()));
                    }
                }
                tickCount = 0;
            } else {
                if(timeManager.isResetTime()){ //Se ha alcanzado la hora de reseteo y se procede a resetear
                    LOGGER.info("Is ban reset time");
                    timeManager.resetAllTime();
                    for (UserBanListEntry p : event.getServer().getPlayerList().getBans().getEntries()) { //Iterar por
                        // todos los jugadores baneados y desbanear aquellas que no estén en la lista de muertos
                        if(deadPlayers.contains(p.getDisplayName())){
                            continue;
                        }
                        event.getServer().getPlayerList().getBans().remove(p);
                    }
                }
                tickCount++;
            }
        }
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof Player) {
            // Get the player who just respawned
            Player player = event.getEntity();
            //Ban and add to dead players list
            player.getServer().getPlayerList().getBans().add(new UserBanListEntry(player.getGameProfile()));
            deadPlayers.add(player.getDisplayName());
        }
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


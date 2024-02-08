package es.nullbyte.relativedimensions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.PvpManager;
import es.nullbyte.relativedimensions.charspvp.network.DailyTimeLimitHandler;
import es.nullbyte.relativedimensions.charspvp.network.ModMessages;
import es.nullbyte.relativedimensions.charspvp.network.PVPStateHandler;
import es.nullbyte.relativedimensions.charspvp.network.RemainingTimeHandler;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import es.nullbyte.relativedimensions.charspvp.commands.ListPlayersCommand;
import es.nullbyte.relativedimensions.charspvp.commands.DespawnChestCommand;
import es.nullbyte.relativedimensions.charspvp.commands.SpawnChestCommand;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.mgrcmds.modPVPcmd;
import es.nullbyte.relativedimensions.charspvp.PlayerTimeLimit.mgrcmds.modTimercmd;
import es.nullbyte.relativedimensions.events.OutOfBorderChecker;
import es.nullbyte.relativedimensions.charspvp.commands.WinnerEnabler;

import java.util.Random;

//TODO: Make lobby / colloseum for possible showdown  (New dimension?)


// The value here should match an entry in the META-INF/mods.toml file
@Mod(RelativeDimensionsMain.MOD_ID)
@Mod.EventBusSubscriber(modid = RelativeDimensionsMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RelativeDimensionsMain {


    // Define mod id in a common place for everything to reference.
    //TEst forGUI mac.
    public static final String MOD_ID = "relativedimensions";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int DEF_TIMELIMIT = 4*60*60; //4 hours
    public static final int DEF_RESETTIME = 6; //6am 35 minutes
    public static final Random RANDOM = new Random();
    public static final PlayerTimeManager timeManager = new PlayerTimeManager(DEF_TIMELIMIT,DEF_RESETTIME);
    public static final OutOfBorderChecker borderchecker = new OutOfBorderChecker(10);

    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public RelativeDimensionsMain() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Register the class on the event bus so any events it has will be called
        MinecraftForge.EVENT_BUS.register(PvpManager.class);


        //Add listeners for the events we want to listen to. Since this is not an item or blocck, that are managed in
        //The main class, we need to add the listeners here
        MinecraftForge.EVENT_BUS.addListener(PvpManager::onPlayerLoggedIn);
        MinecraftForge.EVENT_BUS.addListener(PvpManager::onLivingAttack);
        MinecraftForge.EVENT_BUS.addListener(this::onChatReceived);
        //MinecraftForge.EVENT_BUS.addListener(this::onPlayerLoggedIn);
        //MinecraftForge.EVENT_BUS.addListener(CustomFogRenderState::onFogDensity);
        //MinecraftForge.EVENT_BUS.addListener(CustomFogRenderState::onFogColors);

    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        //Registrar eventos encolados, includos los paquetes de red!
        event.enqueueWork(() -> {
            RemainingTimeHandler.register();
            PVPStateHandler.register();
            DailyTimeLimitHandler.register();
            ModMessages.register();
            //AberrantOreProxHandler.register();
        });
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
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
        LOGGER.info("[CHARMISCMODS - MAIN] Initial command registration");
        modPVPcmd.register(dispatcher);
        modTimercmd.register(dispatcher);
        SpawnChestCommand.register(dispatcher);
        DespawnChestCommand.register(dispatcher);
        ListPlayersCommand.register(dispatcher);
        WinnerEnabler.register(dispatcher);
    }

    //Disable join messages
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatReceived(ClientChatReceivedEvent event) {
        // Get the chat message text
        String message = event.getMessage().getString().trim();
        System.out.println("-------------------------------" + message);

        // Check if the message is the "player has joined" message
        if (message.equals(I18n.get("multiplayer.player.joined", event.getSender()))||message.equals(I18n.get("multiplayer.player.left", event.getSender()))) {
            // If the message is the "player has joined" or "player has left" message, cancel the event to prevent it from being displayed
            event.setCanceled(true);
        }
    }
}
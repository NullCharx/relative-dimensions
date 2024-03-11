package es.nullbyte.relativedimensions.items;

import es.nullbyte.relativedimensions.RelativeDimensionsMain;
import es.nullbyte.relativedimensions.blocks.BlockInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModTabs {
    //Deferred register to hold all the custom creative mode tabs
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MOD_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RelativeDimensionsMain.MOD_ID);

    //Avid TP items tab. The order in which the tabs are registered is the order they will appear in the creative menu
    //The order in which the items are registered is the order they will appear in the tab
    public static final RegistryObject<CreativeModeTab> AVID_TP_ITEMS = CREATIVE_MOD_TABS.register("avidtpitems",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.TRANSMAT_BEAM_EMITTER.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.avidtp")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab
                        pOutput.accept(ModItems.AVID_SDPT.get());
                        pOutput.accept(ModItems.TRANSMAT_BEAM_EMITTER.get());
                        //You can also add vanilla items to the tab
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> AVID_PVP_ITEMS = CREATIVE_MOD_TABS.register("avidpvpitems",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DISARMEDTEAM_TRACKER_COMPASS.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.avidpvp")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab
                        //Put the disarmed tracker compass ONLY
                        pOutput.accept(ModItems.TEAM_TRACKER_COMPASS.get());
                        pOutput.accept(ModItems.PLAYER_TRACKER_COMPASS.get());
                        pOutput.accept(ModItems.DISARMEDTEAM_TRACKER_COMPASS.get());
                        pOutput.accept(ModItems.DISARMEDPLAYER_TRACKER_COMPASS.get());
                        //You can also add vanilla items to the tab
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DIMENSIONAL_ENVIRONMENT = CREATIVE_MOD_TABS.register("dimensionalenvironment",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BlockInit.ABERRANT_GRASS.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.dimenv")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab. Items are ordered in the order they are added
                        pOutput.accept(ModItems.ABERRANT_PICK.get());
                        pOutput.accept(BlockInit.ABERRANT_ORE.get());
                        pOutput.accept(ModItems.ABERRANT_SHARD.get());
                        pOutput.accept(ModItems.ABERRANT_INGOT.get());
                        pOutput.accept(BlockInit.ABERRANT_BLOCK.get());
                        pOutput.accept(BlockInit.ABERRANT_MINERALOID.get());

                        pOutput.accept(ModItems.ABERRANT_AXE.get());
                        pOutput.accept(BlockInit.ABERRANT_LOG.get());
                        pOutput.accept(BlockInit.ABERRANT_WOOD.get());
                        pOutput.accept(BlockInit.STRIPPED_ABERRANT_LOG.get());
                        pOutput.accept(BlockInit.STRIPPED_ABERRANT_WOOD.get());
                        pOutput.accept(BlockInit.ABERRANT_PLANK.get());
                        pOutput.accept(BlockInit.ABERRANT_LEAVE.get());
                        pOutput.accept(BlockInit.ABERRANT_SAPLING.get());
                        pOutput.accept(ModItems.ABERRANT_STICK.get());

                        pOutput.accept(ModItems.ABERRANT_SWORD.get());

                        //pOutput.accept(ItemInit.ABERRANT_SHOVEL.get());
                        pOutput.accept(BlockInit.ABERRANT_GRASS.get());
                        pOutput.accept(BlockInit.ABERRANT_DIRT.get());
                        pOutput.accept(BlockInit.ABERRANT_SNOWY_GRASS.get());

                        //You can also add vanilla items to the tab
                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }


}
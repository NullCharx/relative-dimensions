package es.nullbyte.relativedimensions.utils;

import es.nullbyte.relativedimensions.RelativeDimensionsMain;
import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.init.ItemInit;
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
                    .icon(() -> new ItemStack(ItemInit.TRANSMAT_BEAM_EMITTER.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.avidtp")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab
                        pOutput.accept(ItemInit.AVID_SDPT.get());
                        pOutput.accept(ItemInit.TRANSMAT_BEAM_EMITTER.get());
                        //You can also add vanilla items to the tab
                    })
                    .build());
    public static final RegistryObject<CreativeModeTab> AVID_PVP_ITEMS = CREATIVE_MOD_TABS.register("avidpvpitems",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemInit.TEAM_TRACKER_COMPASS.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.avidpvp")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab
                        pOutput.accept(ItemInit.PLAYER_TRACKER_COMPASS.get());
                        pOutput.accept(ItemInit.TEAM_TRACKER_COMPASS.get());
                        //You can also add vanilla items to the tab
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> DIMENSIONAL_ENVIRONMENT = CREATIVE_MOD_TABS.register("dimensionalenvironment",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ItemInit.TEAM_TRACKER_COMPASS.get())) //Use one of the items as icon
                    .title(Component.translatable("creativetab.dimenv")) //Translation key for the tab name (lang file)
                    .displayItems((pParameters, pOutput) -> {   //List of items to display in the tab
                        pOutput.accept(ItemInit.ABERRANT_PICK.get());
                        pOutput.accept(BlockInit.ABERRANT_ORE.get());
                        pOutput.accept(ItemInit.ABERRANT_SHARD.get());
                        pOutput.accept(ItemInit.ABERRANT_INGOT.get());
                        pOutput.accept(BlockInit.ABERRANT_BLOCK.get());
                        pOutput.accept(BlockInit.ABERRANT_MINERALOID.get());

                        pOutput.accept(ItemInit.ABERRANT_SWORD.get());

                        //pOutput.accept(ItemInit.ABERRANT_AXE.get());
                        pOutput.accept(BlockInit.ABERRANT_LOG.get());
                        pOutput.accept(BlockInit.ABERRANT_WOOD.get());
                        pOutput.accept(BlockInit.STRIPPED_ABERRANT_LOG.get());
                        pOutput.accept(BlockInit.STRIPPED_ABERRANT_LOG.get());
                        pOutput.accept(BlockInit.ABERRANT_PLANKS.get());
                        pOutput.accept(BlockInit.ABERRANT_LEAVES.get());

                        //You can also add vanilla items to the tab
                    })
                    .build());
    public static void register(IEventBus eventBus) {
        CREATIVE_MOD_TABS.register(eventBus);
    }


}

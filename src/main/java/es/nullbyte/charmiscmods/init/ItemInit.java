package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
            () -> new Item(new Item.Properties().tab(ModCreativeTab.instance)));

    //AVID short distance matter particle transmitter
    public static final RegistryObject<Item> AVID_SDPT = ITEMS.register("avidsdpt",
            () -> new AvidShortDistanceParticleTransmitter(new Item.Properties().tab(ModCreativeTab.instance).durability(1000)));

    //Transmat beam generator
    public static final RegistryObject<Item> TRANSMAT_BEAM_EMITTER = ITEMS.register("transmatbeamemt",
            () -> new TransmatBeamEmitter(new Item.Properties().tab(ModCreativeTab.instance).durability(1000)));


    //BLOCK ITEMS
    public static final RegistryObject<Item> TESTBLOCK1ITEM = ITEMS.register("testblock",
            () -> new BlockItem(BlockInit.TESTBLOCK1.get(), new Item.Properties().tab(ModCreativeTab.instance)));
    public static class ModCreativeTab extends CreativeModeTab {
        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(testitem1.get());
        }

        public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "chrmscmds");

    }
}

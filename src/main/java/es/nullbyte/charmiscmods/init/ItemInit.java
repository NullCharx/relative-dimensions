package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    //https://moddingtutorials.org/basic-items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CharMiscModsMain.MOD_ID);
    public static final RegistryObject<Item> testitem1 = ITEMS.register("testitem1",
            () -> new Item(new Item.Properties().tab(ModCreativeTab.instance)));

    public static class ModCreativeTab extends CreativeModeTab {
        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(testitem1.get());
        }
        public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "firstmod");

    }
}

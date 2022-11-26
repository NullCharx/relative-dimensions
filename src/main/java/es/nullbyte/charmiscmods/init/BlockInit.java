package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    //https://moddingtutorials.org/basic-blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CharMiscModsMain.MOD_ID);

    //You can also use Block.Properties.copy(ANOTHER_BLOCK) to avoid writing things out repeatedly. All vanilla blocks
    // can be accessed with Blocks.INSERT_NAME_HERE so you can copy properties from one of them if you feel like it.
    // Or avoid redundancy by referencing YOUR_BLOCK.get()
    public static final RegistryObject<Block> TESTBLOCK1 = BLOCKS.register("testblock",
            () -> new Block(Block.Properties.of(Material.STONE).strength(4f, 1200f).requiresCorrectToolForDrops().lightLevel((state) -> 15)));

    // automatically creates items for all your blocks
    // you could do it manually instead by registering BlockItems in your ItemInit class
    /*@SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            // for each block we registered above...
            BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                // make an item properties object that puts it in your creative tab
                Item.Properties properties = new Item.Properties().tab(ItemInit.ModCreativeTab.instance);

                // make a block item that places the block
                // note, if you have a special block that needs a custom implementation for the BlockItem, just add an if statement here
                Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);

                // register the block item with the same name as the block
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }*/
}

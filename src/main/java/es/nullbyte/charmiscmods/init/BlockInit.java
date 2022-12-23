package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import es.nullbyte.charmiscmods.blocks.AdvTestBlock;
import es.nullbyte.charmiscmods.blocks.AVIDtsrail;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    //https://moddingtutorials.org/basic-blocks
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CharMiscModsMain.MOD_ID);

    //You can also use Block.Properties.copy(ANOTHER_BLOCK) to avoid writing things out repeatedly. All vanilla blocks
    // can be accessed with Blocks.INSERT_NAME_HERE so you can copy properties from one of them if you feel like it.
    // Or avoid redundancy by referencing YOUR_BLOCK.get()
    public static final RegistryObject<Block> TESTBLOCK1 = BLOCKS.register("testblock",
            () -> new Block(Block.Properties.of(Material.STONE).strength(4f, 1200f).requiresCorrectToolForDrops().lightLevel((state) -> 15)));

    public static final RegistryObject<Block> ADVANCEDTESTBLOCK = BLOCKS.register("advtestblock",
            () -> new AdvTestBlock(Block.Properties.copy(Blocks.DIRT)));

    public static final RegistryObject<Block> STRAIGHTRAIL = BLOCKS.register("tsrail",
            () -> new AVIDtsrail(Block.Properties.of(Material.HEAVY_METAL).noOcclusion().strength(1.5F, 6.0F).requiresCorrectToolForDrops(), true));

    @SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                Item.Properties properties = new Item.Properties().tab(ItemInit.ModCreativeTab.instance);
                Supplier<Item> blockItemFactory = () -> new BlockItem(block, properties);
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }

}

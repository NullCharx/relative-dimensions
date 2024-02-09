package es.nullbyte.relativedimensions.blocks;

import es.nullbyte.relativedimensions.RelativeDimensionsMain;
import es.nullbyte.relativedimensions.blocks.aberrant.aberrantMineraloid;
import es.nullbyte.relativedimensions.blocks.aberrant.aberrantOre;
import es.nullbyte.relativedimensions.blocks.flammables.ModFlammableRotatedPillarBlock;
import es.nullbyte.relativedimensions.blocks.flammables.ModFlammableRotatedPlank;
import es.nullbyte.relativedimensions.items.init.ItemInit;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    //DeferredRegister for blocks
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RelativeDimensionsMain.MOD_ID);

    public static final RegistryObject<Block> ABERRANT_BLOCK = registerBlock("aberrant_block",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistryObject<Block> ABERRANT_ORE = registerBlock("aberrant_ore",
            () -> new aberrantOre(Block.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> ABERRANT_MINERALOID = registerBlock("aberrant_mineraloid",
            () -> new aberrantMineraloid(Block.Properties.ofFullCopy(Blocks.IRON_ORE)));


    public static final RegistryObject<Block> ABERRANT_LOG = registerBlock("aberrant_log",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> ABERRANT_WOOD = registerBlock("aberrant_wood",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_ABERRANT_LOG = registerBlock("stripped_aberrant_log",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_ABERRANT_WOOD = registerBlock("stripped_aberrant_wood",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> ABERRANT_PLANKS = registerBlock("aberrant_planks",
            () -> new ModFlammableRotatedPlank(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ABERRANT_LEAVES = registerBlock("aberrant_leaves",
            () -> new LeavesBlock(Block.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        //T is the block type. It will register the block and the block item.
        RegistryObject<T> returnBlock = BLOCKS.register(name, block); //Register the block
        registerBlockItem(name, returnBlock); //Register the block item
        return returnBlock; //Return the block
    }
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        //T is the block type. It will register the block and the block item.
        return ItemInit.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
    public  static  void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }


}

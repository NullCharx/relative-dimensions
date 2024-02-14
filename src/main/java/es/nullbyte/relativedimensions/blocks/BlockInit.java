package es.nullbyte.relativedimensions.blocks;

import es.nullbyte.relativedimensions.RelativeDimensionsMain;
import es.nullbyte.relativedimensions.blocks.aberrant.AberrantGrass;
import es.nullbyte.relativedimensions.blocks.aberrant.AberrantSapling;
import es.nullbyte.relativedimensions.blocks.aberrant.aberrantMineraloid;
import es.nullbyte.relativedimensions.blocks.aberrant.aberrantOre;
import es.nullbyte.relativedimensions.blocks.flammables.ModFlammableRotatedPillarBlock;
import es.nullbyte.relativedimensions.blocks.flammables.ModFlammableRotatedPlank;
import es.nullbyte.relativedimensions.items.ItemInit;
import es.nullbyte.relativedimensions.worldgen.treegen.AberrantTreeGrower;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    //DeferredRegister for blocks
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RelativeDimensionsMain.MOD_ID);

    //Aberrant mineral blocks
    public static final RegistryObject<Block> ABERRANT_BLOCK = registerBlock("aberrant_block",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistryObject<Block> ABERRANT_ORE = registerBlock("aberrant_ore",
            () -> new aberrantOre(Block.Properties.ofFullCopy(Blocks.IRON_ORE)));
    public static final RegistryObject<Block> ABERRANT_MINERALOID = registerBlock("aberrant_mineraloid",
            () -> new aberrantMineraloid(Block.Properties.ofFullCopy(Blocks.IRON_ORE)));

    //Aberrant natural blocks

    //---Wood
    public static final RegistryObject<Block> ABERRANT_SAPLING = registerBlock("aberrant_sapling",
            () -> new AberrantSapling(new AberrantTreeGrower().getGrowerOfAberrantTree(), Block.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final RegistryObject<Block> ABERRANT_LOG = registerBlock("aberrant_log",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_LOG).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 5)));
    public static final RegistryObject<Block> ABERRANT_PLANK = registerBlock("aberrant_plank",
            () -> new ModFlammableRotatedPlank(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 1)));
    public static final RegistryObject<Block> ABERRANT_WOOD = registerBlock("aberrant_wood",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.OAK_WOOD).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 5)));
    public static final RegistryObject<Block> STRIPPED_ABERRANT_LOG = registerBlock("stripped_aberrant_log",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 8)));
    public static final RegistryObject<Block> STRIPPED_ABERRANT_WOOD = registerBlock("stripped_aberrant_wood",
            () -> new ModFlammableRotatedPillarBlock(Block.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 8)));
    public static final RegistryObject<Block> ABERRANT_LEAVE = registerBlock("aberrant_leave",
            () -> new LeavesBlock(Block.Properties.ofFullCopy(Blocks.OAK_LEAVES).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 15)));

    //---Grass and dirt
    public static final RegistryObject<Block> ABERRANT_GRASS = registerBlock("aberrant_grass",
            () -> new AberrantGrass(Block.Properties.ofFullCopy(Blocks.GRASS_BLOCK).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 3)));
    public static final RegistryObject<Block> ABERRANT_DIRT = registerBlock("aberrant_dirt",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.GRASS_BLOCK).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 1)));
    public static final RegistryObject<Block> ABERRANT_SNOWY_GRASS = registerBlock("aberrant_snowy_grass",
            () -> new SnowyDirtBlock(Block.Properties.ofFullCopy(Blocks.GRASS_BLOCK).emissiveRendering((state, world, pos) -> true)
                    .lightLevel((light) -> 1)));
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

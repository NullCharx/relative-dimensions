package es.nullbyte.charmiscmods.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import es.nullbyte.charmiscmods.blocks.AdvTestBlock;
import es.nullbyte.charmiscmods.blocks.MobSlayerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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

    public static final RegistryObject<Block> MOB_SLAYER = BLOCKS.register("mob_slayer",
            () -> new MobSlayerBlock(Block.Properties.copy(Blocks.IRON_BLOCK)));
}

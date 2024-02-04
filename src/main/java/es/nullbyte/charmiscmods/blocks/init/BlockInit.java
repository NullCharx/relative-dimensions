package es.nullbyte.charmiscmods.blocks.init;

import es.nullbyte.charmiscmods.CharMiscModsMain;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CharMiscModsMain.MOD_ID);
}

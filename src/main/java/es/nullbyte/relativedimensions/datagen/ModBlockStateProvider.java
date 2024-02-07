package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.blocks.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output,MOD_ID,existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlockInit.ABERRANT_BLOCK);
        blockWithItem(BlockInit.ABERRANT_ORE);

    }

    //Simple registry pass object block so you dont need the obj.
    private void blockWithItem(RegistryObject<Block> blockRegistryObj) {
        simpleBlock(blockRegistryObj.get(), cubeAll(blockRegistryObj.get()));
    }
}

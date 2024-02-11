package es.nullbyte.relativedimensions.datagen.blockproviders;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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
        blockWithItem(BlockInit.ABERRANT_MINERALOID);

        logBlock(BlockInit.ABERRANT_LOG);
        blockWithItem(BlockInit.ABERRANT_PLANK);
        blockWithItem(BlockInit.ABERRANT_LEAVE);
        blockWithItem(BlockInit.ABERRANT_WOOD);
        blockWithItem(BlockInit.STRIPPED_ABERRANT_WOOD);
        logBlock(BlockInit.STRIPPED_ABERRANT_LOG);
        blockWithItem(BlockInit.ABERRANT_SAPLING); //Cross block model ? xD lol



    }

    //Simple registry pass object block so you dont need the obj.
    private void blockWithItem(RegistryObject<Block> blockRegistryObj) {
        simpleBlock(blockRegistryObj.get(), cubeAll(blockRegistryObj.get()));
    }

    //Column type block, specifically logs
    private void logBlock(RegistryObject<Block> blockRegistryObj) {
        String blockName = blockRegistryObj.getId().getPath();
        if(blockRegistryObj.get() instanceof RotatedPillarBlock) {
            // Use withExistingParent to inherit from the built-in cube_column model
            // and specify custom textures for the side and end
            //Parent cube_column which allows for two textures to be used for the top/bottom and sides of the log
            ModelFile logModel = models().withExistingParent(blockName, "minecraft:block/cube_column")
                    .texture("end", modLoc("block/" + blockName + "_top")) // For the top and bottom of the log
                    .texture("side", modLoc("block/" + blockName)); // For the sides of the log

            //rotation handling
            getVariantBuilder(blockRegistryObj.get()).forAllStates(state -> {
                var axis = state.getValue(RotatedPillarBlock.AXIS);
                return ConfiguredModel.builder()
                        .modelFile(logModel)
                        .rotationX(axis == Direction.Axis.X ? 90 : 0)
                        .rotationY(axis == Direction.Axis.Y ? 0 : axis == Direction.Axis.Z ? 90 : 0)
                        .build();
            });
        } else {
            blockWithItem(blockRegistryObj);
        }
    }
}

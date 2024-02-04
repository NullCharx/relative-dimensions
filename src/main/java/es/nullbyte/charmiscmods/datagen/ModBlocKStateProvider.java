package es.nullbyte.charmiscmods.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class ModBlocKStateProvider extends BlockStateProvider {
    public ModBlocKStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output,MOD_ID,existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //You dont have blocks yet, so this is empty
        //blockWithItem(blockInit.EXAMPLE_BLOCK);
    }

    //Simple registry pass object block so you dont need the obj.
    private void blockWithItem(RegistryObject<Block> blockRegistryObj) {
        simpleBlock(blockRegistryObj.get(), cubeAll(blockRegistryObj.get()));
    }
}

package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.blocks.init.BlockInit;
import es.nullbyte.relativedimensions.utils.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModBlockTagGenerator extends BlockTagsProvider {
public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MOD_ID, existingFileHelper);
    }
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        //Generated block tags on the form
        //this.tag(vanilla or custom tag)
        //        .add(vanilla or custom block);
        //
        //Example to add a NEEDS_DIAMOND_TOOL tag to an example block
        //this.tag(BlockTags.NEEDS_DIAMOND_TOOL.get)
        //        .add(blockInit.EXAMPLE_BLOCK.get())
        //        .add(blockInit.EXAMPLE_BLOCK_2.get()
        //Add blocks here );

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockInit.ABERRANT_ORE.get())
                .add(BlockInit.ABERRANT_BLOCK.get()
                );
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockInit.ABERRANT_ORE.get())
                .add(BlockInit.ABERRANT_BLOCK.get()
                );
        //Empty
        this.tag(ModTags.Blocks.NEED_ABERRANT_TOOL);

    }
}

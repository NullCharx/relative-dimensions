package es.nullbyte.relativedimensions.datagen.blockproviders;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.blocks.ModBlockTags;
import es.nullbyte.relativedimensions.items.utils.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
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
                .add(BlockInit.ABERRANT_BLOCK.get())
                .add(BlockInit.ABERRANT_MINERALOID.get()
                );
        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockInit.ABERRANT_LOG.get())
                .add(BlockInit.ABERRANT_WOOD.get())
                .add(BlockInit.ABERRANT_PLANK.get())
                .add(BlockInit.ABERRANT_LEAVE.get())
                .add(BlockInit.STRIPPED_ABERRANT_LOG.get())
                .add(BlockInit.STRIPPED_ABERRANT_WOOD.get());

        this.tag(BlockTags.LEAVES)
                .add(BlockInit.ABERRANT_LEAVE.get());
        this.tag(BlockTags.LOGS)
                .add(BlockInit.ABERRANT_LOG.get())
                .add(BlockInit.STRIPPED_ABERRANT_LOG.get());
        this.tag(BlockTags.PLANKS)
                .add(BlockInit.ABERRANT_PLANK.get());

        //Empty
        this.tag(ModBlockTags.ABERRANT_BLOCK)
                .add(BlockInit.ABERRANT_BLOCK.get())
                .add(BlockInit.ABERRANT_ORE.get())
                .add(BlockInit.ABERRANT_MINERALOID.get())
                .add(BlockInit.ABERRANT_PLANK.get())
                .add(BlockInit.ABERRANT_LOG.get())
                .add(BlockInit.ABERRANT_WOOD.get())
                .add(BlockInit.ABERRANT_LEAVE.get())
                .add(BlockInit.STRIPPED_ABERRANT_LOG.get())
                .add(BlockInit.STRIPPED_ABERRANT_WOOD.get());
                //.add(BlockInit.ABERRANT_SAPLING.get());


        this.tag(Tags.Blocks.ORES)
                .add(BlockInit.ABERRANT_ORE.get());

    }
}

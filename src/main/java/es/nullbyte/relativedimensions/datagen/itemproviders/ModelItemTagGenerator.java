package es.nullbyte.relativedimensions.datagen.itemproviders;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ItemInit;
import es.nullbyte.relativedimensions.items.utils.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModelItemTagGenerator extends ItemTagsProvider {

    public ModelItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<Block>> lookupBlock, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookupBlock, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {

        //Add aberrant items to the custom aberrant_item tag
        this.tag(ModItemTags.ABERRANT_ITEM)
                .add(ItemInit.ABERRANT_PICK.get())
                .add(ItemInit.ABERRANT_AXE.get())
                .add(ItemInit.ABERRANT_SWORD.get()
                );

        this.tag(ItemTags.TOOLS)
                .add(ItemInit.ABERRANT_PICK.get())
                .add(ItemInit.ABERRANT_AXE.get())
                .add(ItemInit.ABERRANT_SWORD.get()
                );


    }
}
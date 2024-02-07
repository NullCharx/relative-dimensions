package es.nullbyte.charmiscmods.datagen;

import es.nullbyte.charmiscmods.items.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

public class ModelItemTagGenerator extends ItemTagsProvider {

    public ModelItemTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<Block>> lookupBlock, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, lookupBlock, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {

    }
}
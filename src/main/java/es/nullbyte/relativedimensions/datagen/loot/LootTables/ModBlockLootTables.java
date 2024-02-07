package es.nullbyte.relativedimensions.datagen.loot.LootTables;

import es.nullbyte.relativedimensions.blocks.init.BlockInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables(){
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        //Add blocks registerd on block register
        //Drop self means the block will drop itself upon looting method
        //An example of this would be, iron/copper/diamond *blocks* (made with 9 source materials)
        //Upon being correctly mined, they will drop an instance of themselves to the ground
        //this.dropSelf(blockInit.BLOCK1.get());

        //For ore drops you can check BlockLootSubProvider and copy (not override) any method for custom ore drops::
        //This will, for BLOCK2, create a loot table that will drop between 2 and 5 instances of CUSTOM_ITEM
        //As per generated createCustomOreDrop method below
        //this.add (blockInit.BLOCK2.get(),
                // block -> createCustomOreDrop(blockInit.BLOCK2.get(), ItemInit.CUSTOM_ITEM.get()));

        //Remember to add the ores to the tag MINEABLE_WITH_[TOOL] to make them mineable with the correct tool or else
        //They wont drop anything even if their loot table is correctly set up

        //When mining ABERRANT_ORE, it will drop between none and 2 instances of ABERRANT_SHARD
        this.add(BlockInit.ABERRANT_ORE.get(),
                block -> createCustomOreDrop(BlockInit.ABERRANT_ORE.get(), BlockInit.ABERRANT_ORE.get().asItem(), 0.0F, 3.0F));

        //When mining ABERRANT_BLOCK, it will drop an instance of itself
        this.dropSelf(BlockInit.ABERRANT_BLOCK.get());

    }

    //Direct copy of createOreDrops from BlockLootSubProvider
    //This method is used to create the loot table for ores. It will:
    //1. Create a loot table for the block
    //2. Apply explosion decay (if the block is destroyed by an explosion, it will drop less items)
    //3. Apply silk touch dispatch table (if the block is mined with silk touch, it will drop the block itself)
    //4. Apply the loot item (the item that will be dropped)
    //5. Apply the set count function (the amount of items that will be dropped, in this case, between 2 and 5 items)
    //6. Apply the apply bonus count function (if the block is mined with fortune, it will drop more items)
    protected LootTable.Builder createCustomOreDrop(Block block, Item dropItem, float minDrop, float maxDrop) {
        return createSilkTouchDispatchTable(block,
                this.applyExplosionDecay(block,
                        LootItem.lootTableItem(dropItem)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrop, maxDrop)))
                                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    //You can create modifications of BlockLootSubProvider methods to create custom loot tables and add them to the loot table provider
    @Override
    protected Iterable<Block> getKnownBlocks() {
        //Add loot tables for all the blocks added via blockInit Deferred Register (main Block register)
        //It will ignore any block with .noLootTable() in its properties
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}
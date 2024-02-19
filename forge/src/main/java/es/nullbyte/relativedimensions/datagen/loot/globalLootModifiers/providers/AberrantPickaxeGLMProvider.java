package es.nullbyte.relativedimensions.datagen.loot.globalLootModifiers.providers;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.datagen.loot.globalLootModifiers.LootModifiers.PickaxeLootModifiers.MiningAberrantismModifier;
import es.nullbyte.relativedimensions.datagen.loot.globalLootModifiers.LootModifiers.PickaxeLootModifiers.MiningDimensionalShiftModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class AberrantPickaxeGLMProvider extends GlobalLootModifierProvider {
    public AberrantPickaxeGLMProvider(PackOutput output) {
        super(output, MOD_ID);
    }

    @Override
    protected void start() {
        //This where you modify the loot tables

        //This will add a 35% chance to drop the item SOMEITEM when the block is the block state property
       /** add("item_loot_modifier", new AddItemModifier(new LootItemCondition[]{
                LootItemRandomChanceCondition.randomChance(0.35f).build()}, Items.SOMEITEM.get()
        ));*/

       //This will add a 15% chance to drop the item ABERRANT_ORE when mining an ore or any other pickaxe mineable block
        // with the tool ABERRANT_PICKAXE
       add("mining_aberrantism_modifier", new MiningAberrantismModifier(new LootItemCondition[]{
                LootItemRandomChanceCondition.randomChance(0.15f).build()}, BlockInit.ABERRANT_ORE.get().asItem())
               );

        //This will add a 5% chance to drop a random item from the game when mining an ore with the tool ABERRANT_PICK
        //The tiers and items are implemented inside the MiningDimensionalShiftModifier class
        add("mining_dimshift_modifier", new MiningDimensionalShiftModifier(new LootItemCondition[]{
                LootItemRandomChanceCondition.randomChance(0.05f).build()}, BlockInit.ABERRANT_ORE.get().asItem()));
    }

}
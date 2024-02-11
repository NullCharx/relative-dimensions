package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    //Generate a list of items that can be smelted to get item1
    //private statif final List<ItemLike> ITEM1_SMELTABLES = List.of(BlockInit.BLOCK1ORE.get(), BlockInit.BLOCK1.get());
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }
    @Override
    protected void buildRecipes(RecipeOutput pRecipeOutput) {
        //Generate smelting and blasting recipes for item1. All the items in the list will have a generated recipe to get item1
        //oreSmelting(pRecipeOutput, ITEM1_SMELTABLES, RecipeCategory.MISC, ItemInit.ITEM1.get(), 0.7F, 200, "item1");
        //oreBlasting(pRecipeOutput, ITEM1_SMELTABLES, RecipeCategory.MISC, ItemInit.ITEM1.get(), 0.7F, 188, "item1");

        /**
        //Recipe in which items have to be arranged in a certain way to get item2
        //This example creates a solid block of item 2 from 9 item 1
        //The criteria for unlocking the recipe (unlocked_by) is that the player has item 1
        // (for example, you unlock stone tools when you have cobblestone)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemInit.ITEM2.get()) //The recipe is of type MISC and the result is item2
            .pattern("###") //The pattern is a 3x3 grid filled with item1
            .pattern("###")
            .pattern("###")
            .define('#', ItemInit.ITEM1.get()).unlockedBy(ItemInit.ITEM1.get(), has(ItemInit.ITEM1.get())) //The item2 recipe is unlocked by having item1
            .save(pRecipeOutput); //Save the recipe to the output
         **/

        /**
        //Recipe in which the arrangement of items does not matter to get an item
        //In this example we do the reverse of the previous recipe, we get 9 item1 from item2 solid block
        //it does not matter where you place the solid block of item2 in the grid, it will yield 9 item1
        //The criteria for unlocking the recipe (unlocked_by) is that the player has item2
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.ITEM1.get(), 9) //The recipe is of type MISC and the result is 9 item1
                .requires(ItemInit.ITEM2.get())
                .unlockedBy(ItemInit.ITEM2.get(), has(ItemInit.ITEM2.get()) //The item1 recipe is unlocked by having item2
                .save(pRecipeOutput); //Save the recipe to the output

        **/
        //Alwais add custom ids at the save method. In the instances of an item having multiple recipes, the id will be the same, and runData will error out


        List<ItemLike> ingridients = List.of(BlockInit.ABERRANT_ORE.get().asItem());
        oreSmelting(pRecipeOutput, ingridients, RecipeCategory.MISC, ItemInit.ABERRANT_SHARD.get(), 0.5F, 200, MOD_ID + ":aberrant_ingot_smelt");
        oreBlasting(pRecipeOutput, ingridients, RecipeCategory.MISC, ItemInit.ABERRANT_SHARD.get(), 0.7F, 120, MOD_ID + ":aberrant_ingot_blast");

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.ABERRANT_INGOT.get()) //The recipe is of type MISC and the result is item2
                .pattern("###")
                .define('#', ItemInit.ABERRANT_SHARD.get()).unlockedBy(ItemInit.ABERRANT_SHARD.get().toString(), has(ItemInit.ABERRANT_SHARD.get())) //The item2 recipe is unlocked by having item1
                .save(pRecipeOutput, MOD_ID + ":aberrant_ingot_from_shard"); //Save the recipe to the output
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockInit.ABERRANT_BLOCK.get()) //The recipe is of type MISC and the result is item2
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .define('#', ItemInit.ABERRANT_INGOT.get()).unlockedBy(ItemInit.ABERRANT_INGOT.get().toString(), has(ItemInit.ABERRANT_INGOT.get())) //The item2 recipe is unlocked by having item1
                .save(pRecipeOutput, MOD_ID + ":aberrant_block_from_ingot"); //Save the recipe to the output
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.ABERRANT_SWORD.get()) //The recipe is of type MISC and the result is item2
                .pattern(" # ")
                .pattern(" # ")
                .pattern(" - ")
                .define('#', ItemInit.ABERRANT_INGOT.get()).unlockedBy(ItemInit.ABERRANT_INGOT.get().toString(), has(ItemInit.ABERRANT_INGOT.get()))
                .define('-', ItemInit.ABERRANT_STICK.get()).unlockedBy(ItemInit.ABERRANT_STICK.get().toString(), has(ItemInit.ABERRANT_STICK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_sword_vanilla");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.ABERRANT_PICK.get()) //The recipe is of type MISC and the result is item2
                .pattern("###")
                .pattern(" - ")
                .pattern(" - ")
                .define('#', ItemInit.ABERRANT_INGOT.get()).unlockedBy(ItemInit.ABERRANT_INGOT.get().toString(), has(ItemInit.ABERRANT_INGOT.get()))
                .define('-', ItemInit.ABERRANT_STICK.get()).unlockedBy(ItemInit.ABERRANT_STICK.get().toString(), has(ItemInit.ABERRANT_STICK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_pickaxe_vanilla");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.ABERRANT_AXE.get()) //The recipe is of type MISC and the result is item2
                .pattern("## ")
                .pattern("#- ")
                .pattern(" - ")
                .define('#', ItemInit.ABERRANT_INGOT.get()).unlockedBy(ItemInit.ABERRANT_INGOT.get().toString(), has(ItemInit.ABERRANT_INGOT.get()))
                .define('-', ItemInit.ABERRANT_STICK.get()).unlockedBy(ItemInit.ABERRANT_STICK.get().toString(), has(ItemInit.ABERRANT_STICK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_axe_vanilla_left");
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ItemInit.ABERRANT_AXE.get()) //The recipe is of type MISC and the result is item2
                .pattern(" ##")
                .pattern(" -#")
                .pattern(" - ")
                .define('#', ItemInit.ABERRANT_INGOT.get()).unlockedBy(ItemInit.ABERRANT_INGOT.get().toString(), has(ItemInit.ABERRANT_INGOT.get()))
                .define('-', ItemInit.ABERRANT_STICK.get()).unlockedBy(ItemInit.ABERRANT_STICK.get().toString(), has(ItemInit.ABERRANT_STICK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_axe_vanilla_right");
        //Save the recipe to the output
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ItemInit.ABERRANT_STICK.get(), 4)
                .requires(BlockInit.ABERRANT_PLANK.get(),2)
                .unlockedBy(BlockInit.ABERRANT_PLANK.get().toString(), has(BlockInit.ABERRANT_PLANK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_stick_from_plank");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ItemInit.ABERRANT_INGOT.get(), 9)
                .requires(BlockInit.ABERRANT_BLOCK.get())
                .unlockedBy(BlockInit.ABERRANT_BLOCK.get().toString(), has(BlockInit.ABERRANT_BLOCK.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_ingot_from_block");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.ABERRANT_PLANK.get(), 4)
                .requires(BlockInit.ABERRANT_LOG.get())
                .unlockedBy(BlockInit.ABERRANT_LOG.get().toString(), has(BlockInit.ABERRANT_LOG.get()))
                .save(pRecipeOutput, MOD_ID + ":aberrant_plank_from_log");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.ABERRANT_WOOD.get(), 3)
                .requires(BlockInit.ABERRANT_LOG.get(), 4)
                .unlockedBy(BlockInit.ABERRANT_LOG.get().toString(), has(BlockInit.ABERRANT_LOG.get())) //The item1 recipe is unlocked by having item2
                .save(pRecipeOutput, MOD_ID + ":aberrant_wood_from_log");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BlockInit.STRIPPED_ABERRANT_WOOD.get(), 3)
                .requires(BlockInit.STRIPPED_ABERRANT_LOG.get(), 4)
                .unlockedBy(BlockInit.STRIPPED_ABERRANT_LOG.get().toString(), has(BlockInit.STRIPPED_ABERRANT_LOG.get())) //The item1 recipe is unlocked by having item2
                .save(pRecipeOutput, MOD_ID + ":stripepd_aberrant_wood_from_stripepd_log");
    }

    //Due to hardcoded constraint, ore smelting, blasting and cooking must be copied from the original RecipeProvider
    //(1.20.4 changes Consumer<FinishedRecipe> to RecipeOutput)
    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> ingredientsList, RecipeCategory recipeCategory, ItemLike recipeResult, float recipeExp, int recipeTime, String recipeID) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredientsList, recipeCategory, recipeResult, recipeExp, recipeTime, recipeID, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredientsList, RecipeCategory recipeCategory, ItemLike recipeResult, float recpieExp, int recipeTime, String recipeID) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredientsList, recipeCategory, recipeResult, recpieExp, recipeTime, recipeID, "_from_blasting");
    }
    private static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> recipeSerializer, AbstractCookingRecipe.Factory<T> recipieFact, List<ItemLike> recipeIngredients, RecipeCategory recipeCategory, ItemLike recipeResult, float recipeExp, int recipeTime, String recipeID, String recipeName) {
        for(ItemLike itemlike : recipeIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), recipeCategory, recipeResult, recipeExp, recipeTime,
                    recipeSerializer, recipieFact).group(recipeID).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, MOD_ID + ":" + getItemName(recipeResult) + recipeName + "_" + getItemName(itemlike));
        }

    }

}

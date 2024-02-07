package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.blocks.init.BlockInit;
import es.nullbyte.relativedimensions.items.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output,MOD_ID,existingFileHelper);
    }
    @Override
    protected void registerModels() {
        handheldRodItem(ItemInit.AVID_SDPT); //Avid SDPT
        handheldItem(ItemInit.TRANSMAT_BEAM_EMITTER);   //Transmat Beam Emitter
        simpleItem(ItemInit.TESTITEM1); //Test item 1
        generateCompassTextures(); //Generates the tracker compass texture states (both normal and teamed)
        generateCompassModel(ItemInit.PLAYER_TRACKER_COMPASS, 32, "compassstate"); //Generates the tracker compass model
        generateCompassModel(ItemInit.TEAM_TRACKER_COMPASS, 32, "tcompassstate"); //Generates the tracker compass model
        simpleItem(ItemInit.ABERRANT_SHARD); //Aberrant shard
        simpleItem(ItemInit.ABERRANT_INGOT); //Aberrant ingot
        handheldItem(ItemInit.ABERRANT_SWORD); //Aberrant sword
        handheldItem(ItemInit.ABERRANT_PICK); //Aberrant pickaxe
        blockItem(BlockInit.ABERRANT_BLOCK); //Aberrant block block item
        blockItem(BlockInit.ABERRANT_ORE); //Aberrant ore block item
    }

    private ItemModelBuilder blockItem(RegistryObject<Block> block) {
        return withExistingParent(block.getId().getPath(),
                new ResourceLocation(MOD_ID, "block/" + block.getId().getPath()));
    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(MOD_ID,"item/" + item.getId().getPath()));
    }
    private ItemModelBuilder handheldRodItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld_rod")).texture("layer0",
                new ResourceLocation(MOD_ID,"item/" + item.getId().getPath()));
    }



    //--Custom methods-----
    //The base of the folder system is models. You have to manually add the items
    private void generateCompassTextures() {
        for (int i = 0; i <= 31; i++) {
            String modelName = "trackercompass_" + String.format("%02d", i);
            withExistingParent( "item/compassstate/" + modelName, mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/compassstate/" + modelName));
        }
        withExistingParent( "item/compassstate/" + "compass_disarmed", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/compassstate/" + "compass_disarmed"));

        for (int i = 0; i <= 31; i++) {
            String modelName = "teamtrackercompass_" + String.format("%02d", i);
            withExistingParent( "item/tcompassstate/" + modelName, mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/tcompassstate/" + modelName));
        }
        withExistingParent( "item/tcompassstate/" + "tcompass_disarmed", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/tcompassstate/" + "tcompass_disarmed"));
    }

    private void generateCompassModel(RegistryObject<Item> item, int states, String folder) {

        ItemModelBuilder modelBuilder =  withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MOD_ID,"item/" + item.getId().getPath()));

        if (folder.equals("compassstate")) {
            modelBuilder.override()
                    .predicate(new ResourceLocation("custom_model_data"), 0)
                    .model(getExistingFile(modLoc("item/compassstate/compass_disarmed")));
        } else {
            modelBuilder.override()
                    .predicate(new ResourceLocation("custom_model_data"), 0)
                    .model(getExistingFile(modLoc("item/tcompassstate/tcompass_disarmed")));
        }

        for (int i = 1; i < states; i++) {
            String number = String.format("%02d", i); // Formats the number with leading zeros
            modelBuilder.override()
                    .predicate(new ResourceLocation("custom_model_data"), i)
                    .model(getExistingFile(modLoc("item/" + folder + "/" + item.getId().getPath() + "_" + number)));
        }
    }
}


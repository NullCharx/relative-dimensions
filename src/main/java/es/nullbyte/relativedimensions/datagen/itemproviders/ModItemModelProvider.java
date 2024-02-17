package es.nullbyte.relativedimensions.datagen.itemproviders;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ModItems;
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
        handheldRodItem(ModItems.AVID_SDPT); //Avid SDPT
        handheldItem(ModItems.TRANSMAT_BEAM_EMITTER);   //Transmat Beam Emitter
        simpleItem(ModItems.TESTITEM1); //Test item 1
        generateCompassTextures(); //Generates the tracker compass texture states (both normal and teamed)
        disarmedCompass(ModItems.DISARMEDPLAYER_TRACKER_COMPASS); //Disarmed tracker compass
        disarmedCompass(ModItems.DISARMEDTEAM_TRACKER_COMPASS); //Disarmed team tracker compass
        generateCompassModel(ModItems.PLAYER_TRACKER_COMPASS, "compassstate"); //Generates the tracker compass model
        generateCompassModel(ModItems.TEAM_TRACKER_COMPASS, "tcompassstate"); //Generates the tracker compass model

        simpleItem(ModItems.ABERRANT_STICK); //Aberrant stick

        handheldItem(ModItems.ABERRANT_PICK); //Aberrant pickaxe
        simpleItem(ModItems.ABERRANT_SHARD); //Aberrant shard
        simpleItem(ModItems.ABERRANT_INGOT); //Aberrant ingot
        handheldItem(ModItems.ABERRANT_SWORD); //Aberrant sword
        blockItem(BlockInit.ABERRANT_BLOCK); //Aberrant block block item
        blockItem(BlockInit.ABERRANT_ORE); //Aberrant ore block item
        blockItem(BlockInit.ABERRANT_MINERALOID); //Aberrant mineraloid block item

        handheldItem(ModItems.ABERRANT_AXE); //Aberrant axe
        blockItem(BlockInit.ABERRANT_LOG); //Aberrant log block item
        blockItem(BlockInit.ABERRANT_WOOD); //Aberrant wood block item
        blockItem(BlockInit.ABERRANT_PLANK); //Aberrant plank block item
        blockItem(BlockInit.ABERRANT_LEAVE); //Aberrant leave block item
        blockItem(BlockInit.STRIPPED_ABERRANT_LOG); //Stripped aberrant log block item
        blockItem(BlockInit.STRIPPED_ABERRANT_WOOD); //Stripped aberrant wood block item
        blockItem(BlockInit.ABERRANT_SAPLING); //Aberrant sapling block item

        blockItem(BlockInit.ABERRANT_GRASS); //Aberrant grass block item
        blockItem(BlockInit.ABERRANT_DIRT); //Aberrant dirt block item
        blockItem(BlockInit.ABERRANT_SNOWY_GRASS); //Aberrant snowy grass block item
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

    private ItemModelBuilder disarmedCompass(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MOD_ID,"item/compass_disarmed"));
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
        withExistingParent( "item/compassstate/" + "compass_disarmed", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/compass_disarmed"));
        for (int i = 0; i <= 31; i++) {
            String modelName = "trackercompass_" + String.format("%02d", i);
            withExistingParent( "item/compassstate/" + modelName, mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/compassstate/" + modelName));
        }

        withExistingParent( "item/tcompassstate/" + "tcompass_disarmed", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/compass_disarmed"));
        for (int i = 0; i <= 31; i++) {
            String modelName = "teamtrackercompass_" + String.format("%02d", i);
            withExistingParent( "item/tcompassstate/" + modelName, mcLoc("item/generated"))
                    .texture("layer0", modLoc("item/tcompassstate/" + modelName));
        }

    }


    private void generateCompassModel(RegistryObject<Item> item, String folder) {
        // Initialize the model builder with the parent model and texture for disarmed state
        ItemModelBuilder modelBuilder = withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MOD_ID, "item/compass_disarmed"));

        // Define the specific angles and corresponding model names as provided
        float[] angles = {
                0.000000f, 0.015625f, 0.046875f, 0.078125f, 0.109375f, 0.140625f, 0.171875f, 0.203125f,
                0.234375f, 0.265625f, 0.296875f, 0.328125f, 0.359375f, 0.390625f, 0.421875f, 0.453125f,
                0.484375f, 0.515625f, 0.546875f, 0.578125f, 0.609375f, 0.640625f, 0.671875f, 0.703125f,
                0.734375f, 0.765625f, 0.796875f, 0.828125f, 0.859375f, 0.890625f, 0.921875f, 0.953125f, 0.984375f
        };
        String prefix = folder.equals("compassstate") ? "trackercompass_" : "teamtrackercompass_";

        // Generate model overrides based on angles
        for (int i = 0; i < angles.length; i++) {
            // Correcting the model name to use the provided scheme, starting from 16 and wrapping to 00 after 31
            String modelName = prefix + String.format("%02d", (i + 16) % 32);
            modelBuilder.override()
                    .predicate(new ResourceLocation("angle"), angles[i])
                    .model(getExistingFile(new ResourceLocation(MOD_ID, "item/" + folder + "/" + modelName)));
        }
    }


}




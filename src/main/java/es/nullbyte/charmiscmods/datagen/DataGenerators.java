package es.nullbyte.charmiscmods.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

import static es.nullbyte.charmiscmods.CharMiscModsMain.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    //Class that registers the data generators
    //in this case we will only add the item model provider, which the one we have items to test with
    //To remember everything and data gen for 1.20.x, check https://www.youtube.com/watch?v=enzKJWq0vNI&list=PLKGarocXCE1H9Y21-pxjt5Pt8bW14twa-
    //When you add new data generators or new data to an existing provider you must run gradlew runData to generate the data
    @SubscribeEvent
    public static  void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        //Add the providers
        generator.addProvider(event.includeServer(), new ModRecipeProvider(output)); //Recipe provider
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(output)); //Loot table provider

        generator.addProvider(event.includeClient(), new ModBlocKStateProvider(output,existingFileHelper)); //Block state provider
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output,existingFileHelper)); //Loot table provider

        ModBlockTagGenerator blockTagGenerator = generator.addProvider(event.includeServer(),
                new ModBlockTagGenerator(output,lookupProvider,existingFileHelper)); //Block tag provider
        generator.addProvider(event.includeServer(), new ModelItemTagGenerator(output,lookupProvider,blockTagGenerator.contentsGetter(),existingFileHelper)); //Item tag provider
    }
}

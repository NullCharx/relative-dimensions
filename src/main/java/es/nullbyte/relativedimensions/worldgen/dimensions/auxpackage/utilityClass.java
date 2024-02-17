package es.nullbyte.relativedimensions.worldgen.dimensions.auxpackage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mojang.datafixers.util.Pair;
import es.nullbyte.relativedimensions.worldgen.biomes.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.io.File;
import java.util.List;


public class utilityClass {

    // This method is used to patch the aberrant overworld JSON file with the vanilla overworld biomes generated by the game
    // Instead of generating 7k entries dynamically, we can just merge the vanilla biomes with the custom ones
    public static void patchAberrantOverworld() {
        String projectPath = "/Users/carlo/IdeaProjects/CharsMiscMods";
        String customBiomesPath = projectPath + "/src/generated/resources/data/relativedimensions/dimension/aberrant_overworld.json";
        String vanillaBiomesPath = projectPath + "/src/main/java/es/nullbyte/relativedimensions/worldgen/dimensions/auxpackage/dimension.json";
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Load the JSON trees
            JsonNode customBiomesNode = mapper.readTree(new File(customBiomesPath));
            JsonNode vanillaBiomesNode = mapper.readTree(new File(vanillaBiomesPath));

            // Navigate to the "biomes" arrays using the provided structure
            JsonNode customBiomesArrayNode = customBiomesNode.path("generator").path("biome_source").path("biomes");
            JsonNode vanillaBiomesArrayNode = vanillaBiomesNode.path("generator").path("biome_source").path("biomes");

            if (!customBiomesArrayNode.isArray() || !vanillaBiomesArrayNode.isArray()) {
                System.out.println("One of the files does not contain a valid 'biomes' array.");
                return;
            }

            ArrayNode customBiomesArray = (ArrayNode) customBiomesArrayNode;
            ArrayNode vanillaBiomesArray = (ArrayNode) vanillaBiomesArrayNode;

            // Merge the biome arrays
            customBiomesArray.addAll(vanillaBiomesArray);

            // Reconstruct the JSON structure with the merged array
            ObjectNode biomeSourceNode = mapper.createObjectNode();
            biomeSourceNode.put("type", "minecraft:multi_noise");
            biomeSourceNode.set("biomes", customBiomesArray);

            ObjectNode generatorNode = mapper.createObjectNode();
            generatorNode.put("type", "minecraft:noise");
            generatorNode.set("biome_source", biomeSourceNode);
            generatorNode.put("settings", "minecraft:overworld");

            ObjectNode finalJson = mapper.createObjectNode();
            finalJson.put("type", "relativedimensions:aberrant_overworld_type");
            finalJson.set("generator", generatorNode);

            // Write the updated JSON to file
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(customBiomesPath), finalJson);

            System.out.println("Merged JSON saved to " + customBiomesPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

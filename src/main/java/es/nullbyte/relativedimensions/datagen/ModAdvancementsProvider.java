package es.nullbyte.relativedimensions.datagen;

import es.nullbyte.relativedimensions.blocks.BlockInit;
import es.nullbyte.relativedimensions.items.ItemInit;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo.*;
import net.minecraft.ResourceLocationException;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.UsingItemTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.MOD_ID;

public class ModAdvancementsProvider implements ForgeAdvancementProvider.AdvancementGenerator {



    @Override
    public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
        AdvancementHolder aberrant_root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(BlockInit.ABERRANT_ORE.get()), //icon
                        Component.translatable("advancements." + MOD_ID + ".aberrant_root.title"), //Title
                        Component.translatable("advancements." + MOD_ID + ".aberrant_root.toast"), //Description
                        Optional.of(new ResourceLocation(MOD_ID, "textures/block/aberrant_ore.png")), //Texture full path for the advancement window background
                        AdvancementType.TASK, //Advancement type
                        true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
                .addCriterion("has_aberrant_ore_or_log", InventoryChangeTrigger.TriggerInstance.hasItems(BlockInit.ABERRANT_ORE.get().asItem(), BlockInit.ABERRANT_LOG.get()))
                .save(saver, new ResourceLocation(MOD_ID, "aberrant_root_advancement"));


        AdvancementHolder aberrant_tool_made = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ItemInit.ABERRANT_PICK.get()),
                        Component.translatable("advancements." + MOD_ID + ".aberrant_tool.title"),
                        Component.translatable("advancements." + MOD_ID + ".aberrant_tool.toast"), //Description
                        Optional.empty(), //Texture full path (optional)
                        AdvancementType.TASK, //Advancement type
                        true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
                .parent(aberrant_root)
                .addCriterion("has_aberrant_tool", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.ABERRANT_AXE.get(),ItemInit.ABERRANT_SWORD.get(),ItemInit.ABERRANT_PICK.get()))
                .save(saver, new ResourceLocation(MOD_ID, "aberrant_tool_advancement"));

        AdvancementHolder teleportation_root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(ItemInit.AVID_SDPT.get()),
                        Component.translatable("advancements." + MOD_ID + ".teleportation_root.title"),
                        Component.translatable("advancements." + MOD_ID + ".teleportation_root.toast"), //Description
                        Optional.of(new ResourceLocation(MOD_ID, "textures/item/avidsdpt.png")), //Texture full path
                        AdvancementType.TASK , //Advancement type
                        true, true, false)) //Show the advancement notification, Show chat notif, hidden advancement
                //The criterion is the player using the item
                .addCriterion("has_avidsdpt", InventoryChangeTrigger.TriggerInstance.hasItems(ItemInit.AVID_SDPT.get()))
                .save(saver, new ResourceLocation(MOD_ID, "teleportation_root_advancement"));
    }
}

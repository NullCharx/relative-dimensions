package es.nullbyte.charmiscmods.blocks;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import java.awt.*;
import java.util.List;

public class aberrantOre extends Block {

    public aberrantOre(Properties properties) {
        super(properties);
    }



    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter blocKGetter, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("block.chrmscmds.aberrant_ore.tooltip").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE).withStyle(net.minecraft.ChatFormatting.ITALIC));
        super.appendHoverText(pStack, blocKGetter, pTooltipComponents, pIsAdvanced);
    }
}
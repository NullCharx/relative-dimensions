package es.nullbyte.relativedimensions.blocks;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;

import java.util.List;

public class aberrantOre extends Block {

    public aberrantOre(Properties properties) {
        super(properties.emissiveRendering((state, world, pos) -> true)
                .lightLevel((light) -> 5));
    }



    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter blocKGetter, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("block.relativedimensions.aberrant_ore.tooltip").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE).withStyle(net.minecraft.ChatFormatting.ITALIC));
        super.appendHoverText(pStack, blocKGetter, pTooltipComponents, pIsAdvanced);
    }
}
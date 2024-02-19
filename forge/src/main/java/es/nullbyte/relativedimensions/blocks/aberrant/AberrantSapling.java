package es.nullbyte.relativedimensions.blocks.aberrant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;

import javax.annotation.Nullable;
import java.util.List;

public class AberrantSapling extends SaplingBlock {
    public AberrantSapling(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties.emissiveRendering((state, world, pos) -> true)
                .lightLevel((light) -> 1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter blocKGetter, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("block.relativedimensions.aberrant_sapling").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE).withStyle(net.minecraft.ChatFormatting.ITALIC));
        super.appendHoverText(pStack, blocKGetter, pTooltipComponents, pIsAdvanced);
    }
}

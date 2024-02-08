package es.nullbyte.relativedimensions.items;

import es.nullbyte.relativedimensions.effects.init.ModEffects;
import es.nullbyte.relativedimensions.items.init.ItemInit;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static es.nullbyte.relativedimensions.RelativeDimensionsMain.RANDOM;


public class AberrantSword extends SwordItem {

    //probabilities over 100
    public final static double TP_CHANCE = 10.0;
    public final static double WIELDER_SUFFERING_CHANCE = 50.0;
    public final static double TP_DISTANCE = 14.0;
    public final static double WIELDER_SUFFERING_DURATION_SECS = 5.0;

    public static List<MobEffect> harmfulEffectslist = new ArrayList<>();
    public static List<MobEffect> harmfulDamageEffectslist = new ArrayList<>();
    public static List<MobEffect> harmfulLongEffectslist = new ArrayList<>();
    public static List<MobEffect> harmfulShortishEffectslist = new ArrayList<>();
    public static List<MobEffect> harmfulShortEffectslist = new ArrayList<>();



    public AberrantSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);

        //Fill the harmfulEffectslist
        harmfulShortEffectslist.add(MobEffects.LEVITATION);
        harmfulShortEffectslist.add(MobEffects.WEAKNESS);
        harmfulShortEffectslist.add(MobEffects.HUNGER);
        harmfulShortEffectslist.add(MobEffects.DIG_SLOWDOWN);

        harmfulShortishEffectslist.add(MobEffects.DARKNESS);
        harmfulShortishEffectslist.add(MobEffects.CONFUSION);
        harmfulShortishEffectslist.add(MobEffects.MOVEMENT_SLOWDOWN);

        harmfulLongEffectslist.add(MobEffects.BAD_OMEN);
        harmfulLongEffectslist.add(MobEffects.UNLUCK);
        harmfulLongEffectslist.add(MobEffects.BLINDNESS);

        harmfulDamageEffectslist.add(MobEffects.WITHER);
        harmfulDamageEffectslist.add(MobEffects.POISON);
        harmfulDamageEffectslist.add(MobEffects.HARM);

        harmfulEffectslist.addAll(harmfulShortEffectslist);
        harmfulEffectslist.addAll(harmfulShortishEffectslist);
        harmfulEffectslist.addAll(harmfulLongEffectslist);
        harmfulEffectslist.addAll(harmfulDamageEffectslist);

    }

    //https://moddingtutorials.org/advanced-items


    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        Boolean damage = super.hurtEnemy(stack, target, attacker);

        //Test frist for the TP_CHANCE
        if (RANDOM.nextDouble(100.0) < TP_CHANCE) {
            //Play endder teleport sound
            target.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            //Teleport the target
            target.teleportTo(attacker.getX() + (Math.random() * TP_DISTANCE * 2 - TP_DISTANCE), attacker.getY() + 1, attacker.getZ() + (Math.random() * TP_DISTANCE * 2 - TP_DISTANCE));
            target.level().playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        }
        //Now test for the WIELDER_SUFFERING_CHANCE
        if (RANDOM.nextDouble(100.0) < WIELDER_SUFFERING_CHANCE) {
            System.out.println("Wielder suffering");
            Player player = (Player) attacker;
            //Get all the effects labeled as harmful
            MobEffect harmfulEffect = harmfulEffectslist.get(RANDOM.nextInt(harmfulEffectslist.size()));
            //Firstly apply the harmful effect to the wielder
            //Then apply the void bleed effect
            //If the effect is harmful, just apply void bleed
            if (harmfulShortEffectslist.contains(harmfulEffect)) {
                player.addEffect(new MobEffectInstance(harmfulEffect, (int) WIELDER_SUFFERING_DURATION_SECS *20 , 0, true, true, true));
                player.addEffect(new MobEffectInstance(ModEffects.VOID_BLEED.get(), (int) (WIELDER_SUFFERING_DURATION_SECS*20), 0, true, true, true));
            } else if (harmfulShortishEffectslist.contains(harmfulEffect)) {
                //If the harmful effect is darkness, then apply a padding effect to the wielder
                player.addEffect(new MobEffectInstance(harmfulEffect, (int) WIELDER_SUFFERING_DURATION_SECS * 6 *20 , 0, true, true, true));
                player.addEffect(new MobEffectInstance(ModEffects.VOID_BLEED.get(), (int) (WIELDER_SUFFERING_DURATION_SECS * 20), 0, true, true, true));
            } else if (harmfulLongEffectslist.contains(harmfulEffect)) {
                //Check if the player already has one effect of this list
                //If the harmful effect is darkness, then apply a padding effect to the wielder
                player.addEffect(new MobEffectInstance(harmfulEffect, (int) WIELDER_SUFFERING_DURATION_SECS * 120 * 20, 0, true, true, true));
                player.addEffect(new MobEffectInstance(ModEffects.VOID_BLEED.get(), (int) (WIELDER_SUFFERING_DURATION_SECS * 20), 0, true, true, true));
            } else if (harmfulDamageEffectslist.contains(harmfulEffect)) {
                //If the harmful effect is darkness, then apply a padding effect to the wielder
                player.addEffect(new MobEffectInstance(ModEffects.VOID_BLEED.get(), (int) (WIELDER_SUFFERING_DURATION_SECS * 20), 0, true, true, true));
            }



        }
        return damage;
    }

    // makes it repairable
    @Override
    public boolean isRepairable(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack tool, ItemStack material) {
        return material.getItem() == ItemInit.AVID_SDPT.get();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level plevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_sword.tooltip"));
        pTooltipComponents.add(Component.translatable("item.relativedimensions.aberrant_sword.tooltip_modifiers").withStyle(ChatFormatting.BOLD, ChatFormatting.LIGHT_PURPLE));

        super.appendHoverText(pStack, plevel, pTooltipComponents, pIsAdvanced);
    }
}

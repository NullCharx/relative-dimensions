package es.nullbyte.charmiscmods.SpawnRandomLootChest;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DespawnChestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("despawnchest").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(3);
        }).then(Commands.argument("x", DoubleArgumentType.doubleArg())
                .then(Commands.argument("z", DoubleArgumentType.doubleArg())
                        .executes(context -> {// Command logic here
                                    return DespawnChest(context.getSource(), DoubleArgumentType.getDouble(context, "x"),
                                            DoubleArgumentType.getDouble(context, "z"));
                                }
                        ))));
    }

    private static int DespawnChest(CommandSourceStack source, double x, double z) {
        ServerLevel world = source.getLevel();

        // Get the topmost block at the given coordinates
        BlockPos pos = new BlockPos((int)x, 350, (int)z);
        while (pos.getY() > -64 && world.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        // Check if the block is a chest and if it is, remove it without dropping its items.
        BlockState blockState = world.getBlockState(pos);
        if (blockState.getBlock() instanceof ChestBlock) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof ChestBlockEntity) {
                if (blockEntity instanceof ChestBlockEntity) {
                    ChestBlockEntity chest = (ChestBlockEntity) blockEntity;
                    // Get the items in the chest and remove them
                    List<ItemStack> items = new ArrayList<>();
                    for (int i = 0; i < chest.getContainerSize(); i++) {
                        ItemStack item = chest.getItem(i);
                        if (!item.isEmpty()) {
                            items.add(item);
                            chest.setItem(i, ItemStack.EMPTY);
                        }
                    }
                    // Remove the chest block

                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    source.sendSuccess((Supplier<Component>) Component.literal("Chest removed at" + pos.getX() + "," + pos.getY() + "," + pos.getZ()), true);
                    // Send the list of items removed

                    return 1;
                }
            }

        }
        source.sendFailure(Component.literal("No chest found"));
        return 0;
    }
}

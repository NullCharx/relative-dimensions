package es.nullbyte.charmiscmods.commands;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpawnChestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spawnchest").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(3);
        }).then(Commands.argument("x", DoubleArgumentType.doubleArg())
                .then(Commands.argument("z", DoubleArgumentType.doubleArg())
                        .then(Commands.argument("minplayerblockdistance", IntegerArgumentType.integer())
                                .then(Commands.argument("items", StringArgumentType.greedyString())
                                        .suggests(SpawnChestCommand::getItemSuggestions) //This suggest applies to items argument only
                                        .executes(context -> {// Command logic here
                                                    return SpawnChest(context.getSource(), DoubleArgumentType.getDouble(context, "x"),
                                                            DoubleArgumentType.getDouble(context, "z"), IntegerArgumentType.getInteger(context, "minplayerblockdistance"),
                                                            StringArgumentType.getString(context, "items"));
                                                }
                                        ))))));
    }

    private static int SpawnChest(CommandSourceStack source, double x, double z, int minPlayerBlockDistance, String itemsString) {
        ServerLevel world = source.getLevel();

        // Get the topmost block at the given coordinates
        BlockPos pos = new BlockPos((int) x, 350, (int)z);
        while (pos.getY() > -64 && world.isEmptyBlock(pos)) {
            pos = pos.below();
        }
        pos = pos.above();

        // Check if there are any players within the minimum block distance
        boolean playerInRange = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(minPlayerBlockDistance)).size() > 0;
        if (playerInRange) {
            // Calculate a new position that is at least the minimum block distance away from the nearest player
            double playerDistance = Double.MAX_VALUE;
            BlockPos playerPos = null;
            BlockPos finalPos = pos;
            for (ServerPlayer player : world.getPlayers((player) -> player.distanceToSqr(x, finalPos.getY(), z) < minPlayerBlockDistance * minPlayerBlockDistance)) {
                double distance = player.distanceToSqr(x, pos.getY(), z);
                if (distance < playerDistance) {
                    playerDistance = distance;
                    playerPos = player.blockPosition();
                }
            }

            if (playerPos != null) {
                Vec3 offset = new Vec3(x - playerPos.getX(), 0, z - playerPos.getZ()).normalize().scale(minPlayerBlockDistance);
                Vec3i offseti = new Vec3i((int)offset.x, (int)offset.y, (int)offset.z);
                pos = playerPos.offset(offseti);
            }

            // Send a message to the command sender indicating that the chest could not be placed due to players being nearby
            source.sendFailure(Component.literal("Esta posicion está a menos de " + minPlayerBlockDistance + " bloques de un jugador"));
            source.sendSuccess((Supplier<Component>) Component.literal("Una localizacion cercana valida es: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), true);
            return 0;
        }

        // Create the chest block
        BlockState chestState = Blocks.CHEST.defaultBlockState();
        world.setBlock(pos, chestState, 3);

        // Parse the list of items to place in the chest
        List<ItemStack> items = new ArrayList<>();
        String[] itemStrings = itemsString.split("\\s+");
        for (String itemString : itemStrings) {
            int count = 1;
            String itemName = itemString.trim();
            if (itemName.contains("*")) {
                String[] parts = itemName.split("\\*", 2);
                itemName = parts[0].trim();
                count = Integer.parseInt(parts[1].trim());
            }
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            if (item != null && !item.equals(ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:air")))) {
                items.add(new ItemStack(item, count));
            }
        }

        // Randomly distribute the items in the chest
        Collections.shuffle(items);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        Random random = new Random();
        List<Integer> availableSlots = IntStream.range(0, 27).boxed().collect(Collectors.toList());
        int randint;
        if (blockEntity instanceof ChestBlockEntity) {
            Container chest = ((ChestBlockEntity) blockEntity);
            // Add each item to the chest
            for (ItemStack item : items) {
                if (availableSlots.isEmpty()) {
                    break;
                }
                randint = availableSlots.remove(random.nextInt(availableSlots.size()));
                chest.setItem(randint, item);
            }

            // Send a success message
            source.sendSuccess((Supplier<Component>) Component.literal("Cofre colocado en " + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "with items: " + items), true);
            return 1;
        } else {
            // Send a message to the command sender indicating that the block at the given position is not a chest
            source.sendFailure(Component.literal("No hay un cofre en la posición dada"));
            return 0;
        }
    }


    private static CompletableFuture<Suggestions> getItemSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        //This method only suggest the first item
        String input = builder.getRemaining().toLowerCase();

        // Get a list of all items in the game registry that start with the typed string
        List<String> itemNames = ForgeRegistries.ITEMS.getKeys().stream()
                .map(ResourceLocation::toString)
                .filter(name -> name.toLowerCase().startsWith(input))
                .sorted()
                .toList();

        itemNames.forEach(builder::suggest);

        // Add the item names to the suggestions builder
        return builder.buildFuture();
    }



}

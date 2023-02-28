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

public class SpawnChestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setchest").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(3);
        }).then(Commands.argument("x", DoubleArgumentType.doubleArg())
            .then(Commands.argument("z", DoubleArgumentType.doubleArg())
                .then(Commands.argument("minplayerblockdistance", IntegerArgumentType.integer())
                    .then(Commands.argument("items", StringArgumentType.greedyString())
                        .suggests(SpawnChestCommand::getItemSuggestions)
                            .executes(context -> {// Command logic here
                                return execute(context.getSource(), DoubleArgumentType.getDouble(context, "x"),
                                        DoubleArgumentType.getDouble(context, "z"), IntegerArgumentType.getInteger(context, "minplayerblockdistance"),
                                        StringArgumentType.getString(context, "items"));
                            }
        ))))));
    }

    private static int execute(CommandSourceStack source, double x, double z, int minPlayerBlockDistance, String itemsString) {
        ServerLevel world = source.getLevel();

        // Get the topmost block at the given coordinates
        BlockPos pos = new BlockPos(x, 350, z);
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
                Vec3i offseti = new Vec3i(offset.x, offset.y,offset.z);
                pos = playerPos.offset(offseti);
            }

            // Send a message to the command sender indicating that the chest could not be placed due to players being nearby
            source.sendFailure(Component.literal("Esta posicion está a menos de" + minPlayerBlockDistance + "de un jugador"));
            source.sendSuccess(Component.literal("Una localizacion ceracana valida es:" +  pos.getX() + "," + pos.getY() + "," + pos.getZ()), true);
            return 0;
        }

        // Create the chest block
        BlockState chestState = Blocks.CHEST.defaultBlockState();
        world.setBlock(pos, chestState, 3);

        // Parse the list of items to place in the chest
        // Parse the list of items to place in the chest
        List<ItemStack> items = new ArrayList<>();
        String[] itemStrings = itemsString.split(",");
        for (String itemString : itemStrings) {
            int count = 1;
            String itemName = itemString.trim();
            if (itemName.contains("*")) {
                String[] parts = itemName.split("\\*", 2);
                itemName = parts[0].trim();
                count = Integer.parseInt(parts[1].trim());
            }
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
            if (item != null) {
                items.add(new ItemStack(item, count));
            }
        }

        // Randomly distribute the items in the chest
        Collections.shuffle(items);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        Random random = new Random();
        List<Integer> availableSlots = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26);
        int randint;
        if (blockEntity instanceof ChestBlockEntity) {
            Container chest = ((ChestBlockEntity) blockEntity);
            // Add each item to the chest
            for (ItemStack item : items) {
                randint = availableSlots.get(random.nextInt(availableSlots.size()));
                chest.setItem(randint, item);
                availableSlots.remove(randint);
                if(availableSlots.size() == 0){
                    break;
                }
            }

            // Send a success message to the command sender
            source.sendSuccess(Component.literal("Cofre colocado"), true);
            return 1;
        } else {
            // Send a message to the command sender indicating that the block at the given position is not a chest
            source.sendFailure(Component.literal("No hay un cofre en la posición dada"));
            return 0;
        }
    }


    private static CompletableFuture<Suggestions> getItemSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        // Get the typed argument string
        String input = builder.getRemaining().toLowerCase();

        // Get a list of all items in the game registry that start with the typed string
        List<String> itemNames = ForgeRegistries.ITEMS.getKeys().stream()
                .map(ResourceLocation::toString)
                .filter(name -> name.toLowerCase().startsWith(input))
                .sorted()
                .toList();

        itemNames.forEach(itemName -> builder.suggest(itemName));

        // Add the item names to the suggestions builder
        return builder.buildFuture();
    }


    
}

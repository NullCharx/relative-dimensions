package es.nullbyte.charmiscmods.charspvp.enablewinner;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WinnerEnabler {
    private static final SimpleCommandExceptionType ERROR_LEVEL_NOT_VALID = new SimpleCommandExceptionType(Component.translatable("Nivel PVP no valido. EL rango es de -1 (no pvp) a 1 (ULTRA)"));
    private static final SimpleCommandExceptionType ERROR_HIGHEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede aumentar más"));
    private static final SimpleCommandExceptionType ERROR_LOWEST_LEVEL = new SimpleCommandExceptionType(Component.translatable("El nivel PVP no puede disminuir más"));

    private static final int permissionLevel = 3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("toggleWinnerParty").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(3);
        }).then(Commands.argument("player", StringArgumentType.string())
            .suggests(WinnerEnabler::getPlayerSuggestions) //This suggest applies to items argument only
                .executes(context -> {// Command logic here
                    return toggleFireworks(context.getSource(), StringArgumentType.getString(context, "player"));
                }
        )));
    }


    private static <ServerPlayerEntity> int toggleFireworks(CommandSourceStack source, String player) {

        ServerPlayer srvPlayer = source.getServer().getPlayerList().getPlayerByName(player);
        if (srvPlayer != null) {
            winnerEvent winevent = new winnerEvent();
            winevent.player = srvPlayer;
            winevent.isWinnerSet = !winevent.isWinnerSet;
            source.sendSuccess(Component.literal("WINNAH PARTY!"), true);

            return 1;
        } else {
            // Player not found
            source.sendFailure(Component.literal("Jugador no disponible"));
            return 0;
        }
    }

    private static CompletableFuture<Suggestions> getPlayerSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        //This method only suggest the first item
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

package es.nullbyte.charmiscmods.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import es.nullbyte.charmiscmods.events.winnerEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WinnerEnabler {

    private static final int permissionLevel = 3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("toggleWinnerParty").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(permissionLevel);
        }).then(Commands.argument("player", StringArgumentType.string())
                .suggests(WinnerEnabler::getPlayerSuggestions) //This suggest applies to items argument only
                .executes(context -> {// Command logic here
                            return toggleFireworks(context.getSource(), StringArgumentType.getString(context, "player"));
                        }
                )));
    }


    private static int toggleFireworks(CommandSourceStack source, String player) {

        ServerPlayer srvPlayer = source.getServer().getPlayerList().getPlayerByName(player);
        if (srvPlayer != null) {
            winnerEvent winevent = new winnerEvent();
            winnerEvent.player = srvPlayer;
            winnerEvent.isWinnerSet = !winnerEvent.isWinnerSet;
            MutableComponent message, message1, message2;
            //Send status message to all connected players
            message = Component.translatable("GANADOR DEL EVENTO:");
            message.withStyle(ChatFormatting.BLUE);

            message1 = Component.translatable("   ");

            message2 = Component.translatable(player);
            message2.withStyle(ChatFormatting.YELLOW);

            if(winnerEvent.isWinnerSet) {
                for (ServerPlayer p : source.getLevel().getServer().getPlayerList().getPlayers()) {
                    p.sendSystemMessage(message, false);
                    p.sendSystemMessage(message1, false);

                    p.sendSystemMessage(message2, false);

                }
            }
            return 1;
        } else {
            // Player not found
            source.sendFailure(Component.literal("Jugador no disponible"));
            return 0;
        }
    }

    private static CompletableFuture<Suggestions> getPlayerSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining().toLowerCase();

        // Get a list of all online players that start with the typed string
        List<Component> playerNames = context.getSource().getServer().getPlayerList().getPlayers().stream()
                .map(ServerPlayer::getName)
                .filter(name -> name.getString().toLowerCase().startsWith(input))
                .sorted().toList();

        playerNames.forEach(playerName -> builder.suggest(playerName.getString()));

        // Add the player names to the suggestions builder
        return builder.buildFuture();
    }
}
package es.nullbyte.relativedimensions.charspvp.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Supplier;

public class ListPlayersCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("listplayers").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(3);
        }).executes(ctx -> {
            return listPlayers(ctx.getSource());
        }));
    }
    private static int listPlayers(CommandSourceStack source) {
        List<ServerPlayer> players = source.getServer().getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            String playerName = player.getName().toString();
            double posX = player.position().x;
            double posY = player.position().y;
            double posZ = player.position().z;
            String location = String.format("(X: %.2f Y: %.2f Z: %.2f)", posX, posY, posZ);
            source.sendSuccess((Supplier<Component>) Component.literal(playerName + " " + location), false);;
        }
        return 1;
    }



}
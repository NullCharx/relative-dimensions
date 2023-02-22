package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class modTimercmd {

    private static final SimpleCommandExceptionType ERROR_USER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("Jugador no encontrado"));
    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chtime").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("add").then(Commands.argument("time", StringArgumentType.string())
                    .then(Commands.literal("player").executes((timeadd) -> {//chpvp increase
                        String secstoadd = StringArgumentType.getString(timeadd, "time");
                        String playername = StringArgumentType.getString(timeadd, "player");
                        return addTime(timeadd.getSource(),secstoadd,playername);
                    })
                ))).then(Commands.literal("remove").then(Commands.argument("time", StringArgumentType.string())
                    .then(Commands.literal("player").executes((timeremove) -> {//chpvp increase
                        String secstoadd = StringArgumentType.getString(timeremove, "time");
                        String playername = StringArgumentType.getString(timeremove, "player");
                        return substractTime(timeremove.getSource(),secstoadd,playername);
                })))).then(Commands.literal("set").then(Commands.argument("time", StringArgumentType.string())
                    .then(Commands.literal("player").executes((timeset) -> {//chpvp increase
                        String secstoadd = StringArgumentType.getString(timeset, "time");
                        String playername = StringArgumentType.getString(timeset, "player");
                        return setTime(timeset.getSource(),secstoadd,playername);
                })))).then(Commands.literal("show").then(Commands.argument("player", StringArgumentType.string())
                    .executes((timeshow) -> {//chpvp increase
                        String playername = StringArgumentType.getString(timeshow, "player");
                        return showTime(timeshow.getSource(),playername);
                }))));
    };
    private static int addTime(CommandSourceStack source, String seconds, String playername) throws CommandSyntaxException {
        int secondstoadd = Integer.parseInt(seconds);
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).addTimePlayed(secondstoadd);
        return 0;
    }

    private static int substractTime(CommandSourceStack source, String seconds,String playername) throws CommandSyntaxException {
        int secondstoadd = Integer.parseInt(seconds);
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).removeTimePlayed(secondstoadd);
        return 0;
    }

    private static int setTime(CommandSourceStack source, String seconds,String playername) throws CommandSyntaxException {
        int secondstoadd = Integer.parseInt(seconds);
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).setTimePlayed(secondstoadd);
        return 0;
    }

    private static int showTime(CommandSourceStack source, String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).getSecsPlayed();
        return 0;
    }

    //Toggle and show state of countdown

}

package es.nullbyte.charmiscmods.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.PlayerTimeLimit.PlayerTimeManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class modTimercmd {

    private static final SimpleCommandExceptionType ERROR_USER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("Jugador no encontrado"));
    private static final int permissionLevel = 3;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands belows

        dispatcher.register(Commands.literal("chtime").requires((permission) -> { //Check OP or server agent permission
                    return permission.hasPermission(permissionLevel);
                }).then(Commands.literal("add").then(Commands.argument("time", IntegerArgumentType.integer())
                    .then(Commands.argument("player", StringArgumentType.string()).executes((timeadd) -> {//chpvp increase
                        int secstoadd = IntegerArgumentType.getInteger(timeadd, "time");
                        String playername = StringArgumentType.getString(timeadd, "player");
                        return addTime(timeadd.getSource(),secstoadd,playername);
                    })
                ))).then(Commands.literal("remove").then(Commands.argument("time", IntegerArgumentType.integer())
                .then(Commands.argument("player", StringArgumentType.string()).executes((timeremove) -> {//chpvp increase
                    int secstoadd = IntegerArgumentType.getInteger(timeremove, "time");
                        String playername = StringArgumentType.getString(timeremove, "player");
                        return substractTime(timeremove.getSource(),secstoadd,playername);
                })))).then(Commands.literal("set").then(Commands.argument("time", IntegerArgumentType.integer())
                    .then(Commands.argument("player", StringArgumentType.string()).executes((timeset) -> {//chpvp increase
                    int secstoadd = IntegerArgumentType.getInteger(timeset, "time");
                        String playername = StringArgumentType.getString(timeset, "player");
                        return setTime(timeset.getSource(),secstoadd,playername);
                })))).then(Commands.literal("show").then(Commands.argument("player", StringArgumentType.string())
                    .executes((timeshow) -> {//chpvp increase
                        String playername = StringArgumentType.getString(timeshow, "player");
                        return showTime(timeshow.getSource(),playername);
                }))).then(Commands.literal("isenabled") .executes((showstate) -> {
                    return timerState(showstate.getSource());
                })).then(Commands.literal("toggleTimer") .executes((togglestate) -> {
                    return toggleTimer(togglestate.getSource());
                })));

    };
    private static int addTime(CommandSourceStack source, int seconds, String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).removeTimePlayed(seconds);
        return 0;
    }

    private static int substractTime(CommandSourceStack source, int seconds,String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).addTimePlayed(seconds);
        return 0;
    }

    private static int setTime(CommandSourceStack source, int seconds,String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        PlayerTimeManager.getTracker(uuid).setTimePlayed(seconds);
        return 0;
    }

    private static int showTime(CommandSourceStack source, String playername) throws CommandSyntaxException {
        UUID uuid = PlayerTimeManager.playerUUIDbyName(playername, source.getLevel());
        if (uuid == null) {
            throw ERROR_USER_NOT_FOUND.create();
        }
        source.sendSystemMessage(Component.literal(String.format("Tiempo jugado hoy de + " + playername + ": "
                + LocalTime.ofSecondOfDay(PlayerTimeManager.getTracker(uuid).getSecsPlayed()).format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
        return 0;
    }
    private static int timerState(CommandSourceStack source) throws CommandSyntaxException {
        if(PlayerTimeManager.isTimerEnabled()){
            source.sendSystemMessage(Component.literal(String.format("The timer is active")));
        } else {
            source.sendSystemMessage(Component.literal(String.format("The timer is inactive")));
        }
        return 0;
    }

    private static int toggleTimer(CommandSourceStack source) throws CommandSyntaxException {
        PlayerTimeManager.toggleTimer();
        if(PlayerTimeManager.isTimerEnabled()){
            source.sendSystemMessage(Component.literal(String.format("The timer is now active")));
        } else {
            source.sendSystemMessage(Component.literal(String.format("The timer is now inactive")));
        }
        return 0;
    }



    //Toggle and show state of countdown

}

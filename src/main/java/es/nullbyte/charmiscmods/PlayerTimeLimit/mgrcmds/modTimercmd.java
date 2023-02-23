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

public class modTimercmd extends PlayerTimeManager {

    private static final SimpleCommandExceptionType ERROR_USER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("Jugador no encontrado"));
    private static final SimpleCommandExceptionType RESET_TIME_OUT_OF_RANGE =  new SimpleCommandExceptionType(Component.translatable("Error de argumento (formato 24 horas entre 00:00 y 23:59)"));
    private static final SimpleCommandExceptionType DAILY_AMOUNT_TO_LOW =  new SimpleCommandExceptionType(Component.translatable("Error de argumento. Al menos una hora de juego"));
    private static final SimpleCommandExceptionType DAILY_AMOUNT_OUT_OF_RANGE =  new SimpleCommandExceptionType(Component.translatable("Error de argumento. Maximo de 99 horas"));

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
                })).then(Commands.literal("change").then(Commands.argument("resetTime", StringArgumentType.string())
                .then(Commands.argument("hour", IntegerArgumentType.integer()).then(Commands.argument("minute", IntegerArgumentType.integer())
                .executes((resetChange) -> {//chpvp increase
                    int hour = IntegerArgumentType.getInteger(resetChange, "hour");
                    int min = IntegerArgumentType.getInteger(resetChange, "minute");
                    return changeResetTime(resetChange.getSource(),hour,min);
                })))).then(Commands.argument("dailyAmount", StringArgumentType.string())
                .then(Commands.argument("seconds", IntegerArgumentType.integer()).executes((resetDailyPT) -> {//chpvp increase
                            int secs = IntegerArgumentType.getInteger(resetDailyPT, "seconds");
                            return changeDailyPTAmount(resetDailyPT.getSource(),secs);
                })))
        ));

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
            source.sendSystemMessage(Component.literal(String.format("TContador activo")));
        } else {
            source.sendSystemMessage(Component.literal(String.format("Contador inactivo")));
        }
        return 0;
    }

    private static int toggleTimer(CommandSourceStack source) throws CommandSyntaxException {
        PlayerTimeManager.toggleTimer();
        if(PlayerTimeManager.isTimerEnabled()){
            source.sendSystemMessage(Component.literal(String.format("El contador se ha activado")));
        } else {
            source.sendSystemMessage(Component.literal(String.format("El contador se ha desactivado")));
        }
        return 0;
    }

    private static int changeResetTime(CommandSourceStack source, int hour, int min) throws CommandSyntaxException {
        if (hour <0 || min <0 || hour >24 || min >59){
            throw RESET_TIME_OUT_OF_RANGE.create();
        } else {
            PlayerTimeManager.setResetTime(hour,min);
            source.sendSystemMessage(Component.literal(String.format("Tiempo de reseteo fijado a" + hour + ":" + min)));
        }
        return 0;
    }


    //Toggle and show state of countdown
    private static int changeDailyPTAmount(CommandSourceStack source, int seconds) throws CommandSyntaxException {
        if (seconds <3600) {
            throw DAILY_AMOUNT_TO_LOW.create();
        } if (seconds > 99*3600){
            throw DAILY_AMOUNT_OUT_OF_RANGE.create();

        }else {
            PlayerTimeManager.setDailyTimeLimit(seconds);

            source.sendSystemMessage(Component.literal(String.format("Tiempo de juego fijado a" +
                    LocalTime.ofSecondOfDay(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
        }
        return 0;
    }


    }

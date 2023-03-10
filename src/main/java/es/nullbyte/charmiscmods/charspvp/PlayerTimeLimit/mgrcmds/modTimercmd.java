package es.nullbyte.charmiscmods.charspvp.PlayerTimeLimit.mgrcmds;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.charspvp.PlayerTimeLimit.PlayerTimeManager;
import es.nullbyte.charmiscmods.charspvp.network.DailyTimeLimitHandler;
import es.nullbyte.charmiscmods.charspvp.network.packet.S2CDailyTimeLimit;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class modTimercmd extends PlayerTimeManager {
    private static final SimpleCommandExceptionType ERROR_USER_NOT_FOUND = new SimpleCommandExceptionType(Component.translatable("Jugador no encontrado"));
    private static final SimpleCommandExceptionType RESET_TIME_OUT_OF_RANGE =  new SimpleCommandExceptionType(Component.translatable("Error de argumento (formato 24 horas entre 00:00 y 23:59)"));
    private static final SimpleCommandExceptionType DAILY_AMOUNT_TO_LOW =  new SimpleCommandExceptionType(Component.translatable("Error de argumento. Al menos un minuto de juego"));
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
                })).then(Commands.literal("limit") .executes((showstate) -> {
                    return showDailyLimit(showstate.getSource());
                })).then(Commands.literal("resetTime") .executes((showstatereset) -> {
                    return showResetTime(showstatereset.getSource());
                }))).then(Commands.literal("isenabled") .executes((showstate) -> {
                    return timerState(showstate.getSource());
                })).then(Commands.literal("toggleTimer") .executes((togglestate) -> {
                    return toggleTimer(togglestate.getSource());
                })).then(Commands.literal("change").then(Commands.literal("resetTime")
                .then(Commands.argument("hour", IntegerArgumentType.integer()).then(Commands.argument("minute", IntegerArgumentType.integer())
                .executes((resetChange) -> {//chpvp increase
                    int hour = IntegerArgumentType.getInteger(resetChange, "hour");
                    int min = IntegerArgumentType.getInteger(resetChange, "minute");
                    return changeResetTime(resetChange.getSource(),hour,min);
                })))).then(Commands.literal("dailyAmount")
                .then(Commands.argument("seconds", IntegerArgumentType.integer()).executes((resetDailyPT) -> {//chpvp increase
                            int secs = IntegerArgumentType.getInteger(resetDailyPT, "seconds");
                            return changeDailyPTAmount(resetDailyPT.getSource(),secs);
                })))
        ));

    }

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
        source.sendSystemMessage(Component.literal(String.format("Tiempo jugado hoy de " + playername + ": "
                + LocalTime.ofSecondOfDay(PlayerTimeManager.getTracker(uuid).getSecsPlayed()).format(DateTimeFormatter.ofPattern("HH:mm:ss")))));
        return 0;
    }
    private static int timerState(CommandSourceStack source) {
        if(PlayerTimeManager.isTimerEnabled()){
            source.sendSystemMessage(Component.literal(String.format("TContador activo")));
        } else {
            source.sendSystemMessage(Component.literal(String.format("Contador inactivo")));
        }
        return 0;
    }

    private static int toggleTimer(CommandSourceStack source) {
        PlayerTimeManager.toggleTimer();
        BlockPos deathBP =  new BlockPos(0,100,0);
        MutableComponent message;
        source.getLevel().playSound(null, deathBP, SoundEvents.AMBIENT_CAVE.get(), SoundSource.PLAYERS, 100.0f, 1.0f);

        if(PlayerTimeManager.isTimerEnabled()){
            message= Component.translatable("[S.P.A.S] - El tiempo de juego ha comenzado. ¡A explorar! (Y cuidado por donde pisais)");
        } else {
            message= Component.translatable("[S.P.A.S] - Contador detenido");
        }
        message.withStyle(ChatFormatting.BOLD);
        for (ServerPlayer p : source.getLevel().getServer().getPlayerList().getPlayers()) {
            p.sendSystemMessage(message, false);
        }
        return 0;
    }

    private static int changeResetTime(CommandSourceStack source, int hour, int min) throws CommandSyntaxException {
        if (hour <0 || min <0 || hour >24 || min >59){
            throw RESET_TIME_OUT_OF_RANGE.create();
        } else {
            PlayerTimeManager.setResetTime(hour,min);
            source.sendSystemMessage(Component.literal(String.format("Tiempo de reseteo fijado a " + hour + ":" + min)));
        }
        return 0;
    }


    //Toggle and show state of countdown
    private static int changeDailyPTAmount(CommandSourceStack source, int seconds) throws CommandSyntaxException {
        if (seconds < 60) {
            throw DAILY_AMOUNT_TO_LOW.create();
        } if (seconds > 99*3600){
            throw DAILY_AMOUNT_OUT_OF_RANGE.create();

        }else {
            PlayerTimeManager.setDailyTimeLimit(seconds);

            source.sendSystemMessage(Component.literal(String.format("Tiempo de juego fijado a " +
                    LocalTime.ofSecondOfDay(seconds).format(DateTimeFormatter.ofPattern("HH:mm:ss")))));

            for (ServerPlayer p: source.getLevel().players()){ //update the timer for all online players GUIrender
                DailyTimeLimitHandler.sendToPlayer(new S2CDailyTimeLimit(seconds), p);

            }
        }
        return 0;
    }

    private static int showDailyLimit(CommandSourceStack source) {
        source.sendSuccess(Component.translatable("Tiempo diario maximo: " + LocalTime.ofSecondOfDay(PlayerTimeManager.getDailyTimeLimit()).format(DateTimeFormatter.ofPattern("HH:mm:ss")) ),false);
        return 0;
    }

    private static int showResetTime(CommandSourceStack source) {
        source.sendSystemMessage(Component.literal(String.format("LocalDate reset:" + PlayerTimeManager.getResetTime().toString())));
        return 0;
    }

}

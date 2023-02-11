package es.nullbyte.charmiscmods.commands;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import es.nullbyte.charmiscmods.commands.teams.Team;
import es.nullbyte.charmiscmods.commands.teams.TeamMgr;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * JAVA DOC:
 * Comando de equipos personalziado BASE.
 *
 * Remember to check onServerStart on main (the command is registered there)
 */
public class modTeam {
    private static int RETURN_OK = 0;
    private static int TEAM_NON_EXISTENT = 1;
    private static int TEAM_ALREADY_EXISTENT = 2;
    private static int MEMBER_ALREADY_ADDED = 3;
    private static int MEMBER_NON_EXISTENT_ONTEAM = 4;

    private static final SimpleCommandExceptionType ERROR_TEAM_ALREADY_EXISTS = new SimpleCommandExceptionType(Component.translatable("commands.team.add.duplicate"));
    private static final SimpleCommandExceptionType ERROR_TEAM_NON_EXISTENT = new SimpleCommandExceptionType(Component.translatable("Team does not exists"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_ADDED = new SimpleCommandExceptionType(Component.translatable("Player already added to the team"));
    private static final SimpleCommandExceptionType ERROR_UNAVAILABLE_PLAYER = new SimpleCommandExceptionType(Component.translatable("Unrecognised player."));

    private static final int permissionLevel = 2;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        //all subcommands below

        dispatcher.register(Commands.literal("chTeam").requires((permission) -> { //Check OP or server agent permission
            return permission.hasPermission(permissionLevel);
        }).then(Commands.literal("list").then(Commands.literal("teams").executes((teamsList) -> {//modteam list teams
            return teamList(teamsList.getSource());
        })).then(Commands.literal("players").then(Commands.argument("TeamName", StringArgumentType.string()).executes((teamPlayerList) -> {//modteam list players <TeamName>
            String teamName = StringArgumentType.getString(teamPlayerList, "TeamName");
            return playerList(teamPlayerList.getSource(),teamName);
        })))).then(Commands.literal("create").then(Commands.argument("TeamName", StringArgumentType.string()).executes((createteam) -> { //modTeam create <TeamName>
            String teamName = StringArgumentType.getString(createteam, "TeamName");
            return teamCreate(createteam.getSource(), teamName);
        }))).then(Commands.literal("remove").then(Commands.argument("TeamName", StringArgumentType.string()).executes((removeteam) -> { //modTeam remove <TeamName>
            String teamName = StringArgumentType.getString(removeteam, "TeamName");
            return teamRemove(removeteam.getSource(), teamName);
        }))).then(Commands.literal("join").then(Commands.argument("TeamName", StringArgumentType.string()).executes((join) -> {
            String teamName = StringArgumentType.getString(join, "TeamName");
            return joinTeam(join.getSource(), teamName, null);
        }).then(Commands.argument("PlayerName", StringArgumentType.string()).executes((joinP) -> {
            String teamName = StringArgumentType.getString(joinP, "TeamName");
            String playerName = StringArgumentType.getString(joinP, "PlayerName");
            return joinTeam(joinP.getSource(), teamName, playerName);
        })))).then(Commands.literal("leave").executes((leave) -> {//modTeam leave <TeamName>
            return leaveTeam(leave.getSource(),null);
        }).then(Commands.argument("PlayerName", StringArgumentType.string()).executes((leavep) -> {//modTeam leave <TeamName>
            String playerName = StringArgumentType.getString(leavep, "PlayerName");
            return leaveTeam(leavep.getSource(), playerName);
        })))
        );
    }

    private static int teamCreate(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        int returnCode = TeamMgr.addTeam(teamName);
        if (returnCode == TEAM_ALREADY_EXISTENT) {
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        }
        source.sendSystemMessage(Component.literal(String.format("Equipo: " + teamName + " creado")));
        return returnCode;
    }

    private static int teamRemove(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        int returnCode = TeamMgr.removeTeam(teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            source.sendSystemMessage(Component.literal(String.format("Equipo de nombre: " + teamName + " no existe")));
            throw ERROR_TEAM_NON_EXISTENT.create();
        }

        source.sendSystemMessage(Component.literal(String.format("Equipo de nombre: " + teamName + " borrado")));
        return returnCode;
    }

    private static int teamList(CommandSourceStack source) {
        String teamString = TeamMgr.getTeamString();
        source.sendSystemMessage(Component.literal(String.format("TEAMS:\n\n" + teamString)));
        return 0;
    }

    private static int playerList(CommandSourceStack source, String teamName) throws CommandSyntaxException {
        String teamPString = TeamMgr.getTeamMembersList(teamName);
        if (teamPString == null) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        }
        source.sendSystemMessage(Component.literal(String.format("MEMBERS OF:" + teamName + "\n" + teamPString)));

        return 0;
    }

    private static int joinTeam(CommandSourceStack source, String teamName, String playerName) throws CommandSyntaxException {
        Player joiner; //Player to join
        if (playerName == null) { //If no playerargument provided, use command caster
            joiner = source.getPlayer();
        } else { //If playerargument provided, check the online players:
            joiner = source.getServer().getPlayerList().getPlayerByName(playerName);
        }
        if (joiner == null) { //If no player was found after checks, end command
            source.sendSystemMessage(Component.literal(String.format("Jugador no encontrado")));
            throw ERROR_UNAVAILABLE_PLAYER.create();
        }

        int returnCode = TeamMgr.addPlayerToTeam(joiner, teamName);
        if (returnCode == TEAM_NON_EXISTENT) {
            source.sendSystemMessage(Component.literal(String.format("Equipo de nombre: " + teamName + " no existe")));
            throw ERROR_TEAM_ALREADY_EXISTS.create();
        } else if (returnCode == MEMBER_ALREADY_ADDED) {
            source.sendSystemMessage(Component.literal(String.format("Jugador ya forma parte de " + teamName)));
            throw ERROR_ALREADY_ADDED.create();
        }

        //Check first if the player alredy has a saved team
        joiner.getPersistentData().putString("playerchTeam",teamName);
        //Save to persistent json file?

        source.sendSystemMessage(Component.literal(String.format("Jugador: " + joiner.getName().getString() + " se unió a equipo: " + teamName)));
        return 0;
    }

    private static int leaveTeam(CommandSourceStack source, String playerName) throws CommandSyntaxException {
        System.out.println(playerName);
        Player joiner;
        if (playerName == null) { //If no playerargument provided, use command caster
            joiner = source.getPlayer();
        } else { //If playerargument provided, check the online players:
            joiner = source.getServer().getPlayerList().getPlayerByName(playerName);
        }
        if (joiner == null) { //If no player was found after checks, end command
            throw ERROR_UNAVAILABLE_PLAYER.create();
        }
        String currentTeam = joiner.getPersistentData().getString("playerchTeam");
        if (currentTeam == null || currentTeam.equals("")){
            source.sendSystemMessage(Component.literal(String.format("Jugador borrado de equipo")));
            return 0;
        }
        int returnCode = TeamMgr.removePlayerFromTeam(joiner, currentTeam);

        if (returnCode == TEAM_NON_EXISTENT) {
            throw ERROR_TEAM_NON_EXISTENT.create();
        } else if (returnCode == MEMBER_NON_EXISTENT_ONTEAM) {
            throw ERROR_ALREADY_ADDED.create();
        }
        joiner.getPersistentData().putString("playerchTeam","");
        source.sendSystemMessage(Component.literal(String.format("Jugador borrado de equipo")));

        return 0;

    }


}

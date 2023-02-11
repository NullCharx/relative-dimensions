package es.nullbyte.charmiscmods.commands.teams;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.entity.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TeamMgr {

    private static List<Team> teams;


    private static int RETURN_OK = 0;
    private static int TEAM_NON_EXISTENT = 1;

    private static int TEAM_ALREADY_EXISTENT = 2;
    private static int MEMBER_ALREADY_ADDED = 3;
    private static int MEMBER_NON_EXISTENT_ONTEAM = 4;
    static {
        teams = new ArrayList<>();
    }

    public static int addPlayerToTeam(Player player, String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already added
            if (t.getName().equals(teamName)) { //IF it is call team addmember
                return t.addMember(player);
            }
        }
        return TEAM_NON_EXISTENT; //Return on error 1 (Team non-existent) if it does not exist on team manager
    }
    public static int removePlayerFromTeam(Player player, String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already created
            if (t.getName().equals(teamName)) { //if it is, call tam removemember
                return t.removeMember(player);
            }
        }
        return TEAM_NON_EXISTENT; //Return on error 1 (Team non-existent) if it does not exist on team manager
    }
    public static int addTeam (String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already added
            if (t.getName().equals(teamName)) {
                return TEAM_ALREADY_EXISTENT; //If it is already created, return on error (TEAM ALREADY CREATED)
            }
        }
        Team newTeam = new Team(teamName);
        teams.add(newTeam); //If It's not,add it and return OK
        return RETURN_OK;
    }

    public static int removeTeam (String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already added
            if (t.getName().equals(teamName)) {
                for (Player m: t.getMembers()) {
                    m.getPersistentData().putString("playerchTeam", null);
                }
                teams.remove(t);
                return RETURN_OK; //If it is already created, return on error (TEAM ALREADY CREATED)
            }
        }
        return TEAM_NON_EXISTENT; //Return on error 1 (Team non existent) if it does not exist on team manager
    }

    public static List<Team> getTeams() { //Returns list of team Objects
        return teams;
    }

    public static String getTeamString() { //Returns string of team names, each one in a line
        String teamsList = "";
        if (teams.size() == 0) {
            teamsList = "There are no teams";
        } else {
            for (Team t : teams) {
                teamsList = teamsList + t.getName() + "\n";
            }
            teamsList = teamsList + "----------------------";
        }
        return teamsList;
    }

    public static String getTeamMembersList(String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already added
            if (t.getName().equals(teamName)) {
                return t.getMembersString();
            }
        }
        return null;
    }

    public static List<Player> getMembersList(String teamName) {
        for(Team t : teams) { //Iterate over the teamlist to check if the passed team is a team already added
            if (t.getName().equals(teamName)) {
                return t.getMembers();
            }
        }
        return null;
    }
}

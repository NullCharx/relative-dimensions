package es.nullbyte.charmiscmods.commands.teams;

import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {
    private String name;
    private List<Player> members;

    private static int RETURN_OK = 0;
    private static int TEAM_NON_EXISTENT = 1;

    private static int TEAM_ALREADY_EXISTENT = 2;
    private static int MEMBER_ALREADY_ADDED = 3;
    private static int MEMBER_NON_EXISTENT_ONTEAM = 4;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public List<Player> getMembers() {
        return members;
    }

    public int addMember(Player player) {
        for (Player p : members) { //Iterate over members and chek if the soon to add player is already added.
            if (p.getName().equals(player.getName())) { //If it is, return on error. (2, member already added)
                return MEMBER_ALREADY_ADDED;
            }
        }
        //If it is not, add and return OK
        members.add(player);
        return RETURN_OK;
    }

    public int removeMember(Player player) {
        for (Player p : members) { //Iterate over members and chek if the soon to add player is already added.
            if (p.getName().getString().equals(player.getName().getString())) { //If it is, remove and return OK
                members.remove(player);
                return RETURN_OK;
            }
        }
        ////If it is, return on error. (2, member already added)
        return MEMBER_NON_EXISTENT_ONTEAM;
    }

    public String getMembersString () {
        String membersList = "";
        if (members.size() == 0) {
            membersList = "No members";
        } else {
            for (Player p : members) {
                membersList = membersList + p.getName().getString() + "\n";
            }
        }
        return membersList;
    }
}

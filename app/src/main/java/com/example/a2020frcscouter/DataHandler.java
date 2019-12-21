package com.example.a2020frcscouter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DataHandler {

    private final static String[] scoreKeys = {""}; //TODO fill in these keys

    public static ArrayList<JSONObject> teamList = new ArrayList<>();
    public static ArrayList<JSONObject> matchList = new ArrayList<>();

    public static void update(JSONObject matchJson) {
        HashMap<String, Object>[] teams;

        teamList = readTeamFile();
        matchList = readMatchFile();

        matchList.add(matchJson);

        teams = parseTeams(matchJson);

        for(HashMap<String, Object> t: teams) {
            insertTeamData(t);
        }

        writeTeamFile();
        writeMatchFile();
    }

    private static HashMap<String, Object>[] parseTeams(JSONObject json) { //parse six teams from a match json
        //TODO this function
        JSONObject team = new JSONObject();

    }

    private static void insertTeamData(HashMap<String, Object> team) { // Inserts the new team data from a match into the accumulated data for the team.
        // TODO this function
        JSONObject temp;

        for(int i = 0; i < teamList.size(); i++) {
            try {
                if (teamList.get(i).getInt("teamNum") == team.get("teamNum")) {
                    temp = teamList.get(i);
                    for(int n = 1; n < scoreKeys.length; n++) {
                        temp.accumulate(scoreKeys[n], team.get(scoreKeys[n]));
                    }
                    teamList.set(i, temp);
                    break;
                }
            }
            catch(Exception e) {
                Log.v("minto", "I don't even know what this exception is for");
            }
        }
    }

    private static ArrayList<JSONObject> readTeamFile() {

    }

    private static ArrayList<JSONObject> readMatchFile() {

    }

    private static void writeTeamFile() {

    }

    private static void writeMatchFile() {

    }
}

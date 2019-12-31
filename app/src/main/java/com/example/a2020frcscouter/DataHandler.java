package com.example.a2020frcscouter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class DataHandler {

    public final static String filepath = ""; //TODO figure out filepath
    private final static String[] scoreKeys = {"teamNum", "cargoPoints", "hatchPanelPoints", "teleopPoints", "autoPoints", "habClimbPoints"}; //TODO replace these keys

    public static ArrayList<JSONObject> teamList = new ArrayList<>();
    public static ArrayList<JSONObject> matchList = new ArrayList<>();

    public static void update(JSONObject matchJson) {

        HashMap<String, Object>[] teams;

        try {
            teamList = readTeamFile();
          //  matchList = readMatchFile();
        }
        catch(JSONException e) {
            e.printStackTrace();
            Log.v("minto", "Some json problems");
        }

        //matchList.add(matchJson);

        //teams = parseTeams(matchJson);

        teams = JSONHandler.getMatchData(matchJson);

        for(HashMap<String, Object> t: teams) {
            insertTeamData(t);
        }

        writeTeamFile();
        //writeMatchFile();
    }

//    private static HashMap<String, Object>[] parseTeams(JSONObject json) { //parse six teams from a match json
//        //TODO complete "parse teams" function
//        JSONObject team = new JSONObject();
//
//    }

    private static void insertTeamData(HashMap<String, Object> team) { // Inserts the new team data from a match into the accumulated data for the team.
        //I think it's done. Not positive
        JSONObject temp;

        for(int i = 0; i < teamList.size(); i++) {
            try {
                if (teamList.get(i).getInt("teamNum") == Integer.parseInt(team.get("teamNum").toString())) {
                    temp = teamList.get(i);
                    for(int n = 1; n < scoreKeys.length; n++) {
                        temp.accumulate(scoreKeys[n], team.get(scoreKeys[n]));
                    }
                    teamList.set(i, temp);
                    return;
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
                Log.v("minto", "I don't even know what this exception is for");
            }
        }

        //If the code reaches this point, there is not an entry for the team, and one must be created
        JSONObject newTeam = new JSONObject();
        try {
            newTeam.put("teamNum",  Integer.parseInt(team.get("teamNum").toString()));
            teamList.add(newTeam);
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        //Create the empty team, and recursively call the function and allow it to add the appropriate values
        insertTeamData(team);
    }

    private static ArrayList<JSONObject> readTeamFile() throws JSONException {
        //I think this is done
        File file = new File(MainActivity.c.getFilesDir(), "teams.json");
        ArrayList<JSONObject> list = new ArrayList<>();
        BufferedReader br;
        JSONArray jarray = null;

        try {
            br = new BufferedReader(new FileReader(file));
            jarray = new JSONArray(br.readLine());
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
            Log.v("minto", "Team file not found. Creating file");

            try {
                Writer output = new BufferedWriter(new FileWriter(file));
                output.write("");
                output.close();
            }
            catch(IOException x) {
                e.printStackTrace();
                Log.e("minto", "Issue creating file");
            }

            return list;
        }
        catch(IOException e) {
            e.printStackTrace();
            Log.v("minto", "Problem reading from team file");
        }


        for(int i = 0; i < jarray.length(); i++) {
            list.add((JSONObject) jarray.get(i));
        }

        return list;
    }

//    private static ArrayList<JSONObject> readMatchFile() {
//
//    }

    private static void writeTeamFile() {
        File file = new File(MainActivity.c.getFilesDir(), "teams.json");
        Writer output;

        JSONArray list = new JSONArray(); // List of all teams in the file

        for(JSONObject t : teamList) {
            list.put(t);
        }

        try {
            output = new BufferedWriter(new FileWriter(file));
            Log.v("minto", "File path: " + file.getAbsolutePath());
            output.write(list.toString());
            output.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMatchFile() {
        //TODO complete writeMatchFile function
        //Same thing as writeTeamFile function????????
    }

    public static void printTeamsList() {
        Log.v("minto", "Printing team list. Length: " + teamList.size());
        for(JSONObject o : teamList) {
            Log.v("minto", o.toString());
        }
    }
}

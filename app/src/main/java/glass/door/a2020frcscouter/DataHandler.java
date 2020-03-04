package glass.door.a2020frcscouter;

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
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class DataHandler {

    public final static String[] scoreKeys2019 = {"teamNum", "cargoPoints", "hatchPanelPoints", "teleopPoints", "autoPoints", "habClimbPoints"}; //TODO replace these keys

    public final static String[] genericJsonKeys = {"autoCellsBottom", "autoCellsOuter", "autoCellsInner", "teleopCellsBottom", "teleopCellsOuter", "teleopCellsInner",
            "autoPoints", "autoCellPoints", "controlPanelPoints", "endgamePoints", "teleopPoints", "totalPoints"};

    public final static String[] uniqueJsonKeys = {};

    public static ArrayList<TeamJSONObject> teamList = new ArrayList<>();
    public static ArrayList<JSONObject> matchList = new ArrayList<>();
    public static HashMap settings = new HashMap();

    public static HashMap<String, Object>[] getMatchData(JSONObject matchJSON) { //returns an array of hashmaps, each of which represents a team

        try {

            HashMap<String, Object>[] infoTable = (HashMap<String, Object>[]) new HashMap[6];
            for (int x = 0; x < infoTable.length; x++) {
                infoTable[x] = new HashMap<String, Object>();
            }

            // Gets the team number for each team
            JSONObject allianceJSON = matchJSON.getJSONObject("alliances");
            JSONObject blueJSON = allianceJSON.getJSONObject("blue");
            JSONObject redJSON = allianceJSON.getJSONObject("red");
            for (int x = 0; x < 3; x++) {
                infoTable[x].put("teamNum", blueJSON.getJSONArray("team_keys").get(x).toString().substring(3));
                infoTable[x + 3].put("teamNum", redJSON.getJSONArray("team_keys").get(x).toString().substring(3));
            }

            JSONObject breakdownJSON = matchJSON.getJSONObject("score_breakdown");
            blueJSON = breakdownJSON.getJSONObject("blue");
            redJSON = breakdownJSON.getJSONObject("red");

            // Extracts all the values associated with all the uniqueJsonKeys
            for (int k = 0; k < uniqueJsonKeys.length; k++) {
                for (int x = 0; x < 3; x++) {
                    infoTable[x].put(uniqueJsonKeys[k], blueJSON.get(uniqueJsonKeys[k] + (x + 1)));
                    infoTable[x + 3].put(uniqueJsonKeys[k], redJSON.get(uniqueJsonKeys[k] + (x + 1)));
                }
            }

            // Extracts all the values associated with all the genericKeys
            for (int k = 0; k < genericJsonKeys.length; k++) {
                for (int x = 0; x < 3; x++) {
                    infoTable[x].put(genericJsonKeys[k], blueJSON.get(genericJsonKeys[k]));
                    infoTable[x + 3].put(genericJsonKeys[k], redJSON.get(genericJsonKeys[k]));
                }
            }

            // This section handles specific keys and information that is structurally different from the rest
            for (int x = 0; x < 3; x++) {
                switch (x) {
                    case 1:
                        infoTable[x].put("endgameRobot", blueJSON.get("endgameRobot" + (x + 1)));
                        infoTable[x + 3].put("endgameRobot", redJSON.get("endgameRobot" + (x + 1)));
                        break;
                    case 2:
                        infoTable[x].put("endgameRobot", blueJSON.get("endgameRobot" + (x + 1)));
                        infoTable[x + 3].put("endgameRobot", redJSON.get("endgameRobot" + (x + 1)));
                    case 3:
                        infoTable[x].put("endgameRobot", blueJSON.get("endgameRobot" + (x + 1)));
                        infoTable[x + 3].put("endgameRobot", redJSON.get("endgameRobot" + (x + 1)));
                }
            }

            for(Object e: infoTable){
                Log.v("minto", e.toString());
            }

            return infoTable;
        }
        catch(JSONException e) {
            e.printStackTrace();
            Log.d("minto", "Holy hecking heck you best be hoping you don't see this error (bottom of the json-parsing function (dumbass))");
        }

        return null;
    }

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

        teams = getMatchData(matchJson); //TODO Have fun changing this

        if(teams != null) {
            for (HashMap<String, Object> t : teams) {
                insertTeamData(t);
            }

            writeTeamFile();
            //writeMatchFile();
        }
    }

    private static void insertTeamData(HashMap<String, Object> team) { // Inserts the new team data from a match into the accumulated data for the team.
        //I think it's done. Not positive
        TeamJSONObject temp;
        JSONArray tempArr = new JSONArray();

        for(int i = 0; i < teamList.size(); i++) {
            try {
                if (teamList.get(i).getInt("teamNum") == Integer.parseInt(team.get("teamNum").toString())) {
                    temp = teamList.get(i);
                    for(int n = 1; n < genericJsonKeys.length; n++) {
                        temp.accumulate(genericJsonKeys[n], team.get(genericJsonKeys[n]));
                    }
                    if(team.get("endgameRobot") != null) {
                        //Log.d("minto", "Case called");
                        if (team.get("endgameRobot").equals("Hang")) {
                            temp.accumulate("endgameRobot", 1);
                        }
                        else {
                            temp.accumulate("endgameRobot", 0);
                        }
                    }
                    else {
                        temp.accumulate("endgameRobot", 0);
                    }

                    teamList.set(i, temp);
                    return;
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
                Log.v("minto", "I don't even know what this exception is for, but since that's not a very helpful error message, this is midway down the insertTeamData() function");
            }
        }

        //If the code reaches this point, there is not an entry for the team, and one must be created
        TeamJSONObject newTeam = new TeamJSONObject();
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

    private static ArrayList<TeamJSONObject> readTeamFile() throws JSONException {
        //I think this is done
        File file = new File(MyAppy.getAppContext().getFilesDir(), "teams.json");
        ArrayList<TeamJSONObject> list = new ArrayList<>();
        BufferedReader br;
        JSONArray jarray = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String arrString = br.readLine();
            if(arrString != null) {
                jarray = new JSONArray(arrString);
            }
            else {
                jarray = new JSONArray("[]");
            }
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
            list.add(new TeamJSONObject(jarray.get(i).toString()));
        }

        return list;
    }

//    private static ArrayList<JSONObject> readMatchFile() {
//
//    }

    private static void writeTeamFile() {
        File file = new File(MyAppy.getAppContext().getFilesDir(), "teams.json");
        Writer output;

        JSONArray list = new JSONArray(); // List of all teams in the file

        for(TeamJSONObject t : teamList) {
            list.put(t);
        }

        try {
            output = new BufferedWriter(new FileWriter(file));
            //Log.v("minto", "File path: " + file.getAbsolutePath());
            output.write(list.toString());
            output.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSettingsFile() {
        File file = new File(MyAppy.getAppContext().getFilesDir(), "settings.json");
        Writer output;

        JSONObject json = new JSONObject();

        try {
            output = new BufferedWriter(new FileWriter(file));
            json.put("currentEventName", TBAHandler.getCurrentEventName());

            output.write(json.toString());
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void readSettingsFile() {  //TODO TODO TODO TODO Pull the data from TBA right after the event is selected, before you ever change fragments
        File file = new File(MyAppy.getAppContext().getFilesDir(), "settings.json");
        BufferedReader br;
        String jsonString;
        JSONObject json = null;

        try {
            br = new BufferedReader(new FileReader(file));
            jsonString = br.readLine();
            if(jsonString != null) {
                json = new JSONObject(br.readLine());
                settings.put("currentEventName", json.get("currentEventName"));
                Log.d("minto", "read current event: " + settings.get("currentEventName").toString());

                TBAHandler.setCurrentEvent(settings.get("currentEventName").toString());
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(JSONException e) {
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

    public static void clearTeams() {
        File file = new File(MyAppy.getAppContext().getFilesDir(), "teams.json");
        Writer output;

        try {
            output = new BufferedWriter(new FileWriter(file));
            output.write("");
            output.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static TeamJSONObject getTeamJSON(int teamNum) {
        try {
            for (TeamJSONObject t : teamList) {
                if (t.getInt("teamNum") == teamNum) {
                    return t;
                }
            }
            return null;
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean teamIsAtSelectedEvent(int teamNum) {
        try {
            for (TeamJSONObject t : teamList) {
                if (t.getInt("teamNum") == teamNum) {
                    return true;
                }
            }
            return false;
        }
        catch(JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static double getScoreAverage(JSONArray arr) {
        double average = -1;

        if(arr != null) {
            average = 0;
            try {
                for (int i = 0; i < arr.length(); i++) {
                    average += (int) arr.get(i);
                }
                average /= arr.length();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        average *= 100;
        average = ((int) average) / 100.0;
        return average;
    }

    public static ArrayList<TeamJSONObject> sort() {
        try {
            return mergeSort(teamList, 0, teamList.size() - 1);
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ArrayList<TeamJSONObject> mergeSort(ArrayList<TeamJSONObject> list, int l, int r) throws  JSONException{
        if (l < r) {
            // Same as (l+r)/2, but avoids overflow for
            // large l and h
            int m = l+(r-l)/2;

            // Sort first and second halves
            list = mergeSort(list, l, m);
            list = mergeSort(list, m+1, r);

            list = merge(list, l, m, r);
        }
        return list;
    }

    private static ArrayList<TeamJSONObject> merge(ArrayList<TeamJSONObject> list, int l, int m, int r) throws JSONException {
        int i, j, k;
        int n1 = m - l + 1;
        int n2 =  r - m;
        double tempLVal, tempRVal;

        //TeamJSONObject arr[list.size()] = List.asArray(list);

        /* create temp arrays */
        ArrayList<TeamJSONObject> L = new ArrayList<>();
        ArrayList<TeamJSONObject> R = new ArrayList<>();


        /* Copy data to temp arrays L[] and R[] */
        for (i = 0; i < n1; i++)
            L.add(list.get(l + i));
        for (j = 0; j < n2; j++)
            R.add(list.get(m + 1+ j));

        /* Merge the temp arrays back into arr[l..r]*/
        i = 0; // Initial index of first subarray
        j = 0; // Initial index of second subarray
        k = l; // Initial index of merged subarray
        while (i < n1 && j < n2) {
            try {

                if(L.get(i).get(TeamListFragment.currentSortOption) instanceof JSONArray) {
                    tempLVal = DataHandler.getScoreAverage((JSONArray) L.get(i).get(TeamListFragment.currentSortOption));
                }
                else {
                    tempLVal = (Integer) L.get(i).get(TeamListFragment.currentSortOption);
                }

                if(R.get(j).get(TeamListFragment.currentSortOption) instanceof JSONArray) {
                    tempRVal = DataHandler.getScoreAverage((JSONArray) R.get(j).get(TeamListFragment.currentSortOption));
                }
                else {
                    tempRVal = (Integer) R.get(j).get(TeamListFragment.currentSortOption);
                }

                if (tempLVal >= tempRVal) {
                    list.remove(k);
                    list.add(k, L.get(i));
                    i++;
                } else {
                    list.remove(k);
                    list.add(k, R.get(j));
                    j++;
                }
                k++;
            }
            catch(ClassCastException e) {
                e.printStackTrace();
                Log.d("minto", i + " " + j);
                Log.d("minto", "Sort Option: " + TeamListFragment.currentSortOption + "     " + "L: " + L.get(i).get(TeamListFragment.currentSortOption) + "     R: " + R.get(j).get(TeamListFragment.currentSortOption) + "    Full Team: " + R.get(j).toString());
            }
        }

    /* Copy the remaining elements of L[], if there
       are any */
        while (i < n1) {
            list.remove(k);
            list.add(k, L.get(i));
            i++;
            k++;
        }

    /* Copy the remaining elements of R[], if there
       are any */
        while (j < n2) {
            list.remove(k);
            list.add(k, R.get(j));
            j++;
            k++;
        }

        return list;
    }

//    public ArrayList<String> getAllEventKeys() {
//
//    }
}

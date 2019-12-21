package com.example.a2020frcscouter;

import org.json.*;
import java.util.*;

public class JSONHandler {

    // Arrays containing all the keys of interest
    // This array contains all the keys where the values are the same for each team on the same alliance
    private final static String[] genericKeys = {"teleopScaleOwnershipSec", "autoScaleOwnershipSec", "vaultPoints", "autoSwitchAtZero", "teleopOwnershipPoints", "teleopPoints", "autoOwnershipPoints", "autoPoints", "teleopSwitchOwnershipSec"};
    private final static String[] tempKeys = {"cargoPoints", "hatchPanelPoints", "teleopPoints", "autoPoints", "habClimbPoints"};
    // This array contains all the keys where the values differ between teams on the same alliance
    private final static String[] uniqueKeys = {};

    // Returns a HashMap<String, Object> array that contains the match data for each team
    @SuppressWarnings("unchecked")
    public static HashMap<String, Object>[] getMatchData(JSONObject matchJSON) throws JSONException {
        HashMap<String, Object>[] infoTable = (HashMap<String, Object>[]) new HashMap[6];
        for(int x = 0; x < infoTable.length; x++) {
            infoTable[x] = new HashMap<String, Object>();
        }

        //JSONObject json = TBA.getJSON(TBA.getMatch(matchNum));

        // Gets the team number for each team
        JSONObject allianceJSON = matchJSON.getJSONObject("alliances");
        JSONObject blueJSON = allianceJSON.getJSONObject("blue");
        JSONObject redJSON = allianceJSON.getJSONObject("red");
        for(int x = 0; x < 3; x++) {
            infoTable[x].put("teamNumber", blueJSON.getJSONArray("team_keys").get(x).toString().substring(3));
            infoTable[x+3].put("teamNumber", redJSON.getJSONArray("team_keys").get(x).toString().substring(3));
        }

        JSONObject breakdownJSON = matchJSON.getJSONObject("score_breakdown");
        blueJSON = breakdownJSON.getJSONObject("blue");
        redJSON = breakdownJSON.getJSONObject("red");

        // Extracts all the values associated with all the uniqueKeys
        for(int k = 0; k < uniqueKeys.length; k++) {
            for(int x = 0; x < 3; x++) {
                infoTable[x].put(uniqueKeys[k], blueJSON.get(uniqueKeys[k] + (x+1)));
                infoTable[x+3].put(uniqueKeys[k], redJSON.get(uniqueKeys[k] + (x+1)));
            }
        }

        // Extracts all the values associated with all the genericKeys
        for(int k = 0; k < tempKeys.length; k++) {
            for(int x = 0; x < 3; x++) {
                infoTable[x].put(tempKeys[k], blueJSON.get(tempKeys[k]));
                infoTable[x+3].put(tempKeys[k], redJSON.get(tempKeys[k]));
            }
        }

       // This section handles specific keys and information that is structurally different from the rest
        for(int x = 0; x < 3; x++) {
            switch(x) {
                case 1:
                    infoTable[x].put("endgameRobot1", blueJSON.get("endgameRobot" + (x+1)));
                    infoTable[x+3].put("endgameRobot1", redJSON.get("endgameRobot" + (x+1)));
                    break;
                case 2:
                    infoTable[x].put("endgameRobot2", blueJSON.get("endgameRobot" + (x+1)));
                    infoTable[x+3].put("endgameRobot2", redJSON.get("endgameRobot" + (x+1)));
                case 3:
                    infoTable[x].put("endgameRobot3", blueJSON.get("endgameRobot" + (x+1)));
                    infoTable[x+3].put("endgameRobot3", redJSON.get("endgameRobot" + (x+1)));
            }
        }

      /* for(Object e: infoTable){
           Log.v("minto", e.toString());
       }*/
        return infoTable;
    }

}
package com.example.a2020frcscouter;

import android.database.sqlite.SQLiteCursor;
import android.util.Log;

import java.util.Iterator;
import java.util.Set;

public class FRC2019Team {
    public int teamNum;
    //public boolean climb;
    public float autoPoints;
    public boolean autoRun;
    public float teleopPoints;
    public float autoRunPerc;
    public float hatchPoints;
    public float cargoPoints;
    public float climb1;
    public float climb2;
    public float climb3;
    public int matchesPlayed = 1;
    public static float[] scores;
    public static String[] scoreKeys = {"_id", "teleopPoints", "autoPoints", "cargoPoints", "hatchPoints", "lowClimb", "midClimb", "highClimb" };


    public FRC2019Team(int t, float ap, float tp, float hp, float cp, float c1, float c2, float c3) {
        teamNum = t;
        autoPoints = ap;
        teleopPoints = tp;
        hatchPoints = hp;
        cargoPoints = cp;

        scores = new float[] {teamNum, teleopPoints, autoPoints, cargoPoints, hatchPoints, climb1, climb2, climb3};
    }

    public FRC2019Team(SQLiteCursor cursor) {

    }

    public static FRC2019Team buildTeam(Set entry) {

        //boolean tempClimb = false;
        float tempAP = 999;
        boolean tempAR = false;
        int tempTeamNum = 0;
        float tempTP = 999;
        float tempHP = 999;
        float tempCP = 999;
        float tempC1 = 0;
        float tempC2 = 0;
        float tempC3 = 0;

        String currItem;

        Iterator it = entry.iterator();

        while (it.hasNext()) {
            currItem = it.next().toString();
            if (currItem.contains("cargoPoints")) {
                tempCP = Float.parseFloat(currItem.substring(currItem.indexOf('=') + 1, currItem.length()));
                //Log.v("minto", Boolean.toString(tempClimb));
            } else if (currItem.contains("autoPoints")) {
                tempAP = Float.parseFloat(currItem.substring(currItem.indexOf('=') + 1, currItem.length()));
            } else if (currItem.contains("teamNumber")) {
                tempTeamNum = Integer.parseInt(currItem.substring(currItem.indexOf('=') + 1, currItem.length()));
                //Log.v("minto", Integer.toString(tempTeamNum));
            } else if (currItem.contains("teleopPoints")) {
                tempTP = Float.parseFloat(currItem.substring(currItem.indexOf('=') + 1, currItem.length()));
            } else if (currItem.contains("hatchPanelPoints")) {
                tempHP = Float.parseFloat(currItem.substring(currItem.indexOf('=') + 1, currItem.length()));
            }
            else if(currItem.contains("endgameRobot1")) {
                switch(currItem.substring(currItem.indexOf('='))) {
                    case "None":
                        tempC1 = 0;
                        tempC2 = 0;
                        tempC3 = 0;
                        break;
                    case "HabLevel1":
                        tempC1 = 1;
                        tempC2 = 0;
                        tempC3 = 0;
                        break;
                    case "HabLevel2":
                        tempC1 = 0;
                        tempC2 = 1;
                        tempC3 = 0;
                        break;
                    case "HabLevel3":
                        tempC1 = 0;
                        tempC2 = 0;
                        tempC3 = 1;
                        break;
                    default:
                        Log.v("minto", "Not good. climb points returned value that shouldn't be possible");
                }
            }
          /*  else if(currItem.contains("habClimbPoints")) {
                Log.v("minto", currItem);
                switch(Integer.parseInt(currItem.substring(currItem.indexOf('=') + 1, currItem.length()))) {
                    case 0:
                        tempC1 = 0;
                        tempC2 = 0;
                        tempC3 = 0;
                        break;
                    case 3:
                        tempC1 = 1;
                        tempC2 = 0;
                        tempC3 = 0;
                        break;
                    case 6:
                        tempC1 = 0;
                        tempC2 = 1;
                        tempC3 = 0;
                        break;
                    case 12:
                        tempC1 = 0;
                        tempC2 = 0;
                        tempC3 = 1;
                        break;
                    default:
                        Log.v("minto", "Not good. climb points returned value that shouldn't be possible");
                }
            } */

            //Log.d("minto", "Current entry item: " + currItem);
        }

        return new FRC2019Team(tempTeamNum, tempAP, tempTP, tempHP, tempCP, tempC1, tempC2, tempC3);
    }

    public void setMatchesPlayed(int i) {
        matchesPlayed = i;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }
}

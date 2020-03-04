package glass.door.a2020frcscouter;

import android.database.sqlite.SQLiteCursor;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

public class FRC2018Team implements Serializable {

    private int teamNum;
    private boolean climb;
    private int autoPoints;
    private boolean autoRun;
    private int teleopPoints;
    private float autoRunPerc;
    private int vaultPoints;
    private int matchesPlayed;

    public FRC2018Team() {
        climb = false;
        autoPoints = 999;
        autoRun = false;
        teamNum = 0;
        teleopPoints = 999;
        vaultPoints = 999;
        matchesPlayed = 0;
    }

    public FRC2018Team(boolean c, int ap, boolean ar, int tn, int tp, int vp) {
        climb = c;
        autoPoints = ap;
        autoRun = ar;
        teamNum = tn;
        teleopPoints = tp;
        vaultPoints = vp;
        matchesPlayed = 0;
    }

    public FRC2018Team (SQLiteCursor cursor) {
        teamNum = cursor.getInt(0);
        teleopPoints = cursor.getInt(1);
        autoPoints = cursor.getInt(2);
        autoRunPerc = cursor.getInt(3);
        vaultPoints = cursor.getInt(4);

    }


    public int getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }

    public boolean isClimb() {
        return climb;
    }

    public void setClimb(boolean climb) {
        this.climb = climb;
    }

    public int getAutoPoints() {
        return autoPoints;
    }

    public void setAutoPoints(int autoPoints) {
        this.autoPoints = autoPoints;
    }

    public boolean isAutoRun() {
        return autoRun;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public int getTeleopPoints() {
        return teleopPoints;
    }

    public void setTeleopPoints(int teleopPoints) {
        this.teleopPoints = teleopPoints;
    }

    public int getVaultPoints() {
        return vaultPoints;
    }

    public void setVaultPoints(int vaultPoints) {
        this.vaultPoints = vaultPoints;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public int getAutoRunBit() {
        if(autoRun) {
            return 1;
        }
        else {
            return 0;
        }
    }

    public int getClimbBit() {
        if(climb) {
            return 1;
        }
        else {
            return 0;
        }
    }

    // This function builds an FRC2018Team from a dataset retrieved from a JSON file
    public static FRC2018Team buildTeam(Set entry) {

        boolean tempClimb = false;
        int tempAP = 999;
        boolean tempAR = false;
        int tempTeamNum = 0;
        int tempTP = 999;
        int tempVP = 999;

        String currItem;

        Iterator it = entry.iterator();

        while(it.hasNext()) {
            currItem = it.next().toString();
            if(currItem.contains("endgameClimb")) {
                tempClimb = Boolean.parseBoolean(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
                //Log.v("minto", Boolean.toString(tempClimb));
            }
            else if(currItem.contains("autoPoints")) {
                tempAP = Integer.parseInt(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
            }
            else if(currItem.contains("autoRun")) {
                tempAR = Boolean.parseBoolean(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
            }
            else if(currItem.contains("teamNumber")) {
                tempTeamNum = Integer.parseInt(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
                //Log.v("minto", Integer.toString(tempTeamNum));
            }
            else if(currItem.contains("teleopPoints")) {
                tempTP = Integer.parseInt(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
            }
            else if(currItem.contains("vaultPoints")) {
                tempVP = Integer.parseInt(currItem.substring(currItem.indexOf('=')+1, currItem.length()));
            }

            //Log.d("minto", "Current entry item: " + currItem);
        }

        return new FRC2018Team(tempClimb, tempAP, tempAR, tempTeamNum, tempTP, tempVP);
    }
}

package com.example.a2020frcscouter;


import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//import static com.example.cameron.sql_testing.JSONHandler.getMatchData;

public class DBHelper extends SQLiteOpenHelper {

    private final static String SQL_CREATE_ENTRIES = "CREATE TABLE teams (_id INTEGER PRIMARY KEY, teleopPoints INT, autoPoints INT, autoRun FLOAT, vaultPoints INT, climb FLOAT, matchesPlayed INT);";
    private final static String SQL_CREATE_ENTRIES_NEW = "CREATE TABLE teams (_id INTEGER PRIMARY KEY, teleopPoints FLOAT, autoPoints FLOAT, cargoPoints FLOAT, hatchPoints FLOAT, lowClimb FLOAT, midClimb FLOAT, highClimb FLOAT, matchesPlayed INT);";

    private final static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS teams";
    public final static String SQL_TABLE_NAME = "teams";
    public static String nextMatch;
    private Context context;
    private JSONHandler updater;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ScoutDat.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase d = this.getWritableDatabase();
        d.close();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_NEW);
        db.execSQL("INSERT INTO teams VALUES (-1, 0, 0, 0, 0, 0, 0, 0, 0)"); // Entry for mean of each value
        db.execSQL("INSERT INTO teams VALUES (-2, 0, 0, 0, 0, 0, 0, 0, 0)"); // Entry for standard deviation of each value
//        SQLiteDatabase d = this.getWritableDatabase();
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    private void executeSQLScript(SQLiteDatabase database, String dbname) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;

        try{
            inputStream = assetManager.open(dbname);
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();

            String[] createScript = outputStream.toString().split(";");
            for (int i = 0; i < createScript.length; i++) {
                String sqlStatement = createScript[i].trim();
                if (sqlStatement.length() > 0) {
                    database.execSQL(sqlStatement + ";");
                }
            }
        } catch (IOException e){
        } catch (SQLException e) {
        }
    }

    public void doStatsStuff(String important) {
        double[] stats;
        //Log.e("minto", "djfdkjfkdjdldfkf");
        ArrayList<Float> valueList = new ArrayList<>();
        double[] doubleArray;
        Cursor cursor;
        SQLiteDatabase db = this.getWritableDatabase();
        if(important.contentEquals("yeet")) {
            for(int i = 1; i < FRC2019Team.scoreKeys.length; i++) {
                cursor = db.rawQuery("SELECT * FROM teams;", null);

                cursor.moveToFirst();
                while(cursor.moveToNext()) {
                    valueList.add(cursor.getFloat(i));
                }

                doubleArray = new double[valueList.size()];

                for (int x = 0; x < valueList.size(); x++) {
                    doubleArray[x] = valueList.get(x);
                }
                //Log.v("minto", "fdfdf" + Float.toString(valueList.get(0)));
                for(int f=0; f< doubleArray.length;f++) {
                    Log.e("minto", Double.toString(doubleArray[f]));
                }

                stats = StatAnalyser.getStats(doubleArray);
                db.execSQL("UPDATE teams SET " + FRC2019Team.scoreKeys[i] + "=" + stats[0] + " WHERE _id=-1;"); //Update mean of value
                db.execSQL("UPDATE teams SET " + FRC2019Team.scoreKeys[i] + "=" + stats[1] + " WHERE _id=-2;"); //Update standard deviation of value
            }
        }
    }

    public double getSig(float value, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor meanCursor = db.rawQuery("SELECT " + type + " FROM teams WHERE _id=-1;", null);
        Cursor sdCursor = db.rawQuery("SELECT * FROM teams WHERE _id=-2;", null);
        //Log.e("minto", "count: " + Float.toString(meanCursor.getFloat(0)));
        //double mean = meanCursor.getFloat(meanCursor.getColumnIndex(type));
        meanCursor.moveToNext();
        sdCursor.moveToNext();
        double mean = meanCursor.getFloat(meanCursor.getColumnIndex(type));
        double sd = sdCursor.getFloat(sdCursor.getColumnIndex(type));
        double v = value;
        Log.e("minto", "stuff" + Double.toString(mean) + " f ffdfd " + Double.toString(sd));
        return StatAnalyser.getSisgnificance(v, mean, sd);
    }

    public void updateTeamStats(FRC2019Team team) {
        SQLiteDatabase db = this.getWritableDatabase();

        //double oldARun, newARun, oldClimb, newClimb;

        float teleop = team.teleopPoints;
        float _id = team.teamNum;
        float autoPoints = team.autoPoints;
        float cargoPoints = team.cargoPoints;
        float hatchPoints = team.hatchPoints;
        float c1 = team.climb1;
        float c2 = team.climb2;
        float c3 = team.climb3;
        float matches = team.getMatchesPlayed();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " WHERE _id=" + _id + ";", null);

        float oldScore, newScore, currScore;

        if(cursor.moveToNext()) { // There is already an entry for the team, updates the entry

            Log.d("minto", "updating team " + _id);

            cursor.moveToLast();

            ////****UPDATE MATCHES PLAYED****////
            matches = cursor.getInt(8);
            matches++;
            db.execSQL("UPDATE teams SET matchesPlayed=" + matches + " WHERE _id='" + _id + "';");

            for(int i = 1; i < team.scores.length; i++) {
                oldScore = cursor.getFloat(i) * (matches-1);
                oldScore += team.scores[i];
                newScore = oldScore/matches;
                if(team.teamNum == 973) {
                    Log.v("minto", "HEREREERE " + matches + "   " + (cursor.getFloat(i)*(matches-1)) + " " + team.scores[i]);
                }
                db.execSQL("UPDATE teams SET " + team.scoreKeys[i] + "=" + newScore + " WHERE _id='" + _id + "';");
            }
//
//            ////****UPDATE TELEOP****////
//            oldTeleop = cursor.getFloat(1) * (matches-1);
//            oldTeleop += teleop;
//            newTeleop = oldTeleop/matches;
//            db.execSQL("UPDATE teams SET teleopPoints='" + newTeleop + "' WHERE _id='" + _id + "';");
//
//            ////****UPDATE AUTO SCORE****////
//            oldAutoP = cursor.getInt(2)*(matches-1);
//            oldAutoP += autoPoints;
//            newAutoP = oldAutoP/matches;
//            db.execSQL("UPDATE teams SET autoPoints=" + newAutoP + " WHERE _id='" + _id + "';");
//
//            ////****UPDATE CARGO POINTS****////
//            oldCargo = cursor.getInt(3)*(matches-1);
//            newCargo = cargoPoints + oldCargo;
//            db.execSQL("UPDATE teams SET cargoPoints=" + newCargo + " WHERE _id='" + _id + "';");
//
//            ////****UPDATE CLIMB****////
//          /*  matches = cursor.getFloat(5)*(matches-1);
//            newClimb = (oldClimb + climb) / matches;
//            db.execSQL("UPDATE teams SET climb=" + newClimb + " WHERE _id='" + _id + "';");*/
//
//            ////****UPDATE HATCH POINTS****////
//            oldHatch = cursor.getInt(4);
//            newHatch = hatchPoints + oldHatch;
//            db.execSQL("UPDATE teams SET hatchPoints=" + newHatch + " WHERE _id='" + _id + "';");
//
        }
        else { //There is not yet an entry for this team, create an entry
            db.execSQL("INSERT INTO " + SQL_TABLE_NAME + " VALUES (" + _id + ", " + teleop + ", " + autoPoints + ", " + cargoPoints + ", " + hatchPoints + ", " + c1 + ", " + c2 + ", " + c3 + ", " + matches + ");"); //MOST RECENTLELY CHANGED
            Log.v("minto", "INSERT ONE " + _id);
        }

        db.close();
    }

    public void updateTeamStats(FRC2018Team team) { //Old. For 2018 Teams
        SQLiteDatabase db = this.getWritableDatabase();


        int oldTeleop, newTeleop, oldAutoP, newAutoP, oldVault, newVault, matches;

        double oldARun, newARun, oldClimb, newClimb;

        int teleop = team.getTeleopPoints();
        int _id = team.getTeamNum();
        int autoPoints = team.getAutoPoints();
        int vaultPoints = team.getVaultPoints();
        int autoRun = team.getAutoRunBit();
        int climb = team.getClimbBit();
        matches = team.getMatchesPlayed();

        Cursor cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " WHERE _id=" + _id + ";", null);

        if(cursor.moveToNext()) { // There is already an entry for the team, updates the entry

            Log.d("minto", "updating team " + _id);

            cursor.moveToLast();

            ////****UPDATE MATCHES PLAYED****////
            matches = cursor.getInt(6);
            matches++;
            db.execSQL("UPDATE teams SET matchesPlayed=" + matches + " WHERE _id='" + _id + "';");

            ////****UPDATE TELEOP****////
            oldTeleop = cursor.getInt(1);
            newTeleop = oldTeleop + teleop;
            db.execSQL("UPDATE teams SET teleopPoints='" + newTeleop + "' WHERE _id='" + _id + "';");

            ////****UPDATE AUTO SCORE****////
            oldAutoP = cursor.getInt(2);
            newAutoP = autoPoints + oldAutoP;
            db.execSQL("UPDATE teams SET autoPoints=" + newAutoP + " WHERE _id='" + _id + "';");

            ////****UPDATE AUTO RUN****////
            oldARun = cursor.getFloat(3)*(matches-1);
            newARun = (oldARun + autoRun) / matches;
            db.execSQL("UPDATE teams SET autoRun=" + newARun + " WHERE _id='" + _id + "';");

            ////****UPDATE CLIMB****////
            oldClimb = cursor.getFloat(5)*(matches-1);
            newClimb = (oldClimb + climb) / matches;
            db.execSQL("UPDATE teams SET climb=" + newClimb + " WHERE _id='" + _id + "';");

            ////****UPDATE VAULT POINTS****////
            oldVault = cursor.getInt(4);
            newVault = vaultPoints + oldVault;
            db.execSQL("UPDATE teams SET vaultPoints=" + newVault + " WHERE _id='" + _id + "';");

        }
        else { //There is not yet an entry for this team, create an entry
            db.execSQL("INSERT INTO " + SQL_TABLE_NAME + " VALUES (" + _id + ", " + teleop + ", " + autoPoints + ", " + autoRun + ", " + vaultPoints + ", " + climb + ", " + matches + ");"); //MOST RECENTLELY CHANGED
            Log.v("minto", "INSERT ONE " + _id);
        }

        db.close();
        //Log.v("minto", getTeamData(93));
    }

    public String getTeamData(int _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String returnString = "";
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " WHERE _id=" + Integer.toString(_id) + ";", null);

        while(cursor.moveToNext()) {
            returnString += "Team ID: " + Integer.toString(cursor.getInt(0)) + "  Teleop: " + Integer.toString(cursor.getInt(1)) + "   Auto Points: " + cursor.getInt(2);
        }

        cursor.close();
        db.close();

        return returnString;
    }

    public String getAllEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        String returnString = "";
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + ";", null);

       while(cursor.moveToNext()) {
            Log.v("minto", "Team ID: " + Integer.toString(cursor.getInt(0)) + "  Teleop: " + Integer.toString(cursor.getInt(1)) + "   Auto Points: " + cursor.getInt(2));
            returnString += "Team ID: " + Integer.toString(cursor.getInt(0)) + "  Teleop: " + Integer.toString(cursor.getInt(1)) + "   Auto Points: " + cursor.getInt(2);
        }

        cursor.close();
        db.close();
        return returnString;
    }

    public Cursor getAllEntriesTeamCursor() {
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + ";", null);
        return cursor;
    }

    public Cursor getAllEntriesTeleopCursor() {
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " ORDER BY teleopPoints DESC" + ";", null);

        return cursor;
    }

    public Cursor getAllEntriesAutoCursor() {
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " ORDER BY autoPoints DESC" + ";", null);

        return cursor;
    }

    public Cursor getAllEntriesHatchCursor() {
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " ORDER BY hatchPoints DESC" + ";", null);

        return cursor;
    }

    public Cursor getAllEntriesCargoCursor() {
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + " ORDER BY cargoPoints DESC" + ";", null);

        return cursor;
    }

    public ArrayList<FRC2018Team> getAllEntriesList() {
        SQLiteDatabase db = this .getWritableDatabase();
        ArrayList<FRC2018Team> list = new ArrayList<>();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + SQL_TABLE_NAME + ";", null);

        while(cursor.moveToNext()) {
            Log.v("minto", "Team ID: " + Integer.toString(cursor.getInt(0)) + "  Teleop: " + Integer.toString(cursor.getInt(1)) + "   Auto Points: " + cursor.getInt(2));
            list.add(new FRC2018Team(false, cursor.getInt(2), false, cursor.getInt(0), cursor.getInt(1), 0));
        }

        cursor.close();
        db.close();
        return list;
    }
}
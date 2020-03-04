package glass.door.a2020frcscouter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.a2020frcscouter.R;

import org.json.JSONException;
import org.json.JSONObject;

public class TeamInfoActivity extends AppCompatActivity {
    TeamJSONObject scoresJson;
    JSONObject teamInfoJson;

    TextView teamNumText, teamAreaText, teamWebsiteText, teamScoresText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_info);
        try {
            teamInfoJson = new JSONObject(getIntent().getExtras().getString("teamJson"));

            String scoreString = getIntent().getExtras().getString("scoresJson");
            if(scoreString.equals("nope")) {
                scoresJson = null;
            }
            else {
                scoresJson = new TeamJSONObject(scoreString);
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }

        teamNumText = findViewById(R.id.TeamNumText);
        teamAreaText = findViewById(R.id.TeamAreaText);
        teamWebsiteText = findViewById(R.id.TeamWebsiteText);
        teamScoresText = findViewById(R.id.TeamScoresText);

        try {
            teamNumText.setText("Team " + teamInfoJson.getString("team_number"));
            teamAreaText.setText(teamInfoJson.getString("school_name") + ", " + teamInfoJson.getString("city") + ", " + teamInfoJson.get("state_prov") + ", " + teamInfoJson.getString("country"));

            String websiteText = teamInfoJson.getString("website");
            if(websiteText.equals("http://www.firstinspires.org/")) {
                teamWebsiteText.setVisibility(View.GONE);
            }
            else {
                teamWebsiteText.setText(teamInfoJson.getString("website"));
                //teamWebsiteText.setMovementMethod(LinkMovementMethod.getInstance());
            }

            if(scoresJson != null) {

                teamScoresText.setText("Average Scores From Selected Event");
                teamScoresText.append("\n\nTotal Points: " + DataHandler.getScoreAverage(scoresJson.getJSONArray("totalPoints")));
                teamScoresText.append("\nEndgame Points: " + DataHandler.getScoreAverage(scoresJson.getJSONArray("endgamePoints")));
                //teamScoresText.append("\nWin Rate: " + "YET TO BE IMPLEMENTED");
                teamScoresText.append("\n\nTeleop");
                teamScoresText.append("\n\tTotal Teleop Points: " + DataHandler.getScoreAverage(scoresJson.getJSONArray("teleopPoints")));
                teamScoresText.append("\n\tTeleop Cells Scored (Inner Goal): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("teleopCellsInner")));
                teamScoresText.append("\n\tTeleop Cells Scored (Outer): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("teleopCellsOuter")));
                teamScoresText.append("\n\tTeleop Cells Scored (Bottom): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("teleopCellsBottom")));
                teamScoresText.append("\n\tControl Panel Points: " + DataHandler.getScoreAverage(scoresJson.getJSONArray("controlPanelPoints")));
                teamScoresText.append("\n\nAutonomous");
                teamScoresText.append("\n\tTotal Auto Points: " + DataHandler.getScoreAverage(scoresJson.getJSONArray("autoPoints")));
                teamScoresText.append("\n\tAuto Cells Scored (Inner Goal): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("autoCellsInner")));
                teamScoresText.append("\n\tAuto Cells Scored (Outer): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("autoCellsOuter")));
                teamScoresText.append("\n\tAuto Cells Scored (Bottom): " + DataHandler.getScoreAverage(scoresJson.getJSONArray("autoCellsBottom")));

            }
            else {
                //Probably should do some stuff
            }
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

}

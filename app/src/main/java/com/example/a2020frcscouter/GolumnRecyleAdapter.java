package com.example.a2020frcscouter;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class GolumnRecyleAdapter extends RecyclerView.Adapter<GolumnRecyleAdapter.GolumnViewHolder> {
    String sortOption;
    ArrayList<TeamJSONObject> teamList;

    public GolumnRecyleAdapter(String s) {
        super();
        sortOption = s;
        teamList = DataHandler.sort(TeamListFragment.currentSortOption);
        //Log.d("minto", "List in constructor: " + teamList.toString());
    }

    public GolumnViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_entry_golumn, parent, false);

        GolumnViewHolder vh = new GolumnViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(GolumnViewHolder holder, int position) {
        final int newPosition = holder.getAdapterPosition();
        //Log.d("minto", "List in bind: " + teamList.toString());
        final TeamJSONObject team = teamList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //listener.OnDank(team);

                boolean expanded = team.getExpanded();
                team.setExpanded(!expanded);
                notifyItemChanged(newPosition);
            }
        });

        holder.bind(team);

        holder.itemView.findViewById(R.id.teamInfoButton).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.queue.add(new TBAJSONRequest(Request.Method.GET, TBAHandler.baseURL + "/team/frc" + team.getString("teamNum"), null, new TBATeamListener(team), new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("minto", "JSON Request encountered error");
                            error.printStackTrace();
                            Log.v("minto", error.getMessage() + "");
                        }
                    }));
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamList.size();
    }

    public void setSortOption(String option) {
        sortOption = option;
    }

    public void updateTeamList() {
        //teamList.clear();

        teamList = DataHandler.sort(TeamListFragment.currentSortOption);
        Log.d("minto", "Count: " + TeamListFragment.mAdapter.getItemCount());
        notifyDataSetChanged();
    }





    public static class GolumnViewHolder extends RecyclerView.ViewHolder {
        TextView teamNum, teamStat, teleopText, autoText;
        LinearLayout hiddenLayout;
        //Button infoButton;

        public GolumnViewHolder(View itemView) {
            super(itemView);
            teamNum = itemView.findViewById(R.id.TeamNumText);
            teamStat = itemView.findViewById(R.id.statText);
            teleopText = itemView.findViewById(R.id.teleopSubText);
            autoText = itemView.findViewById(R.id.autoSubText);
            hiddenLayout = itemView.findViewById(R.id.hiddenTeamEntry);
            //infoButton = itemView.findViewById(R.id.teamInfoButton);

            hiddenLayout.setVisibility(View.GONE);

        }

        public void bind(final TeamJSONObject team) {
            JSONArray statList;
            double average = 0;
            int num = -1;
            JSONArray teleopNums = null, autoNums = null;

            boolean expanded = team.getExpanded();
            hiddenLayout.setVisibility(expanded ? View.VISIBLE : View.GONE);

            try {
                num = team.getInt("teamNum");
                teleopNums = team.getJSONArray("teleopPoints");
                autoNums = team.getJSONArray("autoPoints");

                for(String s : DataHandler.genericJsonKeys) {
                    if(TeamListFragment.currentSortOption.equals(s)) {
                        Log.d("minto", "Sort option: " + TeamListFragment.currentSortOption + "     Found Key: " + s);
                        average = DataHandler.getScoreAverage(team.getJSONArray(s));
                    }
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
                Log.d("minto", "Issue parsing team json in bind() function");
            }

            //Log.d("minto", "average: " + Double.toString(average));
            teamNum.setText(Integer.toString(num));
            teamStat.setText(Double.toString(average));
            teleopText.setText("Teleop\t\t\n" + Double.toString(Math.round(DataHandler.getScoreAverage(teleopNums))));
            autoText.setText("Autonomous\t\t\n" + Double.toString(Math.round(DataHandler.getScoreAverage(autoNums))));
        }
    }
}

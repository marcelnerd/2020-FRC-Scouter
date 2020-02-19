package com.example.a2020frcscouter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GolumnRecyleAdapter extends RecyclerView.Adapter<GolumnRecyleAdapter.GolumnViewHolder> {
    String sortOption;
    OnDankListener dankListener;

    public GolumnRecyleAdapter(String s, OnDankListener l) {
        sortOption = s;
        dankListener = l;
    }

    public GolumnViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_entry_golumn, parent, false);

        GolumnViewHolder vh = new GolumnViewHolder(v, sortOption);
        return vh;
    }

    @Override
    public void onBindViewHolder(GolumnViewHolder holder, final int position) {
        final TeamJSONObject team = DataHandler.teamList.get(position);
        holder.bind(team, dankListener);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //listener.onDank(team);

                boolean expanded = team.getExpanded();
                team.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataHandler.teamList.size();
    }





    public static class GolumnViewHolder extends RecyclerView.ViewHolder {
        TextView teamNum, teamStat, teleopText, autoText;
        LinearLayout hiddenLayout;
        String sortOption;

        public GolumnViewHolder(View itemView, String s) {
            super(itemView);
            teamNum = itemView.findViewById(R.id.TeamNumText);
            teamStat = itemView.findViewById(R.id.statText);
            teleopText = itemView.findViewById(R.id.teleopSubText);
            autoText = itemView.findViewById(R.id.autoSubText);
            hiddenLayout = itemView.findViewById(R.id.hiddenTeamEntry);

            sortOption = s;

            hiddenLayout.setVisibility(View.GONE);

        }

        public void bind(final TeamJSONObject team, final OnDankListener listener) {
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
                    if(sortOption.equals(s)) {
                        Log.d("minto", s);
                        statList = team.getJSONArray(s);
                        for(int i = 0; i < statList.length(); i++) {
                            average += statList.getInt(i);
                        }
                        average /= statList.length();
                        break;
                    }
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
                Log.d("minto", "big yikers");
            }

            //Log.d("minto", "average: " + Double.toString(average));
            teamNum.setText(Integer.toString(num));
            teamStat.setText(Double.toString(average));
            teleopText.setText("Teleop\t\t\n" + Double.toString(Math.round(DataHandler.getScoreAverage(teleopNums))));
            autoText.setText("Autonomous\t\t\n" + Double.toString(Math.round(DataHandler.getScoreAverage(autoNums))));
        }
    }
}

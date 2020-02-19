package com.example.a2020frcscouter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    public void onBindViewHolder(GolumnViewHolder holder, int position) {
        holder.bind(DataHandler.teamList.get(position), dankListener);

    }

    @Override
    public int getItemCount() {
        return DataHandler.teamList.size();
    }





    public static class GolumnViewHolder extends RecyclerView.ViewHolder {
        TextView teamNum;
        TextView teamStat;
        TextView blinkText;
        String sortOption;

        public GolumnViewHolder(View itemView, String s) {
            super(itemView);
            teamNum = itemView.findViewById(R.id.TeamNumText);
            teamStat = itemView.findViewById(R.id.statText);
            blinkText = itemView.findViewById(R.id.blinkTest);
            sortOption = s;

            blinkText.setVisibility(View.GONE);
        }

        public void bind(final JSONObject team, final OnDankListener listener) {
            JSONArray statList;
            double average = 0;
            int num = -1;
            try {
                num = team.getInt("teamNum");
                for(String s : DataHandler.genericJsonKeys) {
                    if(sortOption.equals(s)) {
                       // Log.d("minto", "proc");
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
            }

            //Log.d("minto", "average: " + Double.toString(average));
            teamNum.setText(Integer.toString(num));
            teamStat.setText(Double.toString(average));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //listener.onDank(team);
                    blinkText.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}

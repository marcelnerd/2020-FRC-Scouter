package com.example.a2020frcscouter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.RequestQueue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button button;
    static TBAHandler handler;
    static ListView list;
    public static String TBAKey;
    public static Spinner sortSpinner;
    public static ArrayAdapter<CharSequence> selectionAdapter;

    private OnFragmentInteractionListener mListener;

    public TeamListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamListFragment newInstance(String param1, String param2) {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        handler = new TBAHandler(MyAppy.getAppContext());

        handler.helper.onUpgrade(handler.helper.getWritableDatabase(), 0, 4);

        selectionAdapter = ArrayAdapter.createFromResource(MyAppy.getAppContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        selectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        ///////////
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);
        view.setBackgroundColor(Color.WHITE);

        button = view.findViewById(R.id.button);
        list = view.findViewById(R.id.listMain);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        sortSpinner.setAdapter(selectionAdapter);

        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHandler.clearTeams();

                for (int i = 1; i < 82; i++) {
                    handler.getMatchData(String.format("/match/%1$s_qm%2$d", "2019caoc", i));
                }

                DataHandler.printTeamsList();
//                FrodoCursorAdapter todoAdapter = new FrodoCursorAdapter(MainActivity.c, helper.getAllEntriesTeleopCursor(), "teleop");
//                MainActivity.list.setAdapter(todoAdapter);
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GolumnListAdapter boboAdapter;
                switch(position) {
                    case 0:
                        boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "teleopPoints");
                        list.setAdapter(boboAdapter);
                        break;
                    case 1:
                        boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "autoPoints");
                        list.setAdapter(boboAdapter);
                        break;
                    case 2:
                        boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "hatchPanelPoints");
                        list.setAdapter(boboAdapter);
                        break;
                    case 3:
                        boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "cargoPoints");
                        list.setAdapter(boboAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                GolumnListAdapter boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "teamNum");
                list.setAdapter(boboAdapter);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                SQLiteCursor cursor = (SQLiteCursor) parent.getItemAtPosition(position);
//                Intent intent = new Intent(MainActivity.c, TeamInfoActivity.class);
//
//                intent.putExtra("teamNum", Integer.toString(cursor.getInt(0)));
//                intent.putExtra("teleop", Float.toString(cursor.getFloat(1)));
//                intent.putExtra("cargoPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("cargoPoints"))));
//                intent.putExtra("hatchPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("hatchPoints"))));
//                intent.putExtra("autoPoints", Float.toString(cursor.getFloat(cursor.getColumnIndex("autoPoints"))));
//                startActivity(intent);

                for (Fragment fragment : getFragmentManager().getFragments()) {
                    getFragmentManager().beginTransaction().remove(fragment).commit();
                }

                FragmentManager manager = getFragmentManager(); // Might break
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.mainLayout, new TeamInfoFragment(), "teamInfoFraggy");
                transaction.commit();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

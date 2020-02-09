package com.example.a2020frcscouter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
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

    static ListView list;
    public static String TBAKey;
    public static Spinner sortSpinner;
    public static ArrayAdapter<CharSequence> selectionAdapter;

    SwipeRefreshLayout refreshLayout;

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

        selectionAdapter = ArrayAdapter.createFromResource(MyAppy.getAppContext(), R.array.sort_array, android.R.layout.simple_spinner_item);
        selectionAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_list, container, false);
        view.setBackgroundColor(Color.WHITE);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        list = view.findViewById(R.id.listMain);
        sortSpinner = view.findViewById(R.id.sortSpinner);
        sortSpinner.setAdapter(selectionAdapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //DataHandler.clearTeams();

                TBAHandler.requestMatchKeys();

                //DataHandler.printTeamsList();

                list.setAdapter(getSelectedAdapter(sortSpinner.getSelectedItemPosition()));

                refreshLayout.setRefreshing(false);
            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.setAdapter(getSelectedAdapter(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                TBAHandler.requestMatchKeys();
                list.setAdapter(new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "teamNum"));
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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

    public static GolumnListAdapter getSelectedAdapter(int position) {
        GolumnListAdapter boboAdapter = null;
        switch(position) {
            case 0:
                boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "teleopPoints");
                break;
            case 1:
                boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "autoPoints");
                break;
            case 2:
                boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "hatchPanelPoints");
                break;
            case 3:
                boboAdapter = new GolumnListAdapter(MyAppy.getAppContext(), R.layout.team_entry_golumn, DataHandler.teamList, "cargoPoints");
        }
        return boboAdapter;
    }
}

package com.example.a2020frcscouter;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView textView;

    // TODO: Rename and change types of parameters
    private SQLiteCursor cursor;

    private OnFragmentInteractionListener mListener;

    public TeamInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment TeamInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamInfoFragment newInstance(SQLiteCursor cursor) {
        TeamInfoFragment fragment = new TeamInfoFragment();
        Bundle args = new Bundle();
        //args.putSerializable("team", team);// TODODODODOD ADD TEAM AS PARAM
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          // mTeam = (FRC2018Team) getArguments().getSerializable("team");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        textView = getView().findViewById(R.id.testText);
        textView.setText(cursor.getInt(0));
        return inflater.inflate(R.layout.fragment_team_info, container, false);
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

    public void setThing(SQLiteCursor c) {
         cursor = c;
    }
}

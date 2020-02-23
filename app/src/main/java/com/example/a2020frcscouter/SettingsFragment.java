package com.example.a2020frcscouter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner eventSpinner;
    private ArrayAdapter<String> eventAdapter;

    private boolean lmaoProgramming;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        //DataHandler.readSettingsFile();

        lmaoProgramming = false;

        eventAdapter = new ArrayAdapter<>(MyAppy.getAppContext(), android.R.layout.simple_spinner_item, TBAHandler.getEventNames());

        TBAKeysJSONRequest request = new TBAKeysJSONRequest(Request.Method.GET, "https://www.thebluealliance.com/api/v3/events/2020/simple", null, new TBAEventKeysListener(), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("minto", "Json Request encountered error");
                error.printStackTrace();
                Log.v("minto", error.getMessage() + "");
            }

        });

        MainActivity.queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        eventSpinner = view.findViewById(R.id.eventSpinner);

        eventSpinner.setAdapter(eventAdapter);

        eventSpinner.setSelection(TBAHandler.getCurrentEventIndex());

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(lmaoProgramming) {
                    //Log.d("minto", "true case");
                    TBAHandler.setCurrentEvent((String) ((TextView) view).getText());
                    TBAHandler.requestMatchKeys();
                    SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("currentEventName", TBAHandler.getCurrentEventName());
                    editor.putString("currentEventKey", TBAHandler.getCurrentEventKey());
                    editor.commit();
                }
                else {
                    Log.d("minto", "else case");
                    parent.setSelection(TBAHandler.getCurrentEventIndex());
                    lmaoProgramming = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                eventSpinner.setAdapter(new ArrayAdapter<>(MyAppy.getAppContext(), android.R.layout.simple_spinner_item, TBAHandler.getEventNames()));
                parent.setSelection(TBAHandler.getCurrentEventIndex());
            }
        });

        return view;
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

    private void setSettings() {

    }
}

package glass.door.a2020frcscouter;

import android.content.Context;
import android.database.sqlite.SQLiteCursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a2020frcscouter.R;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamInfoFragment2019.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamInfoFragment2019#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView teamText, teleopText, autoText, hatchText, cargoText;
    String teamNum;

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
     * @return A new instance of fragment TeamInfoFragment2019.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamInfoFragment2019 newInstance(JSONObject team) {
        TeamInfoFragment2019 fragment = new TeamInfoFragment2019();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_team_info_2019, container, false);
        //view.setBackgroundColor(Color.WHITE);
        teamNum = this.getArguments().getString("teamNum");

        teamText = view.findViewById(R.id.numTextView);

        teleopText = view.findViewById(R.id.teleopText);
        autoText = view.findViewById(R.id.autoText);
        hatchText = view.findViewById(R.id.hatchText);
        cargoText = view.findViewById(R.id.cargoText);

        teamText.setText(teamNum);

        // Inflate the layout for this fragment
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

}

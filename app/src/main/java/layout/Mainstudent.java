package layout;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.gsc.template2.Back.Utils.Utils;
import com.example.gsc.template2.MainActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.TeacherActivity;

import com.wooplr.spotlight.SpotlightView;

import java.util.HashMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.example.gsc.template2.MainActivity.menu;
import static com.example.gsc.template2.MainActivity.toolbar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlankFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mainstudent extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Mainstudent() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_mainstudent, container, false) ;
        SliderLayout mSlider = (SliderLayout)view.findViewById(R.id.myslider);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Teachers easier to find",R.drawable.selection);
        file_maps.put("get help easily",R.drawable.learn);
        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(
                            new BaseSliderView.OnSliderClickListener() {

                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    MainActivity.drawer.openDrawer(GravityCompat.START);
                                    TextView locButton = (TextView) MainActivity.nav.getMenu().findItem(R.id.nav_share).getActionView();
                                    TextView disuccuin = (TextView) MainActivity.nav.getMenu().findItem(R.id.nav_send).getActionView();
                                    TextView map = (TextView) MainActivity.nav.getMenu().findItem(R.id.map).getActionView();

                                    TextView find = (TextView) MainActivity.nav.getMenu().findItem(R.id.find).getActionView();
                                    // locButton.setLayoutParams((new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)));
                                    locButton.setMaxWidth(Integer.MAX_VALUE);


                                    ShowcaseConfig config = new ShowcaseConfig();
                                   // half second between each showcase view

                                    MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity());
                                    sequence.addSequenceItem(Utils.create(getActivity(),locButton,"Here you can fnd the list of the request you sent and manage them",0));
                                    sequence.addSequenceItem(Utils.create(getActivity(),disuccuin,"Here you can fnd the list of your discussions",0));
                                    sequence.addSequenceItem(Utils.create(getActivity(),find,"Here you can  Search for teachres by price,speciality..",0));
                                    sequence.addSequenceItem(Utils.create(getActivity(),find,"in this section you find the nearest teacher to you..",0));





                                    // optional but starting animations immediately in onCreate can make them choppy

                                    sequence.setConfig(config);

                                


                                    sequence.start();
                                    // Find Menu

                                    FloatingActionButton floatingActionButton = ((MainActivity) getActivity()).fab;




                                }
                            }

                    );

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mSlider.addSlider(textSliderView);
        }

        // Inflate the layout for this fragment
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

    }

    @Override
    public void onDetach() {
        super.onDetach();

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

package layout;

import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Adapter.ViewPagerAdapter;

import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;
import com.squareup.leakcanary.RefWatcher;


public class TabrRequest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CollapsingToolbarLayout cp ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public TabrRequest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabrRequest.
     */
    // TODO: Rename and change types and number of parameters
    public static TabrRequest newInstance(String param1, String param2) {
        TabrRequest fragment = new TabrRequest();
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

        View v = inflater.inflate(R.layout.fragment_tabr_request, container, false);


        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) v);
        // Inflate the layout for this fragment
        return v;
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new StudentPending(), "Pending");
        adapter.addFragment(new StudentRequestList(), "Accepted");
        adapter.addFragment(new StudentRefused(), "Refused");
        viewPager.setAdapter(adapter);
    }

    @Override public void onDestroy() {
        super.onDestroy();
    }
}

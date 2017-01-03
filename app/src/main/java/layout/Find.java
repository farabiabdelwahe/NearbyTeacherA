package layout;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.gsc.template2.AppName;
import com.example.gsc.template2.Back.Utils.FontChangeCrawler;
import com.example.gsc.template2.R;

import info.hoang8f.widget.FButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link Find#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Find extends Fragment  implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText pr;
    AutoCompleteTextView spec;
 RatingBar ratingbar ;


    public Find() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Find.
     */
    // TODO: Rename and change types and number of parameters
    public static Find newInstance(String param1, String param2) {
        Find fragment = new Find();
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
         View v = inflater.inflate(R.layout.fragment_find, container, false);
        ratingbar = (RatingBar) v.findViewById(R.id.dialog_ratingbar) ;
        pr = (EditText)  v.findViewById(R.id.input_price);
        String[] foo_array =  getActivity().getResources().getStringArray(R.array.subjects);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(),android.R.layout.select_dialog_item,foo_array);

        spec = (AutoCompleteTextView)  v.findViewById(R.id.input_speciality);
       spec.setThreshold(1);//will start working from first character
        spec.setAdapter(adapter);

        FButton b =  (FButton)
                v.findViewById(R.id.btn_find);
        b.setOnClickListener(this);
        // TextView nam = (TextView)  v.findViewById(R.id.spec);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "fonts/myfont.ttf");
        fontChanger.replaceFonts((ViewGroup) v);


        return  v ;
    }

    // TODO: Rename method, update argument and hook method into UI event



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {

        ((AppName) getActivity().getApplication()).setPrice(Double.parseDouble(pr.getText().toString()));
        ((AppName) getActivity().getApplication()).setSpec(spec.getText().toString());
        ((AppName) getActivity().getApplication()).setRating(ratingbar.getRating());


        getFragmentManager().beginTransaction().replace(R.id.content_main,new Teacher()).addToBackStack(null).commit();
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

    @Override
    public void onPause()
    {
        super.onPause();

        unbindDrawables(getActivity().findViewById(R.id.content_main));
        System.gc();
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        unbindDrawables(getActivity().findViewById(R.id.content_main));
        System.gc();
    }

    private void unbindDrawables(View view)
    {
        if (view.getBackground() != null)
        {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView))
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
}

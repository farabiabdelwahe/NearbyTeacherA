package layout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.example.gsc.template2.LoginActivity;
import com.example.gsc.template2.MainActivity;
import com.example.gsc.template2.R;
import com.example.gsc.template2.TeacherActivity;

public class UpdateProfileTeacher extends Fragment implements View.OnClickListener {
    Button _upButton;
    EditText _nameText,_priceText,_mobileText,_emailText,_passwordText;
    BackendlessUser u;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public UpdateProfileTeacher() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileTeacher.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfileTeacher newInstance(String param1, String param2) {
        UpdateProfileTeacher fragment = new UpdateProfileTeacher();
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

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_update_profile_teacher, container, false);

        _upButton = (Button) v.findViewById(R.id.btn_up);
        _nameText=(EditText) v.findViewById(R.id.input_name);
        _priceText=(EditText) v.findViewById(R.id.input_price);
        _mobileText=(EditText) v.findViewById(R.id.input_mobile);
        _emailText=(EditText) v.findViewById(R.id.input_email);
        _passwordText=(EditText) v.findViewById(R.id.input_password);
        _upButton.setOnClickListener(this);

         u = Backendless.UserService.CurrentUser();

        if (u != null) {
            String email = (String) u.getProperty("email");
            String name = (String) u.getProperty("name");
            String password = (String) u.getProperty("password");
            String Tel = (String) u.getProperty("Tel");
            Double price = Double.parseDouble(u.getProperty("price").toString());


            _nameText.setText(name);
            _passwordText.setText(password);
            // String FirstName = etFirstName.getText().toString();
            //   String LastName = etLastName.getText().toString();
            _emailText.setText(email);
            _mobileText.setText(Tel);
            _priceText.setText(price.toString());
        } else {
            Log.e("not connected", "");

        }



        return  v;

    }


    @Override
    public void onClick(View view) {



        final BackendlessUser user1 = new BackendlessUser();

      user1.setProperty( "objectId", u.getProperty("objectId").toString());

        user1.setProperty("Tel", _mobileText.getText().toString());
        user1.setProperty("name", _nameText.getText().toString());
        user1.setProperty("email", _emailText.getText().toString());
        user1.setProperty("price",Double.parseDouble( _priceText.getText().toString()));

        Backendless.UserService.update( user1, new AsyncCallback<BackendlessUser>()
        {
            @Override
            public void handleResponse( BackendlessUser backendlessUser )
            {
                SharedPreferences prefs1 = getActivity().getSharedPreferences(LoginActivity.params, getActivity().MODE_PRIVATE);
                String log = prefs1.getString("login", null);
                String pa = prefs1.getString("password", null);
                Backendless.UserService.login( log, pa, new AsyncCallback<BackendlessUser>()
                {


                    public void handleResponse( BackendlessUser user )
                    {

                        getFragmentManager().beginTransaction().replace(R.id.content_teacher,new MyteacherProfile()).addToBackStack(null).commit();

                    }// user has been logged in


                    public void handleFault( BackendlessFault fault )
                    {

                        Toast.makeText(getActivity(), fault.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                });



                Toast.makeText(getActivity(), "User updated ", Toast.LENGTH_SHORT ).show();

                Log.e("ffffffff","ssssssss");
                Log.e("ffffffff", _nameText.getText().toString());


            }

            @Override
            public void handleFault( BackendlessFault backendlessFault )
            {
                Log.e("ffffffff",backendlessFault.getMessage());
            }
        } );

    }

}

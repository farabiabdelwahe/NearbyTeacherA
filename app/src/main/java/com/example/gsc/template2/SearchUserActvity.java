package com.example.gsc.template2;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.UserService;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.example.gsc.template2.Back.User;
import com.example.gsc.template2.Back.Users;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import layout.Teacher;

public class SearchUserActvity extends AppCompatActivity implements View.OnClickListener{
    Button btn_find;
    ListView liste;
    EditText input_speciality,input_price;
    @Bind(R.id.input_speciality) EditText _spec;
    private BackendlessCollection<Users>users;
    private List<Users> allUsers= new ArrayList<>();
    private boolean  isLoadingItems=false;
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_user_actvity);

        input_speciality = (EditText) findViewById(R.id.input_speciality);
        input_price = (EditText) findViewById(R.id.input_price);

        btn_find = (Button) findViewById(R.id.btn_find);
        btn_find.setOnClickListener(this);

       /* adapter = new UsersAdapter(SearchUserActvity.this,R.layout.activity_search_user_actvity,allUsers);
        setListAdapter((ListAdapter) adapter);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.getClass();
       // queryOptions.setRelated(Arrays.asList("speciality"));

        BackendlessDataQuery query = new BackendlessDataQuery(queryOptions);
        Backendless.Data.of(Users.class).find(query,new Loa<BackendlessCollection<Users>>(this,"loading Users",true)
        {
            @Override
            public void handleResponse(BackendlessCollection<Users>usersBackendlessCollection)
            {
                users=usersBackendlessCollection;
                addMoreItems(usersBackendlessCollection);
        //   super.handleResponse(usersBackendlessCollection);

            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }

        });

        btn_find = (Button) findViewById(R.id.btn_find);
        liste = (ListView) findViewById(R.id.liste);
        liste.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                {
                    if(needToLoadItems(firstVisibleItem,visibleItemCount,totalItemCount)))
isLoadingItems=true;
                    users.nextPage(new AsyncCallback<BackendlessCollection<Users>>() {
                        @Override
                        public void handleResponse(BackendlessCollection<Users> nextPage) {
                            users=nextPage;
                            addMoreItems(nextPage);
                            isLoadingItems=false;
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {

                        }


                    });
                }
            }


        });
    }

private boolean needToLoadItems(int firstVisibileItem,int visibleItemCount,int totalItemCount)
{
    return !isLoadingItems&& totalItemCount !=0 &&totalItemCount-(visibleItemCount+firstVisibileItem)<visibleItemCount/2;
}

private  void addMoreItems(BackendlessCollection<Users>nextPage)
{
    allUsers.addAll(nextPage.getCurrentPage());

}

*/

}
    @Override
    public void onClick(View v) {
        Double prix= Double.parseDouble(input_price.getText().toString());
        ((AppName) this.getApplication()).setPrice(prix);
        ((AppName) this.getApplication()).setSpec(input_speciality.getText().toString());

        getFragmentManager().beginTransaction().replace(R.id.content_main,new Teacher()).addToBackStack(null).commit();


    }
}
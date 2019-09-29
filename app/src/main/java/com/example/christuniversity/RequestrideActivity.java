package com.example.christuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RequestrideActivity extends AppCompatActivity {

    private ListView listView;
    private RetroAdapter1 retroAdapter1;
    private INodeJs myAPI;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit retrofit = RetrofitClient.getInstance();
    private ArrayList<RequestListView> requestListViewArrayList;
    private Button _reqbtn;
    private Session session;
    private HashMap<String, String> uid;
    private String tid,uid1;
    private TextView trip_id;
    private Toolbar toolbar;
    TextView tvrequest_id;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestride);

        session = new Session(RequestrideActivity.this);

        uid = session.getUserDetails();
        uid1=uid.toString();


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestrideActivity.this, Homepage.class);
                startActivity(intent);
                finish();
            }
        });

        listView = findViewById(R.id.lv);

        getJSONResponse();

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

                                View v = (View) listView.getChildAt(position);
                                FrameLayout frameLayout = (FrameLayout) v;

                                LinearLayout linearLayoutParent = (LinearLayout) frameLayout.getChildAt(0);

                                // Getting the inner Linear Layout
                                LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(1);

                                // Getting the Country TextView
                                tvrequest_id = (TextView) linearLayoutChild.getChildAt(3);
                                //Toast.makeText(RequestrideActivity.this, tvrequest_id.getText().toString(), Toast.LENGTH_SHORT).show();

                                // Getting the Country TextView
                                send_decline_requestid(uid1,tvrequest_id.getText().toString());
                                retroAdapter1.remove(position);



                            }
                        });


        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                }
                else
                    {

                    FrameLayout frameLayout = (FrameLayout) view;

                    LinearLayout linearLayoutParent = (LinearLayout) frameLayout.getChildAt(0);

                    // Getting the inner Linear Layout
                    LinearLayout linearLayoutChild = (LinearLayout) linearLayoutParent.getChildAt(1);

                    // Getting the Country TextView
                    tvrequest_id = (TextView) linearLayoutChild.getChildAt(3);
                    send_accept_requestid(uid1,tvrequest_id.getText().toString());

                }
            }
        });

    }




    /*private void getriderequest_listview(final String user_id, final String trip_id) {

        compositeDisposable.add(myAPI.getriderequest_listview(user_id,trip_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(RequestrideActivity.this, "Your Registration is successful" + s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }
*/

    private void getJSONResponse(){


        myAPI = retrofit.create(INodeJs.class);

        Call<String> call = myAPI.getriderequest_listview();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeListView(jsonresponse);

                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeListView(String response){

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                requestListViewArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for(int i = 0; i < dataArray.length(); i++) {

                    RequestListView requestListView = new RequestListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    //modelListView.setImgURL(dataobj.getString("imgURL"));
                    requestListView.setName(dataobj.getString("first_name"));
                    requestListView.setmobile(dataobj.getString("mobile"));
                    requestListView.setcollege(dataobj.getString("college"));
                    requestListView.setseats(dataobj.getString("seats"));
                    requestListView.setrequest_id(dataobj.getString("req_id"));

                    requestListViewArrayList.add(requestListView);

                }

                retroAdapter1 = new RetroAdapter1(this, requestListViewArrayList);
                listView.setAdapter(retroAdapter1);

            }else {
                Toast.makeText(RequestrideActivity.this, "No Passenger Request", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void send_accept_requestid(final String user_id, final String request_id) {



        compositeDisposable.add(myAPI.send_accept_requestid(user_id,request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        Toast.makeText(RequestrideActivity.this, "Ride Request Accepted", Toast.LENGTH_LONG).show();

                    }
                }));

    }

    private void send_decline_requestid(final String user_id, final String request_id) {

        compositeDisposable.add(myAPI.send_decline_requestid(user_id,request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(RequestrideActivity.this, "Ride Request Declined", Toast.LENGTH_SHORT).show();

                    }
                }));

    }

}

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

import static com.paytm.pgsdk.easypay.manager.PaytmAssist.getContext;

public class BookingActivity extends AppCompatActivity {

    private ListView listView;
    private RetroAdapter2 retroAdapter;
    private INodeJs myAPI;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit retrofit = RetrofitClient.getInstance();
    private ArrayList<ModelListView1> modelListViewArrayList;
    private Button _reqbtn;
    private Session session;
    private HashMap<String, String> uid;
    private String tid,uid1,seats;
    private TextView trip_id;
    private Toolbar toolbar;
    private ProgressDialog mProgress;
    private TextView tvrequest_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        session = new Session(BookingActivity.this);

        uid = session.getUserDetails();
        uid1=uid.toString();
        listView = findViewById(R.id.lv);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookingActivity.this, Homepage.class);
                startActivity(intent);
                finish();
            }
        });


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
                                tvrequest_id = (TextView) linearLayoutChild.getChildAt(0);
                                //Toast.makeText(RequestrideActivity.this, tvrequest_id.getText().toString(), Toast.LENGTH_SHORT).show();

                                // Getting the Country TextView
                                get_passenger_trip(tvrequest_id.getText().toString());
                                retroAdapter.remove(position);

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
            }
        });

    }


    private void getJSONResponse(){

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
*/
        myAPI = retrofit.create(INodeJs.class);

        Call<String> call = myAPI.getpassenger_listview();

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
                        Toast.makeText(getContext(),"Nothing To Display",Toast.LENGTH_LONG).show();
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

                modelListViewArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for(int i = 0; i < dataArray.length(); i++) {

                    ModelListView1 modelListView = new ModelListView1();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    //modelListView.setImgURL(dataobj.getString("imgURL"));
                    modelListView.setrequest_id(dataobj.getString("request_id"));
                    modelListView.settime(dataobj.getString("time"));
                    modelListView.setorigin(dataobj.getString("origin"));
                    modelListView.setdestination(dataobj.getString("destination"));
                    modelListView.settrip_id(dataobj.getString("trip_id"));
                    modelListViewArrayList.add(modelListView);

                }

                retroAdapter = new RetroAdapter2(this, modelListViewArrayList);
                listView.setAdapter(retroAdapter);

            }else {
                Toast.makeText(BookingActivity.this, "No Records To Display", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void get_passenger_trip(final String request_id) {

        compositeDisposable.add(myAPI.get_passenger_trip(request_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        Toast.makeText(BookingActivity.this, "Ride Is Cancelled", Toast.LENGTH_LONG).show();

                    }
                }));

    }

}

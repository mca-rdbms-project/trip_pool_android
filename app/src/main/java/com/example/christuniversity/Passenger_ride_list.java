package com.example.christuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

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

public class Passenger_ride_list extends AppCompatActivity {

    private ListView listView;
    private RetroAdapter retroAdapter;
    private INodeJs myAPI;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Retrofit retrofit = RetrofitClient.getInstance();
    private ArrayList<ModelListView> modelListViewArrayList;
    private Button _reqbtn;
    private Session session;
    private HashMap<String, String> uid;
    private String tid,uid1,seats;
    private TextView trip_id;
    private Toolbar toolbar;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_list);

        session = new Session(Passenger_ride_list.this);

        uid = session.getUserDetails();
        uid1=uid.toString();
        listView = findViewById(R.id.lv);

        seats = getIntent().getExtras().getString("seats");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Passenger_ride_list.this, Homepage.class);
                startActivity(intent);
                finish();
            }
        });




        //ModelListView obj = new ModelListView();
        //tid=obj.gettrip_id();

        //_reqbtn = (Button)findViewById(R.id.reqbtn);

        getJSONResponse();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                LinearLayout linearLayoutParent = (LinearLayout) view;

                // Getting the inner Linear Layout
                LinearLayout linearLayoutChild = (LinearLayout ) linearLayoutParent.getChildAt(1);

                // Getting the Country TextView
                TextView tvCountry = (TextView) linearLayoutChild.getChildAt(5);


                //Toast.makeText(getBaseContext(), tvCountry.getText().toString(), Toast.LENGTH_SHORT).show();

                requestinfo(uid1,tvCountry.getText().toString(),seats);

                Intent intent = new Intent(Passenger_ride_list.this,Homepage.class);
                startActivity(intent);
                finish();

            }
        });
        /*getJSONResponse();*/
    }


    private void requestinfo(final String user_id, final String trip_id, final String seat) {

        mProgress = new ProgressDialog(Passenger_ride_list.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        //mProgress.setTitle("Processing...");
        mProgress.setMessage("Waiting for Rider To Accept");
        //mProgress.setCancelable(false);
        mProgress.show();



        compositeDisposable.add(myAPI.requestinfo(user_id,trip_id,seat)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //JSONObject obj1 = new JSONObject(s);
                        //tid = obj1.optString("trip_id");

                        mProgress.dismiss();
                        Toast.makeText(Passenger_ride_list.this, "Your Request Is Been Accept" + s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


    private void getJSONResponse(){

        /*Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
*/
        myAPI = retrofit.create(INodeJs.class);

        Call<String> call = myAPI.getString_listview();

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

                    ModelListView modelListView = new ModelListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    //modelListView.setImgURL(dataobj.getString("imgURL"));
                    modelListView.setName(dataobj.getString("first_name"));
                    modelListView.settime(dataobj.getString("time"));
                    modelListView.setv_details(dataobj.getString("v_details"));
                    modelListView.setrules(dataobj.getString("rules"));
                    modelListView.setamount(dataobj.getString("amount"));
                    modelListView.setmobile(dataobj.getString("mobile"));
                    modelListView.settrip_id(dataobj.getString("trip_id"));

                    modelListViewArrayList.add(modelListView);

                }

                retroAdapter = new RetroAdapter(this, modelListViewArrayList);
                listView.setAdapter(retroAdapter);

            }else {
                Toast.makeText(Passenger_ride_list.this, "No Records To Display", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
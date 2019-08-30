package com.example.christuniversity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Passenger_ride_list extends AppCompatActivity {

    private ListView listView;
    private RetroAdapter retroAdapter;
    INodeJs myAPI;
    Retrofit retrofit = RetrofitClient.getInstance();
    ArrayList<ModelListView> modelListViewArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_ride_list);

        listView = findViewById(R.id.lv);

        getJSONResponse();
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

                modelListViewArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("data");

                for (int i = 0; i < dataArray.length(); i++) {

                    ModelListView modelListView = new ModelListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    //modelListView.setImgURL(dataobj.getString("imgURL"));
                    modelListView.setName(dataobj.getString("name"));
                    //modelListView.setCountry(dataobj.getString("country"));
                    modelListView.setCity(dataobj.getString("city"));

                    modelListViewArrayList.add(modelListView);

                }

                retroAdapter = new RetroAdapter(this, modelListViewArrayList);
                listView.setAdapter(retroAdapter);

            }else {
                Toast.makeText(Passenger_ride_list.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}

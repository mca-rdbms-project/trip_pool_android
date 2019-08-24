package com.example.christuniversity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.R.layout.simple_spinner_item;


public class Registration extends AppCompatActivity {

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ArrayList<SpinnerModel> goodModelArrayList;
    ArrayList<SpinnerModel1> goodModelArrayList1;

    ArrayList<String> cities = new ArrayList<String>();
    Spinner spinner;

    ArrayList<String> colleges = new ArrayList<String>();
    Spinner spinner1;


    Retrofit retrofit = RetrofitClient.getInstance();

    EditText _fname, _mname, _lname, _email, _mno, _idphoto, _usertype, _gender, _password;
    Button _instbtn;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //InitAPI
        //Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        _fname = (EditText) findViewById(R.id.f_name);
        _mname = (EditText) findViewById(R.id.m_name);
        _lname = (EditText) findViewById(R.id.l_name);
        _email = (EditText) findViewById(R.id.email);
        _mno = (EditText) findViewById(R.id.mno);
        //_city = (EditText) findViewById(R.id.city);
        //_college = (EditText) findViewById(R.id.college);
        _idphoto = (EditText) findViewById(R.id.id_photo);
        _usertype = (EditText) findViewById(R.id.user_type);
        _gender = (EditText) findViewById(R.id.gender);
        _password = (EditText) findViewById(R.id.password);

        spinner = findViewById(R.id.city);
        fetchJSON();

        /*spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityid(city_id.getText.toString());
            }
        });*/

        spinner1 = findViewById(R.id.college);
        //fetchJSON1();

        _instbtn = (Button) findViewById(R.id.instbtn);

        _instbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(_fname.getText().toString(),
                        _mname.getText().toString(),
                        _lname.getText().toString(),
                        _email.getText().toString(),
                        _mno.getText().toString(),
                        spinner.getSelectedItem().toString(),
                        spinner1.getSelectedItem().toString(),
                        _idphoto.getText().toString(),
                        _usertype.getText().toString(),
                        _gender.getText().toString(),
                        _password.getText().toString());

            }
        });
    }

    //spinner population for city
    private void fetchJSON(){

        //Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);
        //SpinnerInterface api = retrofit.create(SpinnerInterface.class);

        Call<String> call = myAPI.getJSONString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                //Log.d(response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);


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

    /*public void cityid(final String city_id)
    {
        compositeDisposable.add(myAPI.cityid(city_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(Registration.this, "College is fetched"+s, Toast.LENGTH_SHORT).show();

                    }
                }));
    }
*/
    private void spinJSON(String response){

        try {

            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                goodModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("list");

                for (int i = 0; i < dataArray.length(); i++) {

                    SpinnerModel spinnerModel = new SpinnerModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    spinnerModel.setCityid(dataobj.getString("city_id"));
                    spinnerModel.setCity(dataobj.getString("city_name"));

                    goodModelArrayList.add(spinnerModel);

                }

                for (int i = 0; i < goodModelArrayList.size(); i++){
                    cities.add(goodModelArrayList.get(i).getCity());
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Registration.this, simple_spinner_item, cities);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //spinner population for college
    /*private void fetchJSON1(){

        //Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);
        //SpinnerInterface api = retrofit.create(SpinnerInterface.class);

        Call<String> call = myAPI.getJSONString1();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                //Log.d(response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse1 = response.body().toString();
                        spinJSON1(jsonresponse1);


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

    private void spinJSON1(String response){

        try {

            JSONObject obj1 = new JSONObject(response);
            if(obj1.optString("status").equals("true")){

                goodModelArrayList1 = new ArrayList<>();
                JSONArray dataArray1  = obj1.getJSONArray("list1");

                for (int i = 0; i < dataArray1.length(); i++) {

                    SpinnerModel1 spinnerModel1 = new SpinnerModel1();
                    JSONObject dataobj1 = dataArray1.getJSONObject(i);

                    spinnerModel1.setCollegeid(dataobj1.getString("college_id"));
                    spinnerModel1.setCollege(dataobj1.getString("college_name"));

                    goodModelArrayList1.add(spinnerModel1);

                }

                for (int i = 0; i < goodModelArrayList1.size(); i++){
                    colleges.add(goodModelArrayList1.get(i).getCollege());
                }

                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(Registration.this, simple_spinner_item, colleges);
                spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner1.setAdapter(spinnerArrayAdapter1);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

*/
    private void registerUser(final String f_name, final String m_name, final String l_name, final String email, final String mno, final String city, final String college, final String id_photo, final String user_type, final String gender, final String password) {

       /* new MaterialStyledDialog.Builder(this)
                .setTitle("Register")
                .setDescription("One more step")
                .setNegativeText("Cancel")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveText("Register")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {*/
                        compositeDisposable.add(myAPI.registerUser(f_name, m_name, l_name, email, mno, city, college, id_photo, user_type, gender, password)
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(new Consumer<String>() {
                                                    @Override
                                                    public void accept(String s) throws Exception {
                                                        Toast.makeText(Registration.this, "Your Registration is successful"+s, Toast.LENGTH_SHORT).show();

                                                    }
                                                }));

                    }




    }

//}


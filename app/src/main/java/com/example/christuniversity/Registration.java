package com.example.christuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
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


public class Registration extends AppCompatActivity {


    private INodeJs myAPI;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<SpinnerModel> goodModelArrayList;
    private ArrayList<SpinnerModel1> goodModelArrayList1;

    private ArrayList<String> cities = new ArrayList<String>();
    private ArrayList<String> colleges = new ArrayList<String>();

    private Spinner spinner, spinner1, _usertype, _gender;

    private SpinnerModel spinnerModel;

    private JSONObject dataobj;

    private Retrofit retrofit = RetrofitClient.getInstance();

    private EditText _fname, _mname, _lname, _email, _mno, _idphoto, _password;

    private Button _instbtn;

    private String spin1,spin2;

    private int spin,spin_1;

    private AwesomeValidation awesomeValidation;

    private ProgressDialog mProgress;


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

        myAPI = retrofit.create(INodeJs.class);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        _fname = (EditText) findViewById(R.id.f_name);
        _mname = (EditText) findViewById(R.id.m_name);
        _lname = (EditText) findViewById(R.id.l_name);
        _email = (EditText) findViewById(R.id.email);
        _mno = (EditText) findViewById(R.id.mno);
        _password = (EditText) findViewById(R.id.password);

        spinner = (Spinner) findViewById(R.id.city);
        fetchJSON();

        spinner1 = (Spinner) findViewById(R.id.college);
        fetchJSON1();

        _instbtn = (Button) findViewById(R.id.instbtn);

        _usertype =(Spinner) findViewById(R.id.usertype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _usertype.setAdapter(adapter);

        _gender =(Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _gender.setAdapter(adapter1);


        awesomeValidation.addValidation(this, R.id.f_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.l_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.mno, "^[0-9]{3}[0-9]{7}$", R.string.mobileerror);
        awesomeValidation.addValidation(this, R.id.password, "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$^+=!*()@%&]).{8,10}$", R.string.passerror);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spin=position;
                spin1=String.valueOf(spin);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spin_1=position;
                spin2=String.valueOf(spin_1);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _instbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()){

                    if (_usertype.getSelectedItem().toString().trim().equals("Select City")) {
                        Toast.makeText(Registration.this, "Please Select a City", Toast.LENGTH_SHORT).show();}
                    else if (_usertype.getSelectedItem().toString().trim().equals("Select College")) {
                            Toast.makeText(Registration.this, "Please Select a College", Toast.LENGTH_SHORT).show();}
                    else if (_usertype.getSelectedItem().toString().trim().equals("User Type")) {
                        Toast.makeText(Registration.this, "Please Select a User Type", Toast.LENGTH_SHORT).show();
                    } else if (_gender.getSelectedItem().toString().trim().equals("Gender Type")) {
                        Toast.makeText(Registration.this, "Please Select a Gender Type", Toast.LENGTH_SHORT).show();
                    } /*else if (mImageView.getDrawable() == null) {
                        Toast.makeText(Registration.this, "Please Select a photo", Toast.LENGTH_SHORT).show();
                    }*/

                    /*else if (mBitmap != null)
                    {
                        multipartImageUpload();
                    }
*/
                    else
                    {
                        registerUser(_fname.getText().toString(),
                                _mname.getText().toString(),
                                _lname.getText().toString(),
                                _email.getText().toString(),
                                _mno.getText().toString(),
                                spin1, spin2,
                                _usertype.getSelectedItem().toString(),
                                _gender.getSelectedItem().toString(),
                                _password.getText().toString());

                    }

                    /*Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                    intent.putExtra("mobile", _mno.getText().toString());
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/

                }

            }
        });




    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(false);
    }

    //spinner population for city
    private void fetchJSON(){


        myAPI = retrofit.create(INodeJs.class);

        Call<String> call = myAPI.getJSONString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().toString();
                        spinJSON(jsonresponse);


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    private void spinJSON(String response){

        try {

            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                goodModelArrayList = new ArrayList<>();
                JSONArray dataArray  = obj.getJSONArray("list");

                for (int i = 0; i < dataArray.length(); i++) {

                    spinnerModel = new SpinnerModel();
                    dataobj = dataArray.getJSONObject(i);

                    spinnerModel.setCityid(dataobj.getString("city_id"));
                    spinnerModel.setCity(dataobj.getString("city_name"));

                    goodModelArrayList.add(spinnerModel);

                }

                cities.add("Select City");
                for (int i = 0; i < goodModelArrayList.size(); i++){

                    cities.add(goodModelArrayList.get(i).getCity());

                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Registration.this, R.layout.spinner_item, cities);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner.setAdapter(spinnerArrayAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //spinner population for college
    private void fetchJSON1(){

        myAPI = retrofit.create(INodeJs.class);

        Call<String> call = myAPI.getJSONString1();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse1 = response.body().toString();
                        spinJSON1(jsonresponse1);


                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");
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

                colleges.add("Select College");
                for (int i = 0; i < goodModelArrayList1.size(); i++){
                    colleges.add(goodModelArrayList1.get(i).getCollege());
                }

                ArrayAdapter<String> spinnerArrayAdapter1 = new ArrayAdapter<String>(Registration.this, R.layout.spinner_item, colleges);
                spinnerArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner1.setAdapter(spinnerArrayAdapter1);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void registerUser(final String f_name, final String m_name, final String l_name, final String email, final String mno, final String city, final String college, final String user_type, final String gender, final String password) {


        /*mProgress = new ProgressDialog(Registration.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Creating Account");
        mProgress.show();

*/
        compositeDisposable.add(myAPI.registerUser(f_name, m_name, l_name, email, mno, city, college, user_type, gender, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        JSONObject obj1 = new JSONObject(s);
                        if (obj1.optString("status").equals("true")) {

                            Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                            intent.putExtra("mobile", _mno.getText().toString());
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                            //Toast.makeText(Registration.this, "You Account Is Created" + s, Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            Toast.makeText(Registration.this, "Mobile Number Already Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }


}



package com.example.christuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Session session;
    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText _username, _Password;
    Button _btn_login;
    TextView _register;

    private AwesomeValidation awesomeValidation;
    private ProgressDialog mProgress;

    private static final int REQUEST_SIGNUP = 0;

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
        setContentView(R.layout.activity_main);

        session = new Session(MainActivity.this);

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        _username = (EditText) findViewById(R.id.username);
        _Password = (EditText) findViewById(R.id.Password);

        _btn_login = (Button) findViewById(R.id.btn_login);




        _btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    loginUser(_username.getText().toString(),
                            _Password.getText().toString());


            }
        });


        _register = (TextView) findViewById(R.id.register);

        _register.setOnClickListener(this);

    }

    private void loginUser(final String email, final String Password) {

        //_btn_login.setEnabled(false);

        mProgress = new ProgressDialog(MainActivity.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        //mProgress.setTitle("Processing...");
        mProgress.setMessage("Authenticating");
        //mProgress.setCancelable(false);
        mProgress.show();



        compositeDisposable.add(myAPI.loginUser(email, Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        JSONObject obj1 = new JSONObject(s);
                        if (obj1.optString("status").equals("true")) {

                            //Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            mProgress.dismiss();
                            session.createLoginSession(obj1.optString("user_id"));
                            Intent int1 = new Intent(MainActivity.this, Homepage.class);
                            int1.putExtra("username", _username.getText().toString());
                            startActivity(int1);
                            Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                        }
                        else{
                            mProgress.dismiss();
                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }));

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Registration.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
package com.example.christuniversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    EditText _username, _Password;
    Button _btn_login;
    TextView _register;

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

        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJs.class);

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

        compositeDisposable.add(myAPI.loginUser(email, Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (s.contains("true")) {
                            //Toast.makeText(MainActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

                           Intent int1 = new Intent(MainActivity.this, Homepage.class);
                            //int1.putExtra("Username", user);
                            startActivity(int1);
                            //finish();
                        }
                        else
                            Toast.makeText(MainActivity.this, "not successful"+s, Toast.LENGTH_SHORT).show();
                    }
                }));

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,Registration.class);
        startActivity(intent);
    }
}


       /* Spinner spinner = (Spinner) findViewById(R.id.spinner);

        String[] acc_type = new String[]{
                "Account Type",
                "Faculty",
                "Student",
        };

        // Initializing an ArrayAdapter
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,acc_type
        );
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);*/
   // }


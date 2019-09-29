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
import androidx.appcompat.widget.Toolbar;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;

import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class OtpActivity extends AppCompatActivity {

    TextView _otp;
    Button _submit;
    String mno;
    private INodeJs myAPI;
    private Retrofit retrofit = RetrofitClient.getInstance();
    private ProgressDialog mProgress;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        myAPI = retrofit.create(INodeJs.class);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpActivity.this, Registration.class);
                startActivity(intent);
                finish();
            }
        });

        _otp = (EditText) findViewById(R.id.otp);
        _submit  = (Button) findViewById(R.id.submit);

        mno = getIntent().getExtras().getString("mobile");

        _submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_otp(mno, _otp.getText().toString());

                /*Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
*/
            }
        });
    }

    private void check_otp(final String mobile, final String otp) {


        mProgress = new ProgressDialog(OtpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Creating Account");
        mProgress.show();


        compositeDisposable.add(myAPI.check_otp(mobile, otp)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                        JSONObject obj1 = new JSONObject(s);
                        if (obj1.optString("status").equals("true")) {
                            mProgress.dismiss();

                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                            Toast.makeText(OtpActivity.this, "You Account Is Created", Toast.LENGTH_SHORT).show();
                        }

                        else
                        {
                            mProgress.dismiss();
                            Toast.makeText(OtpActivity.this, "OTP Is Not Correct", Toast.LENGTH_SHORT).show();
                        }

                    }
                }));
    }

}

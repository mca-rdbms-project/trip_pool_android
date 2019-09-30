package com.example.christuniversity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Homepage extends AppCompatActivity implements View.OnClickListener{

    private INodeJs myAPI;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;
    private Toolbar toolbar;
    private CardView _passenger, _driver;
    private NavigationView _nv;
    private Session session;
    private HashMap<String, String> uid;
    private String uid1,username;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Retrofit retrofit = RetrofitClient.getInstance();
    private ProgressDialog mProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        session = new Session(Homepage.this);
        session.checkLogin();
        uid = session.getUserDetails();
        uid1=uid.toString();

        username = getIntent().getExtras().getString("username");

        myAPI = retrofit.create(INodeJs.class);

        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToogle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();

        _passenger=(CardView) findViewById(R.id.passenger);
        _driver=(CardView) findViewById(R.id.driver);

        _passenger.setOnClickListener(this);
        _driver.setOnClickListener(this);

        _nv = (NavigationView)findViewById(R.id.nv);
/*

        TextView _username = (TextView)findViewById(R.id.username);
        _username.setText(username);
*/

        _nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id)
                {
                    case R.id.payment:
                        //Toast.makeText(Homepage.this,PaymentActivity.class);
                        intent = new Intent(Homepage.this, PaymentActivity1.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                    case R.id.booking:
                        intent = new Intent(Homepage.this, BookingActivity.class);
                        senduserid1(uid1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                    case R.id.kyc:
                        intent = new Intent(Homepage.this, RequestrideActivity.class);
                        senduserid(uid1);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                    case R.id.offer:
                        intent = new Intent(Homepage.this, OfferedTripActivity.class);
                        senduserid2(uid1);
                        startActivity(intent);
                        break;
                    case R.id.help:
                        intent = new Intent(Homepage.this, HelpActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                        break;
                    case R.id.logout:
                        session.logoutUser();
                        break;
                    default:
                        return true;
                }


                return true;

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId())
        {
            case R.id.passenger : intent=new Intent(this,PermissionsActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;

            case R.id.driver : intent=new Intent(this,PermissionsActivity1.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                break;
        }
    }

    private void senduserid(final String user_id) {

        mProgress = new ProgressDialog(Homepage.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Searching for Passengers");
        mProgress.show();

        compositeDisposable.add(myAPI.senduserid(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mProgress.dismiss();
                        //Toast.makeText(Homepage.this, "Your Registration is successful" + s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


    private void senduserid1(final String user_id) {

        mProgress = new ProgressDialog(Homepage.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Searching for Booked Trips");
        mProgress.show();

        compositeDisposable.add(myAPI.senduserid1(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mProgress.dismiss();
                        //Toast.makeText(Homepage.this, "Your Registration is successful" + s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    private void senduserid2(final String user_id) {

        mProgress = new ProgressDialog(Homepage.this,
                R.style.AppTheme_Dark_Dialog);
        mProgress.setIndeterminate(true);
        mProgress.setMessage("Searching for Posted Trips");
        mProgress.show();

        compositeDisposable.add(myAPI.senduserid2(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        mProgress.dismiss();
                        //Toast.makeText(Homepage.this, "Your Registration is successful" + s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }


}

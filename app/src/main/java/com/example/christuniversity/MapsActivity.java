package com.example.christuniversity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.example.christuniversity.Modules.DirectionFinderListener;
import com.example.christuniversity.Modules.Route;
import com.example.christuniversity.Retrofit.INodeJs;
import com.example.christuniversity.Retrofit.RetrofitClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.common.collect.Range;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener, AdapterView.OnItemSelectedListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    //private EditText etOrigin;

    private AutocompleteSupportFragment etOrigin, etDestination;
    private EditText etDest;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private View mapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 15;
    //private PlacesClient placesClient;
    private Spinner _vehicle_type;
    private String v_type;
    private CheckBox _rule1,_rule2,_rule3,_rule4,_rule5;
    private EditText _v_model,_v_color,_v_no,_seats;
    private String r1;
    private String r2;
    private String r3;
    private String r4;
    private String r5;
    private String dateString;
    private String timeString;
    private HashMap<String, String> uid;
    private String uid1;
    private String origin1;
    private String destination1;
    TextView _display_date, _display_time;
    private Session session;
    PlacesClient placesClient;

    private AwesomeValidation awesomeValidation;

    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    Retrofit retrofit = RetrofitClient.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Places.initialize(getApplicationContext(), "AIzaSyBSjMmeNnPp00VQhtalS1czrRCYf2ATYLg");

        placesClient = Places.createClient(this);

        session = new Session(MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        uid = session.getUserDetails();
        uid1=uid.toString();

        myAPI = retrofit.create(INodeJs.class);


        Thread thread = new Thread()
        {
            @Override
            public  void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                _display_date = (TextView) findViewById(R.id.tdate);
                                long date = System.currentTimeMillis();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                dateString = sdf.format(date);
                                //_display_date.setText(dateString);
                                //_display_date.setAlpha(0.0f);

                                _display_time = (TextView) findViewById(R.id.ttime);
                                SimpleDateFormat stf = new SimpleDateFormat("hh:mm a");
                                timeString = stf.format(date);
                                //_display_time.setText(timeString);
                                //_display_time.setAlpha(0.0f);


                            }
                        });
                    }
                }
                catch (InterruptedException e){

                }
            }
        };
        thread.start();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        _v_model = (EditText) findViewById(R.id.v_model);
        _v_color = (EditText) findViewById(R.id.v_color);
        _v_no = (EditText) findViewById(R.id.v_no);
        _seats = (EditText) findViewById(R.id.seats);
        _rule1 = (CheckBox) findViewById(R.id.rule1);
        _rule2 = (CheckBox) findViewById(R.id.rule2);
        _rule3 = (CheckBox) findViewById(R.id.rule3);
        _rule4 = (CheckBox) findViewById(R.id.rule4);
        _rule5 = (CheckBox) findViewById(R.id.rule5);
        //LinearLayout ll = (LinearLayout) findViewById(R.id.linearlayout);
        //ll.setAlpha((float) 0.4);


        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        /*etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);
*/
        //origin1=etOrigin.toString();
        //destination1=etDestination.toString();

        //etOrigin = findFragmentById(R.id.etOrigin) as AutocompleteSupportFragment;

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBSjMmeNnPp00VQhtalS1czrRCYf2ATYLg");
        }


        // Initialize the AutocompleteSupportFragment.

        etOrigin = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.etOrigin);


        etOrigin.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));


        etOrigin.setOnPlaceSelectedListener(new com.google.android.libraries.places.widget.listener.PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                origin1=place.getAddress();
                //etDest.setText(origin1);
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        etDestination = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.etDestination);

        etDestination.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS));

        etDestination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destination1=place.getAddress();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        /*etOrigin= (PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.etOrigin);
        etOrigin.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                origin1 = place.getAddress().toString();
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(MapsActivity.this,""+status.toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

        _vehicle_type =(Spinner) findViewById(R.id.vehicle_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _vehicle_type.setAdapter(adapter);
        _vehicle_type.setOnItemSelectedListener(this);



        awesomeValidation.addValidation(this, R.id.v_model, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.modelerror);
        awesomeValidation.addValidation(this, R.id.v_color, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.colorerror);
        awesomeValidation.addValidation(this, R.id.v_no, "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$", R.string.vnoerror);
        awesomeValidation.addValidation(this, R.id.seats, Range.closed(1, 6), R.string.seaterror);



        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //sendRequest();


                if (_rule1.isChecked()) {
                    r1 = _rule1.getText().toString();
                }
                if (_rule2.isChecked()) {
                    r2 = _rule2.getText().toString();
                }
                if (_rule3.isChecked()) {
                    r3 = _rule3.getText().toString();
                }
                if (_rule4.isChecked()) {
                    r4 = _rule4.getText().toString();
                }
                if (_rule5.isChecked()) {
                    r5 = _rule5.getText().toString();
                }

                if (awesomeValidation.validate()) {
                    if (_vehicle_type.getSelectedItem().toString().trim().equals("Vehicle Type"))
                    {
                        Toast.makeText(MapsActivity.this, "Please Select a Vehicle Type", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        driverinfo(dateString, timeString,
                                //etOrigin.,
                                //etDestination.getText().toString(),
                                origin1,destination1,
                                _vehicle_type.getSelectedItem().toString(),
                                _seats.getText().toString(),
                                _v_model.getText().toString(),
                                _v_color.getText().toString(),
                                _v_no.getText().toString(),
                                uid1,r1, r2, r3, r4, r5);
                    }
                }
            }
        });

        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        //Places.initialize(MapsActivity.this, "AIzaSyBSjMmeNnPp00VQhtalS1czrRCYf2ATYLg");
        //placesClient = Places.createClient(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        v_type= parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void driverinfo(final String ddate, final String ttime, final String etOrigin, final String etDestination, final String vehicle_type, final String seats, final String v_model, final String v_color, final String v_no, final String user_id, final String r1, final String r2, final String r3, final String r4, final String r5) {

        compositeDisposable.add(myAPI.driverinfo(ddate, ttime, etOrigin, etDestination, vehicle_type, seats, v_model, v_color, v_no, user_id, r1, r2, r3, r4, r5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(MapsActivity.this, "Your Request is successful"+s, Toast.LENGTH_SHORT).show();

                    }
                }));

    }

    /*private void sendRequest() {

*//*
        if (origin1.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination1.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }
*//*

        try {
            new DirectionFinder(this, origin1, destination1).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
*/
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        /*if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);
        }
*/
        //check if gps is enabled or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MapsActivity.this, 51);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });




        /*mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (etOrigin.isSuggestionsVisible())
                    etOrigin.clearSuggestions();
                if (etOrigin.isSearchEnabled())
                    etOrigin.disableSearch();
                return false;
            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }



    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.destination))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLACK).
                    width(15);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if (locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                            }
                        } else {
                            Toast.makeText(MapsActivity.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
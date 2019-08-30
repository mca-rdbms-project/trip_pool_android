package com.example.christuniversity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
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

import static android.R.layout.simple_spinner_item;


public class Registration extends AppCompatActivity {

    //private static final int IMAGE_CAPTURE_CODE = 1001 ;
    /*Button mCaptureBtn;
    ImageView mImageView;
    *///private static final int PERMISSION_CODE = 1000;
    //Uri image_uri;


    INodeJs myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    ArrayList<SpinnerModel> goodModelArrayList;
    ArrayList<SpinnerModel1> goodModelArrayList1;

    ArrayList<String> cities = new ArrayList<String>();
    Spinner spinner;

    ArrayList<String> colleges = new ArrayList<String>();
    Spinner spinner1, _usertype, _gender;


    Retrofit retrofit = RetrofitClient.getInstance();

    EditText _fname, _mname, _lname, _email, _mno, _idphoto, _password;
    Button _instbtn;

    String spin1,spin2,spin3,spin4;

    private AwesomeValidation awesomeValidation;


    /*private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    Bitmap mBitmap;
*/
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

        //mImageView = findViewById(R.id.photo);
        //mCaptureBtn = findViewById(R.id.capture_image_btn);

        _usertype =(Spinner) findViewById(R.id.usertype);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user, simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _usertype.setAdapter(adapter);

        _gender =(Spinner) findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gender, simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _gender.setAdapter(adapter1);

        //_usertype.setOnItemSelectedListener(this);

        /*askPermissions();
        initRetrofitClient();
*/
       /* mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);


            }
        });
*/


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        _fname = (EditText) findViewById(R.id.f_name);
        _mname = (EditText) findViewById(R.id.m_name);
        _lname = (EditText) findViewById(R.id.l_name);
        _email = (EditText) findViewById(R.id.email);
        _mno = (EditText) findViewById(R.id.mno);
        //_city = (EditText) findViewById(R.id.city);
        //_college = (EditText) findViewById(R.id.college);
        //_idphoto = (EditText) findViewById(R.id.id_photo);
        _usertype = (Spinner) findViewById(R.id.usertype);
        _gender = (Spinner) findViewById(R.id.gender);
        _password = (EditText) findViewById(R.id.password);

        spinner = (Spinner) findViewById(R.id.city);
        fetchJSON();


        /*spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cityid(city_id.getText.toString());
            }
        });*/

        spinner1 = (Spinner) findViewById(R.id.college);
        fetchJSON1();

        _instbtn = (Button) findViewById(R.id.instbtn);

        awesomeValidation.addValidation(this, R.id.f_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        //awesomeValidation.addValidation(this, R.id.f_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.l_name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, Patterns.EMAIL_ADDRESS, R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.mno, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);

        /*awesomeValidation.addValidation(this, R.id.editTextDob, "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.editTextAge, Range.closed(13, 60), R.string.ageerror);
*/



        _instbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mImageView.setEnabled(false);



                if (awesomeValidation.validate()) {
                    if (_usertype.getSelectedItem().toString().trim().equals("User Type")) {
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
                                spinner.getSelectedItem().toString(),
                                spinner1.getSelectedItem().toString(),
                                _usertype.getSelectedItem().toString(),
                                _gender.getSelectedItem().toString(),
                                _password.getText().toString());

                        Toast.makeText(getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                        //finish();

                    }

                }

            }




            //}
        });



    }


   /* private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initRetrofitClient()
    { OkHttpClient client = new OkHttpClient.Builder().build();
      myAPI = new Retrofit.Builder().baseUrl("http://192.168.43.107:3000/").client(client).build().create(INodeJs.class);
        //myAPI = retrofit.create(INodeJs.class);
    }


    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {

            //ImageView imageView = findViewById(R.id.imageView);
            mImageView = findViewById(R.id.photo);

            if (requestCode == IMAGE_RESULT) {


                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    mImageView.setImageBitmap(mBitmap);
                }
            }

        }

    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", image_uri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        image_uri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void multipartImageUpload() {
        try {
            File filesDir = getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            Call<ResponseBody> req = myAPI.postImage(body, name);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.code() == 200) {
                        //textView.setText("Uploaded Successfully!");
                        //textView.setTextColor(Color.BLUE);
                    }

                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //textView.setText("Uploaded Failed!");
                    //textView.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
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

                cities.add("City");
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
    private void fetchJSON1(){

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


    private void registerUser(final String f_name, final String m_name, final String l_name, final String email, final String mno, final String city, final String college, final String user_type, final String gender, final String password) {

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

        compositeDisposable.add(myAPI.registerUser(f_name, m_name, l_name, email, mno, city, college, user_type, gender, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Toast.makeText(Registration.this, "Your Registration is successful" + s, Toast.LENGTH_SHORT).show();

                    }
                }));
    }


    /*@Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String sp1= String.valueOf(spinner.getSelectedItem());
        Toast.makeText(this, sp1, Toast.LENGTH_SHORT).show();
        if(sp1.contentEquals("Bangalore")) {
            List<String> list = new ArrayList<String>();
            list.add("Christ University");
            list.add("Oxford College");
            list.add("Ramaya College");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            spinner1.setAdapter(dataAdapter);
        }

        if(sp1.contentEquals("Pune")) {
            List<String> list = new ArrayList<String>();
            list.add("Christ Lavasa Campus");
            list.add("Symbiosis International University");
            list.add("Bharati Vidyapeeth University");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            spinner1.setAdapter(dataAdapter);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}

//}


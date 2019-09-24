package com.example.christuniversity.Retrofit;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface INodeJs {

    @POST("user-registration")
    @FormUrlEncoded
    Observable<String> registerUser(@Field("f_name") String f_name, @Field("m_name") String m_name, @Field("l_name") String l_name, @Field("email") String email,
                                    @Field("mno") String mno, @Field("city") String city, @Field("college") String college,
                                    @Field("user_type") String user_type, @Field("gender") String gender, @Field("password") String password);

    /*@Multipart
    @POST("upload")
    Call<ResponseBody> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);
*/
    @POST("user-login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("user_name") String user_name, @Field("password") String password);


    @POST("do-offer-trip")
    @FormUrlEncoded
    Observable<String> driverinfo(@Field("date") String date, @Field("time") String time, @Field("origin") String f_location, @Field("destination") String to_location, @Field("vehicle") String v_type, @Field("seats") String seats,
                                  @Field("v_model") String v_model, @Field("v_color") String v_color, @Field("v_no") String v_no, @Field("user") String user_id, @Field("rule1") String rule1,
                                  @Field("rule2") String rule2, @Field("rule3") String rule3, @Field("rule4") String rule4, @Field("rule5") String rule5);

    @POST("find-trip")
    @FormUrlEncoded
    Observable<String> passengerinfo(@Field("date") String date, @Field("time") String time, @Field("f_location") String f_location, @Field("to_location") String to_location);
    //Call<String> getString_listview();

    @POST("request-trip")
    @FormUrlEncoded
    Observable<String> requestinfo(@Field("user_id") String user_id, @Field("trip_id") String trip_id);

    @POST("find-requests")
    @FormUrlEncoded
    Observable<String> senduserid(@Field("user_id") String user_id);


    /*@POST("ride-request")
    @FormUrlEncoded
    Observable<String> riderequestinfo(@Field("user_id") String user_id);
*/

    @GET("cities")
    Call<String> getJSONString();

    /*@GET("get-user-id")
    Call<String> getuserid();
*/
    @GET("colleges")
    Call<String> getJSONString1();

    @GET("list-view-rider")
    Call<String> getString_listview();

    @GET("view-requests")
    Call<String> getriderequest_listview();






    }
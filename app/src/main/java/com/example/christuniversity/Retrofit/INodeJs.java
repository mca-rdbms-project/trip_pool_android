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
                                    @Field("mno") String mno, @Field("city") String city, @Field("college") String college, @Field("id_photo") String id_photo,
                                    @Field("user_type") String user_type, @Field("gender") String gender, @Field("password") String password);


    @POST("user-login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("user_name") String user_name, @Field("password") String password);

    @POST("do-offer-trip")
    @FormUrlEncoded
    Observable<String> driverinfo(@Field("f_location") String f_location, @Field("to_location") String to_location, @Field("v_type") String v_type, @Field("seats") String seats,
                                  @Field("v_model") String v_model, @Field("v_color") String v_color, @Field("v_no") String v_no, @Field("rule1") String rule1,
                                  @Field("rule2") String rule2, @Field("rule3") String rule3, @Field("rule4") String rule4, @Field("rule5") String rule5);


    @GET("cities")
    Call<String> getJSONString();

    /*@POST("CollegesByCity")
    Observable<String> cityid(@Field("city_id") String city_id);
*/

    /*@GET("CollegesByCity")
    Call<String> getJSONString1();
*/


    }
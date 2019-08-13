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

    @GET("cities")
    //@FormUrlEncoded
    Call<String> getJSONString();
}

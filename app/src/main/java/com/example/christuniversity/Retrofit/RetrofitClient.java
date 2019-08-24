package com.example.christuniversity.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit instance;

    public static Retrofit getInstance()
    {
        if(instance==null)
            instance=new Retrofit.Builder()
                    .baseUrl("http://192.168.43.107:3000/") //offline
                    //.baseUrl("http://18.219.51.95:3000/") //server
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        return instance;
    }
}

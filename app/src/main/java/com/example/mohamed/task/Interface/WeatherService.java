package com.example.mohamed.task.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {


    @GET("group")
    Call<ResponseBody> getTemp(@Query("id")String cityIds
            ,@Query("units")String tempType, @Query("appid")String key);


    @GET("forecast")
    Call<ResponseBody> getForecast(@Query("q")String cityName,@Query("units")String tempType,
                                   @Query("appid")String key);


}

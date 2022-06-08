package com.natrocare.naturocare;

import com.natrocare.naturocare.model.currentweather.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrentWeatherApi {
    @GET("weather")
    Call<CurrentWeatherResponse> getCurrentWeather(
            @Query("lat") double latitude,
            @Query("lon") double longitude,
            @Query("appid") String apiKey,
            @Query("units") String unit
    );
}

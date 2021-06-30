package android.com.jumpco.io.androidchallange.android.com.jumpco.io.interfaces;

import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("data/2.5/weather?")
    Call<WeatherResponse> getCurrentWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String app_id);

}

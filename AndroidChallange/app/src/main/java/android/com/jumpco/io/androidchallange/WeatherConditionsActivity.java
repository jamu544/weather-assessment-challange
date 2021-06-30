package android.com.jumpco.io.androidchallange;

import android.app.Activity;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.interfaces.WeatherService;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.WeatherResponse;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherConditionsActivity extends AppCompatActivity {

    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "482cf2ce25f8841f70e5c870e59183a6";
    public String latitude;
    public String longitude;
    public Context context;


    private TextView locationView;
    private TextView weatherDescriptionView;
    private TextView tempView;


    private TextView minTempView;
    private TextView maxTempView;
    private TextView windSpeed;
    private TextView windDegrees;
    private TextView humidity;


    private TextView sunrise;
    private TextView sunset;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_conditions);

        context = this;
        init();
      //  weatherData = (TextView) findViewById(R.id.location);


        // get latitude and longitude of the selected city
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getString( "latitude" );
            longitude = extras.getString( "longitude" );
        }

        //display weather condition results
        getCurrentData();

    }

   // get response from the server
  public void  getCurrentData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(latitude, longitude, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    locationView.setText(weatherResponse.name+","+weatherResponse.sys.country);
                    weatherDescriptionView.setText(weatherResponse.weather.get(0).description+"");
                    tempView.setText("" + Math.round((weatherResponse.main.temp - 275.15))+" \u00B0");

                    minTempView.setText("" + Math.round((weatherResponse.main.temp_min - 275.15))+" \u00B0");
                    maxTempView.setText("" + Math.round((weatherResponse.main.temp_max - 275.15))+" \u00B0");

                    windSpeed.setText(weatherResponse.wind.speed+" km/h ");
                    windDegrees.setText(Math.round((weatherResponse.wind.deg - 275.15))+"\u00B0");

                    humidity.setText(weatherResponse.main.humidity+"%");

                    sunrise.setText(convertMilliSecondsToTime(weatherResponse.sys.sunrise)+" AM");
                    sunset.setText(convertMilliSecondsToTime(weatherResponse.sys.sunset)+"PM");

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
             //   weatherData.setText(t.getMessage());
            }
        });
    }

    public void init(){

          locationView = (TextView) findViewById(R.id.locationTxtView);
          weatherDescriptionView = (TextView) findViewById(R.id.weatherTxtView);
          tempView = (TextView) findViewById(R.id.degressTxtView);


          minTempView = (TextView) findViewById(R.id.tempMin);
          maxTempView = (TextView) findViewById(R.id.tempMax);
          windSpeed = (TextView) findViewById(R.id.windSpeed);
          windDegrees = (TextView) findViewById(R.id.windDeg);

          humidity = (TextView) findViewById(R.id.humidity);
          sunrise = (TextView) findViewById(R.id.sunrise);
          sunset = (TextView) findViewById(R.id.sunset);





    }

    // convert milli seconds to time format
    public static String convertMilliSecondsToTime(long milli){
        DateFormat formatter = new SimpleDateFormat("hh:mm",Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String time = formatter.format(new Date(milli));

        return time;
    }


}

package android.com.jumpco.io.androidchallange;

import android.com.jumpco.io.androidchallange.android.com.jumpco.io.adapters.CityAdapter;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.interfaces.WeatherService;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.City;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.WeatherResponse;
import android.com.jumpco.io.androidchallange.android.com.jumpco.io.utils.Utils;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static MainActivity context;

    ArrayList<City> listOfCities;


    public static String BaseUrl = "http://api.openweathermap.org/";
    public static String AppId = "482cf2ce25f8841f70e5c870e59183a6";
    String latitude, longitude;
    //api.openweathermap.org/data/2.5/find?q=London&units=metric

    public TextView locationTextView;
    public TextView weatherDescription;
    public TextView degressTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfCities = new ArrayList<>();
        context = this;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.weather_app);
        listView = (ListView) findViewById(R.id.listOfCites);

        locationTextView = (TextView) findViewById(R.id.locationTextView);
        weatherDescription = (TextView) findViewById(R.id.weatherDescription);
        degressTextView = (TextView) findViewById(R.id.degressTextView);




        String jsonFileString = Utils.getJsonFromAssets(getApplicationContext(), "city.json");
        listView.setAdapter(new CityAdapter(Utils.getListOfCities(jsonFileString), this));
        System.out.println("weather data ====  " + jsonFileString);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            latitude = extras.getString("latitude" );
            longitude = extras.getString( "longitude" );
        }


        getWeatherConditionsForCurrentLocation();

    }

    // get response from the server
    public void getWeatherConditionsForCurrentLocation(){
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

                    System.out.println("response from the server "+ weatherResponse);

                    locationTextView.setText(weatherResponse.name+","+weatherResponse.sys.country);
                    weatherDescription.setText(weatherResponse.weather.get(0).description+"");
                    degressTextView.setText("" + Math.round((weatherResponse.main.temp - 275.15))+"\u00B0");

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
             //   weatherData.setText(t.getMessage());
            }
        });
    }
}

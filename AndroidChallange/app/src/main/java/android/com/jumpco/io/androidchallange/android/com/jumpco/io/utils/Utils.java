package android.com.jumpco.io.androidchallange.android.com.jumpco.io.utils;

import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.City;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Utils {

    //reading json file from assets folder
    public static String getJsonFromAssets(Context context, String fileName){
        String jsonString = null;

     try {

         InputStream is = context.getAssets().open(fileName);

         int size = is.available();
         byte[] buffer = new byte[size];
         is.read(buffer);
         is.close();

         jsonString = new String(buffer, "UTF-8");

     }catch (IOException io){
         io.getStackTrace();

         return null;
     }

    return jsonString;

    }


    //parse json file and create a list of cities
    public static ArrayList<City> getListOfCities(String jsonFileString){
        ArrayList<City> listOfCities = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonFileString);

            JSONArray jsonArray = jsonObject.getJSONArray( "cities");

            for (int i = 0; i < jsonArray.length(); i++){

                JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                String city = jsonObject2.getString("city");
                String latitude = jsonObject2.getString("lat");
                String longitude = jsonObject2.getString("lon");
                String country = jsonObject2.getString("country");

                listOfCities.add(new City(city,latitude,longitude,country));
            }

            return listOfCities;

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }







}

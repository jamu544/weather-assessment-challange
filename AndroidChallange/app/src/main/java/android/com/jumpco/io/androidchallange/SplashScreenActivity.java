package android.com.jumpco.io.androidchallange;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String locationText = "";
    String locationLatitude = "";
    String locationLongitude = "";

    private int mInterval = 3000; // 3 seconds by default, can be changed later
    private Handler mHandler;
    AlertDialog.Builder dailog;


    //testing git

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



          if(checkInternetConnectivity()) {

              //Alert Dialog
              AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                      SplashScreenActivity.this);

              // Setting Dialog Title
              alertDialog2.setTitle("Notification");

              // Setting Dialog Message
              String string1 = "Give it seconds for your coordinates to update";

              alertDialog2.setMessage(string1);

              // Setting Icon to Dialog
              alertDialog2.setIcon(R.drawable.weather_app);

              // Setting Positive "Yes" Btn
              alertDialog2.setPositiveButton("Continue",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog, int which) {

                          }
                      });

              // Showing Alert Dialog
              alertDialog2.show();

              Handler handler2 = new Handler();
              handler2.postDelayed(new Runnable() {
                  public void run() {
                      mHandler = new Handler();
                      startRepeatingTask();
                  }
              }, 5000);   //5 seconds


              //Permissions for getting Locations etc.

              if (ContextCompat.checkSelfPermission(getApplicationContext(),
                      Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                      ActivityCompat.checkSelfPermission(getApplicationContext(),
                              Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                  ActivityCompat.requestPermissions(this,
                          new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                  Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

              }
          } else {
              showNoInternetDailog();
          }
    }
    // getting location
    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
    void startRepeatingTask() {
        mStatusChecker.run();
    }
    void stopRepeatingTask() {
        if(mHandler !=null)
        mHandler.removeCallbacks(mStatusChecker);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {


            try {
                getLocation(); //this function can change value of mInterval.

                if (locationText.toString() == "") {
                    Toast.makeText(getApplicationContext(), "Trying to retrieve coordinates...", Toast.LENGTH_LONG).show();
                }
                else {

                   Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);

                i.putExtra("latitude", locationLatitude);
                i.putExtra("longitude", locationLongitude);
                startActivity(i);

                // close this activity
                finish();

                    System.out.println("lat == "+locationLatitude);
                    System.out.println("lon == "+locationLongitude);
                }
            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };




     // Called when the location has changed.
    @Override
    public void onLocationChanged(Location location) {

        locationText = location.getLatitude() + "," + location.getLongitude();
        locationLatitude = location.getLatitude() + "";
        locationLongitude = location.getLongitude() + "";

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(SplashScreenActivity.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    // check internet connection for both MOBILE and WIFI
    private boolean checkInternetConnectivity() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    private void showNoInternetDailog(){
        dailog = new AlertDialog.Builder(SplashScreenActivity.this);
        dailog.setMessage("No internet connection!");
        dailog.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        AlertDialog alert = dailog.create();
        alert.show();
    }



}

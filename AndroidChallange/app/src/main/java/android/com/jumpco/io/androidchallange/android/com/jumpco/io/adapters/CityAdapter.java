package android.com.jumpco.io.androidchallange.android.com.jumpco.io.adapters;

import android.com.jumpco.io.androidchallange.android.com.jumpco.io.pojos.City;
import android.com.jumpco.io.androidchallange.MainActivity;
import android.com.jumpco.io.androidchallange.R;
import android.com.jumpco.io.androidchallange.WeatherConditionsActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CityAdapter extends BaseAdapter {

    ArrayList<City> listOFCities;
    Context context;

    public CityAdapter(ArrayList<City> listOFCities, MainActivity context) {
        this.listOFCities = listOFCities;
        this.context = context;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return listOFCities.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    public  class ViewHolder {

    TextView cityTextView;
    TextView latitudeTextView;
    TextView longitudeTextView;
    TextView countryTextView;

    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView==null) {

            LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.row_layout, null);

            holder = new ViewHolder();
            holder.cityTextView = (TextView) convertView.findViewById(R.id.cityTextView);
            holder.latitudeTextView = (TextView) convertView.findViewById(R.id.latTextView);
            holder.longitudeTextView = (TextView) convertView.findViewById(R.id.lonTextView);
            holder.countryTextView = (TextView) convertView.findViewById(R.id.countryTextView);

            convertView.setTag(holder);
        }else {

            holder = (ViewHolder)convertView.getTag();
        }

        City city = listOFCities.get(position);
        holder.cityTextView.setText(city.city);
        holder.latitudeTextView.setText(city.latitude);
        holder.longitudeTextView.setText(city.longitude);
        holder.countryTextView.setText(city.country);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openWeatherConditionsActivity(
                        listOFCities.get(position).latitude,
                        listOFCities.get(position).longitude);
            }
        });
        return convertView;
    }
    //open weatherConditionsActivity
    public void openWeatherConditionsActivity(String lat, String lon) {
        Intent intent = new Intent(context, WeatherConditionsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("latitude", lat);
        bundle.putString("longitude", lon);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}

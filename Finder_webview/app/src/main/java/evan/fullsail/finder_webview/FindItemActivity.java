/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/15/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: FindItemActivity.java
 * Purpose: This activity helps the user to find the item they are searching for. It displays a picture of the location (if there is one), a compass to point them in the right direction
 */

package evan.fullsail.finder_webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class FindItemActivity extends Activity
{
    TextView name;
    TextView locName;
    ImageView image;
    ImageView compass;
    ImageView arrow;
    ImageView light;
    Location target = new Location(LocationManager.GPS_PROVIDER);
    LocationManager locationManager;
    Location current;
    LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            current = location;
            CompareLocations();
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle){}
        @Override
        public void onProviderEnabled(String s){}
        @Override
        public void onProviderDisabled(String s){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //starts location services
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_item);

        name = (TextView)findViewById(R.id.nameFTV);
        locName = (TextView)findViewById(R.id.locNameFTV);
        image = (ImageView)findViewById(R.id.locationImage);
        image.setImageResource(R.drawable.placeholder);
        light = (ImageView)findViewById(R.id.lightIV);
        light.setImageResource(R.drawable.redlight);
        compass = (ImageView)findViewById(R.id.compass);
        compass.setImageResource(R.drawable.compass);
        arrow = (ImageView)findViewById(R.id.arrow);
        arrow.setImageResource(R.drawable.arrow);

        //sets the screens information to the desired item's information
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        locName.setText(intent.getStringExtra("locName"));
        target.setLatitude(intent.getDoubleExtra("latitude", 0));
        target.setLongitude(intent.getDoubleExtra("longitude", 0));
        String imageSource = intent.getStringExtra("imageUri");
        Uri uri = Uri.parse(imageSource);
        if (uri != null)
        {
            image.setImageURI(uri);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.find_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //compares the current location with the desired location and changes the screen images
    private void CompareLocations()
    {
        //gets distance and if close enough it displays a green light instead of a red light image
        float distance = current.distanceTo(target);
        Log.i("FindItemActivity: Distance", String.valueOf(distance));
        if (distance >= 5)
        {
            light.setImageResource(R.drawable.redlight);
        }
        else
        {
            light.setImageResource(R.drawable.bluelight);
        }

        //gets the direction of the item then rotates the image to point in that direction
        float bearing = current.bearingTo(target);
        Log.i("FindItemActivity: Bearing", String.valueOf(bearing));
        Matrix matrix=new Matrix();
        arrow.setScaleType(ImageView.ScaleType.MATRIX);
        matrix.postRotate(bearing, 108f, 108f);
        arrow.setImageMatrix(matrix);
    }
}

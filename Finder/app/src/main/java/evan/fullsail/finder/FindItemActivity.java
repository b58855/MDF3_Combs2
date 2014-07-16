package evan.fullsail.finder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import evan.fullsail.finder.R;

public class FindItemActivity extends Activity
{
    TextView name;
    TextView locName;
    TextView locationTV;
    TextView longitude;
    TextView latitude;
    ImageView image;
    LocationManager locationManager;
    Location location;
    LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            SetLocation(location);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_item);

        name = (TextView)findViewById(R.id.nameFTV);
        locName = (TextView)findViewById(R.id.locNameFTV);
        locationTV = (TextView)findViewById(R.id.locationFTV);
        longitude = (TextView)findViewById(R.id.curLongTV);
        latitude = (TextView)findViewById(R.id.curLatTV);
        image = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        locName.setText(intent.getStringExtra("locName"));
        locationTV.setText("Longitude: " + intent.getDoubleExtra("longitude", 0) + " Latitude: " + intent.getDoubleExtra("latitude", 0));
        String imageSource = intent.getStringExtra("imageUri");
        Uri uri = null;// = Uri.parse(imageSource);
        if (uri != null)
        {
            image.setImageURI(uri);
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
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

    private void SetLocation(Location location)
    {
        this.location = location;
        longitude.setText("Longitude: " + location.getLongitude());
        latitude.setText("Latitude: " + location.getLatitude());
    }
}

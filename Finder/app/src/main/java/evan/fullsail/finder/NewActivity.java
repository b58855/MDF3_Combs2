package evan.fullsail.finder;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

import evan.fullsail.finder.R;

public class NewActivity extends Activity
{
    EditText nameEditText;
    EditText locationNameEditText;
    ImageView imageView;
    Button locationButton;
    Button addImageButton;
    Button addButton;
    Location location = new Location(LocationManager.GPS_PROVIDER);
    String imageSource;
    Random random = new Random();
    LocationManager locationManager;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location)
        {
            Log.i("Latitude", String.valueOf(location.getLatitude()));
            Log.i("Longitude", String.valueOf(location.getLongitude()));
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        nameEditText = (EditText)findViewById(R.id.nameET);
        locationNameEditText = (EditText)findViewById(R.id.locNameET);
        imageView = (ImageView)findViewById(R.id.addImageIV);

        locationButton = (Button)findViewById(R.id.locationButton);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetCurrentLocation();
            }
        });

        addImageButton = (Button)findViewById(R.id.addImageButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TakePicture();
            }
        });

        addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddItem();
            }
        });

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private void TakePicture()
    {

    }

    private void GetCurrentLocation()
    {
        //check to see if location returns null
        //if it does check to see if the phones location provider is on
        //display note saying location services need to be on, or unable to get current location please wait 30 seconds and try again
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.i("NewActivity: GetCurrentLocation: Location", String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
    }

    private void AddItem()
    {
        locationManager.removeUpdates(locationListener);
        long id = random.nextLong();
        String name = nameEditText.getText().toString();
        String locationName = locationNameEditText.getText().toString();

        Item item = new Item(id, name, imageSource, locationName, location);
        DataManager.items.add(item);
        try
        {
            DataManager.SaveItems(this);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

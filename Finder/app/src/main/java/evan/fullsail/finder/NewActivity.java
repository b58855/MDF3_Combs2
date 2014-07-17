/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: NewActivity.java
 * Purpose: Displays a form so the user can add a new item to their list.
 */

package evan.fullsail.finder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class NewActivity extends Activity
{
    EditText nameEditText;
    EditText locationNameEditText;
    TextView locationTextView;
    ImageView imageView;
    Button locationButton;
    Button addImageButton;
    Button addButton;
    String imageSource;
    Random random = new Random();
    Uri uri;
    Location location = new Location(LocationManager.GPS_PROVIDER);
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
        locationTextView = (TextView)findViewById(R.id.locationTV);

        imageView = (ImageView)findViewById(R.id.addImageIV);
        //inserts a placeholder image until an image is taken
        imageView.setImageResource(R.drawable.placeholder);

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

        //starts location updates
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            //displays the image just taken
            imageSource = uri.toString();
            imageView.setImageURI(uri);
        }
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
        //the current time in milliseconds is used to create a unique filename
        Calendar cal = Calendar.getInstance();
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ("finder_" + cal.getTimeInMillis() + ".jpg"));
        Log.i("FilePath", file.getAbsolutePath());
        if (file.exists())
        {
            //deletes the file if one already exists
            file.delete();
        }
        try
        {
            file.createNewFile();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        uri = Uri.fromFile(file);

        //creates and sends the intent to the camera app
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 100);
    }

    private void GetCurrentLocation()
    {
        //check to see if location returns null
        //if it does check to see if the phones location provider is on
        //display note saying location services need to be on, or unable to get current location please wait 30 seconds and try again
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.i("NewActivity: Location", String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
        locationTextView.setText("Longitude: " + String.valueOf(location.getLongitude()) + ", Latitude: " + String.valueOf(location.getLatitude()));
    }

    //adds item to the list and saves it
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
        //returns to the main screen
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

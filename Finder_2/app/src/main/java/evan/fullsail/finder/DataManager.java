/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/15/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: DataManager.java
 * Purpose: Holds the items list. Saves the items to file. Gets the items from file
 */

package evan.fullsail.finder;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager
{
    public static List<Item> items = new ArrayList<Item>();
    private static final String filename = "items";

    //retrieves items from file
    public static void GetItems(Context context) throws IOException, JSONException
    {
        FileInputStream file = null;
        file = context.openFileInput(filename);
        BufferedInputStream buffer = new BufferedInputStream(file);
        StringBuffer stringBuffer = new StringBuffer();
        while (buffer.available() != 0)
        {
            char c = (char)buffer.read();
            stringBuffer.append(c);
        }
        String a = new String(stringBuffer);
        buffer.close();
        file.close();

        items.clear();
        //parses the information from the JSON into instances of Item
        JSONArray array = new JSONArray(a);
        for (int i = 0; i < array.length(); i++)
        {
            JSONObject object = (JSONObject)array.get(i);
            Log.i("DataManager: Object", object.toString());
            long id = object.getLong("id");
            String name = null;
            if(object.has("name"))
            {
                name = object.getString("name");
            }
            String imageSource = null;
            if(object.has("imageSource"))
            {
                imageSource = object.getString("imageSource");
            }
            String locationName = null;
            if(object.has("locationName"))
            {
                locationName = object.getString("locationName");
            }
            Location location = new Location(LocationManager.GPS_PROVIDER);
            if(object.has("longitude") && object.has("latitude"))
            {
                location.setLongitude(object.getDouble("longitude"));
                location.setLatitude(object.getDouble("latitude"));
            }
            Item item = new Item(id, name, imageSource, locationName, location);
            items.add(item);
            Log.i("INFO: DataManager: GetItems", id + " " + name + " " + imageSource + " " + locationName + " " + location.toString());
        }
    }

    //saves items to file
    public static void SaveItems(Context context) throws JSONException, IOException
    {
        //turns the instnaces of Item into a JSON string
        JSONArray array = new JSONArray();
        for (int i = 0; i < items.size(); i++)
        {
            JSONObject object = new JSONObject();
            Item item = items.get(i);
            Log.i("DataManager: SaveItems", item.id + " " + item.name + " " + item.imageSource + " " + item.locationName + " " + item.location);
            object.put("id", item.id);
            object.put("name", item.name);
            object.put("imageSource", item.imageSource);
            object.put("locationName", item.locationName);
            object.put("longitude", item.location.getLongitude());
            object.put("latitude", item.location.getLatitude());
            array.put(object);
        }

        String data = array.toString();
        FileOutputStream fileOutputStream = context.openFileOutput(filename, context.MODE_PRIVATE);
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();
    }
}

package evan.fullsail.wikiview;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Evan on 7/10/2014.
 */
public class DataManagement
{
    private static final String file = "favorites";

    //Saves the current URL as a favorite
    public static void SaveNewFavorite(String url, Context context) throws IOException
    {
        JSONArray data = null;
        try
        {
            //if there is already a file with favorites it retrieves that file
            data = GetFavorites(context);
        }
        catch (IOException e)
        {
            //if no file is found or unable to read file
            e.printStackTrace();
        }
        //if no JSONArray is returned create a new one
        if (data == null)
        {
            data = new JSONArray();
        }
        //adds the URL to the JSONArray
        data.put(url);
        //saves the JSONArray to file
        String json = data.toString();
        Log.i("DataManagement: SaveNewFavorites", json);
        FileOutputStream fileOutputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
        fileOutputStream.write(json.getBytes());
        fileOutputStream.close();
    }

    //Gets the currently saved Favorites list
    public static JSONArray GetFavorites(Context context) throws IOException
    {
        FileInputStream fileInputStream = context.openFileInput(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        StringBuffer stringBuffer = new StringBuffer();
        while (bufferedInputStream.available() != 0)
        {
            char c = (char)bufferedInputStream.read();
            stringBuffer.append(c);
        }
        String a = new String(stringBuffer);
        Log.i("DataManagement: GetFavorites", a);
        bufferedInputStream.close();
        fileInputStream.close();

        //creates the JSON array from the file
        JSONArray data = null;
        try
        {
            data = new JSONArray(a);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return data;
    }
}

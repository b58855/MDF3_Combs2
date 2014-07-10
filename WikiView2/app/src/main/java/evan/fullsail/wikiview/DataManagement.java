package evan.fullsail.wikiview;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Evan on 7/10/2014.
 */
public class DataManagement
{
    private static final String file = "favorites";
    public static void SaveNewFavorite(String url, Context context) throws IOException {
        JSONArray data = new JSONArray();
        try
        {
            data = GetFavorites(context);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        data.put(url);

        String json = data.toString();
        FileOutputStream fileOutputStream = context.openFileOutput(file, Context.MODE_PRIVATE);
        fileOutputStream.write(json.getBytes());
        fileOutputStream.close();
    }

    public static JSONArray GetFavorites(Context context) throws IOException {
        //need context
        FileInputStream fileInputStream = context.openFileInput(file);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        StringBuffer stringBuffer = new StringBuffer();
        while (bufferedInputStream.available() != 0)
        {
            char c = (char)bufferedInputStream.read();
            stringBuffer.append(c);
        }
        String a = new String(stringBuffer);
        bufferedInputStream.close();
        fileInputStream.close();
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

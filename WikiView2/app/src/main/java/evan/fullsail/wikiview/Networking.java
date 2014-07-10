package evan.fullsail.wikiview;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Evan on 7/10/2014.
 */
public class Networking
{
    //checks for a connection to the internet
    public static boolean CheckConnection(Activity activity)
    {
        ConnectivityManager manage = (ConnectivityManager)activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manage.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            Log.i("INFO: Networking: CheckConnection():NOTE", "Internet Access");
            return true;
        }
        Log.i("INFO: Networking: CheckConnection(): NOTE", "No Internet Access");
        return false;
    }

    //Retrieves data from NYTimes then returns it as a string
    public static String RetrieveData(String title) throws IOException
    {
        HttpClient httpClient = new DefaultHttpClient();
        String url = "http://en.wikipedia.org/w/api.php?format=json&action=query&titles=" + title + "&prop=revisions&rvprop=content";
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse;
        try
        {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null)
            {
                InputStream inputStream = httpEntity.getContent();
                Log.i("INFO: Networking: RetrieveData(): Response", inputStream.toString());
                BufferedInputStream bufStream = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[1024];
                int length = 0;
                String content;
                StringBuffer contentBuffer = new StringBuffer();
                while ((length = bufStream.read(buffer)) != -1)
                {
                    content = new String(buffer, 0, length);
                    contentBuffer.append(content);
                }
                content = contentBuffer.toString();
                Log.i("INFO: Networking: RetrieveData(): Response", content);
                bufStream.close();
                return content;
            }
        }
        catch (Exception e)
        {
            Log.i("INFO: Networking: RetrieveData(): Error", e.toString());
        }
        return null;
    }

    //Retrieves data from NYTimes then returns it as a string
    public static String RetrieveData(Uri url) throws IOException
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url.toString());
        HttpResponse httpResponse;
        try
        {
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null)
            {
                InputStream inputStream = httpEntity.getContent();
                Log.i("INFO: Networking: RetrieveData(): Response", inputStream.toString());
                BufferedInputStream bufStream = new BufferedInputStream(inputStream);
                byte[] buffer = new byte[1024];
                int length = 0;
                String content;
                StringBuffer contentBuffer = new StringBuffer();
                while ((length = bufStream.read(buffer)) != -1)
                {
                    content = new String(buffer, 0, length);
                    contentBuffer.append(content);
                }
                content = contentBuffer.toString();
                Log.i("INFO: Networking: RetrieveData(): Response", content);
                bufStream.close();
                return content;
            }
        }
        catch (Exception e)
        {
            Log.i("INFO: Networking: RetrieveData(): Error", e.toString());
        }
        return null;
    }
}

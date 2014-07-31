/**
 * Created by: Evan on 7/22/2014
 * Last Edited: 7/24/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: WidgetService.java
 * Purpose: Main control of the Widget. Updates the widget periodically.
 */

package evan.fullsail.finder_webview.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import evan.fullsail.finder_webview.DataManager;
import evan.fullsail.finder_webview.Item;
import evan.fullsail.finder_webview.MainActivity;
import evan.fullsail.finder_webview.R;

public class WidgetService extends Service
{
    public static boolean service = false;
    int[] widgetIds;
    public static int index = 0;
    public static List<Item> items = new ArrayList<Item>();
    Context context;
    AppWidgetManager appWidgetManager;

    final Handler handler = new Handler();
    Timer timer = new Timer();
    TimerTask update = new TimerTask()
    {
        @Override
        public void run()
        {
            Log.i("WidgetService", "TimerTask");
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.i("WidgetService", "Runnable");
                    for (int widgetId : widgetIds)
                    {
                        UpdateList(context);
                        UpdateTextView(appWidgetManager);
                    }
                }
            });
        }
    };

    LocationManager locationManager;
    LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            for (int i = 0; i < items.size(); i++)
            {
                if (items.get(i).location != null)
                {
                    if (location.distanceTo(items.get(i).location) <= 10)
                    {
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_finder);
                        views.setTextViewText(R.id.widgetTV, items.get(i).name);
                        Uri uri = Uri.parse(items.get(i).imageSource);
                        try
                        {
                            //scales down a bitmap to reduce memory usage
                            Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                            image = Bitmap.createScaledBitmap(image, (int)(image.getWidth() * 0.05), (int)(image.getHeight() * 0.05), false);
                            views.setImageViewBitmap(R.id.imageView, image);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                            views.setImageViewUri(R.id.imageView, null);
                        }
                        Log.i("UpdateTextView", items.get(i).name);
                        appWidgetManager.updateAppWidget(widgetIds, views);
                    }
                }
            }
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle){}
        @Override
        public void onProviderEnabled(String s){}
        @Override
        public void onProviderDisabled(String s){}
    };

    public WidgetService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("WidgetService", "onStartCommand");
        appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        context = this.getApplicationContext();
        UpdateList(context);
        UpdateTextView(appWidgetManager);

        //Sets PendingIntent for Launch Button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_finder);
        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 565428, launchIntent, PendingIntent.FLAG_NO_CREATE);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

        //pending intent for next item button
        Intent nextIntent = new Intent(getBaseContext(), WidgetProvider.class);
        nextIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        int id = 505287;
        nextIntent.putExtra("id", id);

        PendingIntent cPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 505287, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.nextButton, cPendingIntent);

        //pending intent for previous item button
        Intent pevIntent = new Intent(getBaseContext(), WidgetProvider.class);
        pevIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        pevIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        int bId = 16546;
        pevIntent.putExtra("id", bId);

        PendingIntent bPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 16546, pevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.prevButton, bPendingIntent);

        appWidgetManager.updateAppWidget(widgetIds, views);


        //gets preferences and sets update states
        SharedPreferences preferences = context.getSharedPreferences("finder_widget_pref", MODE_PRIVATE);
        int minutes = preferences.getInt("minutes", 60) * 1000;
        timer.schedule(update, minutes);
        //if nearby is set to true start the location requests
        boolean nearby = preferences.getBoolean("nearbyItems", false);
        if (nearby)
        {
            locationManager = (LocationManager)getSystemService(getBaseContext().LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 30, locationListener);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void UpdateList(Context context)
    {
        Log.i("WidgetService", "UpdateList");
        try
        {
            DataManager.GetItems(context);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        for (int i = 0; i < DataManager.items.size(); i++)
        {
            items.add(DataManager.items.get(i));
        }
    }

    private void UpdateTextView(AppWidgetManager appWidgetManager)
    {
        Log.i("WidgetService", "UpdateTextView");
        index++;
        if(index >= items.size())
        {
            index = 0;
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_finder);
        //update the TextView
        if (items.size() > 0)
        {
            views.setTextViewText(R.id.widgetTV, items.get(index).name);
            Uri uri = Uri.parse(items.get(index).imageSource);
            try
            {
                //scales down a bitmap to reduce memory usage
                Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                image = Bitmap.createScaledBitmap(image, (int)(image.getWidth() * 0.05), (int)(image.getHeight() * 0.05), false);
                views.setImageViewBitmap(R.id.imageView, image);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                views.setImageViewUri(R.id.imageView, null);
            }
            Log.i("UpdateTextView", items.get(index).name);
        }
        else
        {
            views.setTextViewText(R.id.widgetTV, "No Items");
            views.setImageViewUri(R.id.imageView, null);
        }
        appWidgetManager.updateAppWidget(widgetIds, views);
    }
}

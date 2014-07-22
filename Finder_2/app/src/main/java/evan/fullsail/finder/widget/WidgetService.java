package evan.fullsail.finder.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import evan.fullsail.finder.DataManager;
import evan.fullsail.finder.Item;
import evan.fullsail.finder.MainActivity;
import evan.fullsail.finder.R;

public class WidgetService extends Service
{
    int[] widgetIds;
    int index = 0;
    public static List<Item> items = new ArrayList<Item>();
    Context context;

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
                        UpdateTextView();
                    }
                }
            });
        }
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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
        context = this.getApplicationContext();
        UpdateList(context);
        UpdateTextView();

        //Sets PendingIntent for Launch Button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_finder);
        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 565428, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);
        appWidgetManager.updateAppWidget(widgetIds, views);


        //gets preferences and sets update states
        SharedPreferences preferences = context.getSharedPreferences("finder_widget_pref", MODE_PRIVATE);
        int minutes = preferences.getInt("minutes", 60) * 1000;
        timer.schedule(update, minutes);
        boolean nearby = preferences.getBoolean("nearbyItems", false);
        if (nearby)
        {
            //start LocationListener
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

    private void UpdateTextView()
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
            Log.i("UpdateTextView", items.get(index).name);
        }
        else
        {
            views.setTextViewText(R.id.widgetTV, "No Items");
        }
    }
}

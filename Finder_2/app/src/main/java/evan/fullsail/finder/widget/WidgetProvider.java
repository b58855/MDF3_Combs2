package evan.fullsail.finder.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import evan.fullsail.finder.DataManager;
import evan.fullsail.finder.Item;
import evan.fullsail.finder.MainActivity;
import evan.fullsail.finder.R;

/**
 * Created by Evan on 7/22/2014.
 */
public class WidgetProvider extends AppWidgetProvider
{
    static int index = 0;
    public static List<Item> items = new ArrayList<Item>();

    @Override
    public void onEnabled(Context context)
    {
        Log.i("WidgetProvider", "onEnabled");
        UpdateList(context);
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.i("WidgetProvider", "onUpdate");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        if(items.size() <= 0)
        {
            UpdateList(context);
        }

        //starts WidgetService
        ComponentName componentName = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        context.startService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("WidgetProvider", "onReceive");
        super.onReceive(context, intent);

        if (intent.getAction() == AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        {
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
            }
            else
            {
                views.setTextViewText(R.id.widgetTV, "");
            }

            //Sets PendingIntent for Launch Button
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 565428, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            appWidgetManager.updateAppWidget(componentName, views);
        }
    }

    private void UpdateList(Context context)
    {
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
}

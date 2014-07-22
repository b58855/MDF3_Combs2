package evan.fullsail.finder.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import evan.fullsail.finder.MainActivity;
import evan.fullsail.finder.R;

/**
 * Created by Evan on 7/22/2014.
 */
public class WidgetProvider extends AppWidgetProvider
{

    @Override
    public void onEnabled(Context context)
    {
        Log.i("WidgetProvider", "onEnabled");
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        Log.i("WidgetProvider", "onUpdate");

        if (!WidgetService.service)
        {
            //starts WidgetService
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
            context.startService(intent);
            WidgetService.service = true;
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("WidgetProvider", "onReceive");
        super.onReceive(context, intent);

        if (intent.getAction() == AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName appWidgetIds = new ComponentName(context, WidgetProvider.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_finder);

            if (intent.getIntExtra("id", 0) == 505287)
            {
                WidgetService.index++;
                if(WidgetService.index >= WidgetService.items.size())
                {
                    WidgetService.index = 0;
                }
            }
            else  if (intent.getIntExtra("id", 0) == 16546)
            {
                WidgetService.index--;
                if(WidgetService.index < 0)
                {
                    WidgetService.index = WidgetService.items.size() - 1;
                }
            }
            if(WidgetService.items.size() > 0)
            {
                views.setTextViewText(R.id.widgetTV, WidgetService.items.get(WidgetService.index).name);
                Uri uri = Uri.parse(WidgetService.items.get(WidgetService.index).imageSource);
                views.setImageViewUri(R.id.imageView, uri);
                Log.i("UpdateTextView", WidgetService.items.get(WidgetService.index).name);
            }
            else
            {
                views.setTextViewText(R.id.widgetTV, "No Items");
                views.setImageViewUri(R.id.imageView, null);
            }
            //Sets PendingIntent for Launch Button
            Intent launchIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 565428, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widgetButton, pendingIntent);

            //pending intent for next item button
            Intent nextIntent = new Intent(context, WidgetProvider.class);
            nextIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            int id = 505287;
            nextIntent.putExtra("id", id);

            PendingIntent cPendingIntent = PendingIntent.getBroadcast(context, 505287, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.nextButton, cPendingIntent);

            //pending intent for previous item button
            Intent pevIntent = new Intent(context, WidgetProvider.class);
            pevIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            pevIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            int bId = 16546;
            pevIntent.putExtra("id", bId);

            PendingIntent bPendingIntent = PendingIntent.getBroadcast(context, 16546, pevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.prevButton, bPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, views);
        }
    }
}

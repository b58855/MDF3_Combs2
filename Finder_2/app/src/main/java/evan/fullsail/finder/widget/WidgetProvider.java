package evan.fullsail.finder.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        //starts WidgetService
        ComponentName componentName = new ComponentName(context, WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        Intent intent = new Intent(context, WidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        context.startService(intent);
    }
}

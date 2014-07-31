/**
 * Created by: Evan on 7/22/2014
 * Last Edited: 7/24/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: WidgetConfigActivity.java
 * Purpose: Called upon the initial creation of the widget. Allows user to set up widget how they want it.
 */

package evan.fullsail.finder_webview.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RemoteViews;

import evan.fullsail.finder_webview.MainActivity;
import evan.fullsail.finder_webview.R;

public class WidgetConfigActivity extends Activity
{
    int appWidgetId = 0;
    CheckBox checkBox;
    EditText editText;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.i("WidgetConfigActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_config);

        checkBox = (CheckBox)findViewById(R.id.widgetCB);
        editText = (EditText)findViewById(R.id.widgetET);
        button = (Button)findViewById(R.id.confirmButton);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //saves the preferences
                SharedPreferences preferences = getSharedPreferences("finder_widget_pref", MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = preferences.edit();
                prefEditor.putBoolean("nearbyItems", checkBox.isChecked());
                prefEditor.putInt("minutes", Integer.getInteger(editText.getText().toString(), 60));
                prefEditor.commit();

                //Creates the widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getBaseContext());
                RemoteViews views = new RemoteViews(getBaseContext().getPackageName(), R.layout.widget_finder);

                //pending intent for the launch button to launch the app
                Intent launchIntent = new Intent(getBaseContext(), MainActivity.class);
                PendingIntent launchPendingIntent = PendingIntent.getActivity(getBaseContext(), 565428, launchIntent, PendingIntent.FLAG_NO_CREATE);
                views.setOnClickPendingIntent(R.id.widgetButton, launchPendingIntent);

                //pending intent for next item button
                Intent nextIntent = new Intent(getBaseContext(), WidgetProvider.class);
                nextIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
                int id = 505287;
                nextIntent.putExtra("id", id);

                PendingIntent cPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 505287, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.nextButton, cPendingIntent);

                //pending intent for previous item button
                Intent pevIntent = new Intent(getBaseContext(), WidgetProvider.class);
                pevIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                pevIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
                int bId = 16546;
                pevIntent.putExtra("id", bId);

                PendingIntent bPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 16546, pevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.prevButton, bPendingIntent);

                appWidgetManager.updateAppWidget(appWidgetId, views);

                //returns the results
                Intent intent = new Intent();
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }
}

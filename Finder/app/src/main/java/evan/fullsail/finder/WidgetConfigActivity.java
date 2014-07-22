package evan.fullsail.finder;

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

import evan.fullsail.finder.MainActivity;
import evan.fullsail.finder.R;

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
                PendingIntent launchPendingIntent = PendingIntent.getActivity(getBaseContext(), 565428, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widgetButton, launchPendingIntent);
                appWidgetManager.updateAppWidget(appWidgetId, views);

               // Intent serviceIntent = new Intent(getBaseContext(), WidgetService.class);
                //startService(serviceIntent);

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

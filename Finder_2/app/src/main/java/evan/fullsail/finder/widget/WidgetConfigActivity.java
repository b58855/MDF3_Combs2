package evan.fullsail.finder.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.util.Map;
import java.util.Set;

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


                //need to set up Provider and Widget before moving on
            }
        });

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
    }
}

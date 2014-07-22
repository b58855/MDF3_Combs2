/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: MainActivity.java
 * Purpose: Displays the list of items the user has. If there is nothing in the list it bypasses this screen, and goes to the add new item screen.
 */

package evan.fullsail.finder;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;


public class MainActivity extends ListActivity
{
    ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        if(DataManager.items.size() <= 0)
        {
            //gets items from file and adds them to a list
            try
            {
                DataManager.GetItems(this);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            //if the list is empty start NewActivity and take the user straight to the add new item screen
            if(DataManager.items.size() <= 0)
            {
                Intent intent = new Intent(this, NewActivity.class);
                startActivity(intent);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new ListAdapter(this, R.layout.list_item, DataManager.items);
        setListAdapter(adapter);

        //gets the preferences and if the service is supposed to be on turns on the service
        final SharedPreferences preferences = getSharedPreferences("finder_pref", MODE_PRIVATE);
        if (preferences != null)
        {
            boolean notify = preferences.getBoolean("notify", false);
            if (notify)
            {
                Intent intent = new Intent(this, NotifyService.class);
                stopService(intent);
                startService(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_new)
        {
            Intent intent = new Intent(this, NewActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.action_settings)
        {
            SettingsDialog settings = new SettingsDialog();
            settings.show(getFragmentManager(), "dialog_settings");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        //displays a dialog to see if the user wants to delete the item or search for it
        super.onListItemClick(l, v, position, id);
        ListItemDialog listItemDialog = new ListItemDialog(DataManager.items.get(position), adapter);
        listItemDialog.show(getFragmentManager(), "dialog_list_item");
    }
}

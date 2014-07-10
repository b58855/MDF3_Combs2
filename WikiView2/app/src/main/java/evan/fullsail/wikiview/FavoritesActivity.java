package evan.fullsail.wikiview;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends ListActivity
{
    FavoritesAdapter adapter;
    List<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        try
        {
            JSONArray favorites = DataManagement.GetFavorites(this);
            for (int i = 0; i < favorites.length(); i++)
            {
                urls.add(favorites.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new FavoritesAdapter(this, R.layout.list_row, urls);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("url", urls.get(position));
        startActivity(intent);
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        finish();
        return false;
    }
}

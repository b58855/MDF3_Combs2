/**
 * Created by: Evan on 7/9/2014
 * Last Edited: 7/10/2014
 * Project: WikiView
 * Package: evan.fullsail.wikiview
 * File: FavoritesAdapter.java
 * Purpose: Adapter that controls what is displayed in a cell of a list view.
 */


package evan.fullsail.wikiview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesAdapter extends ArrayAdapter<String>
{
    List<String> objects = new ArrayList<String>();
    Context context;
    int resource;

    //constructor
    public FavoritesAdapter(Context context, int resource, List<String> objects)
    {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

    //creates the cell
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        Holder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.url = (TextView)row.findViewById(R.id.url);

            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        String object = objects.get(position);
        //removes the beginning of the url leaving only the title to be used as the name
        String name = object.replace("http://en.m.wikipedia.org/wiki/", "");
        holder.name.setText(name);
        holder.url.setText(object);

        return row;
    }

    private static class Holder
    {
        TextView name;
        TextView url;
    }
}

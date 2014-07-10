package evan.fullsail.wikiview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Evan on 7/10/2014.
 */
public class FavoritesAdapter extends ArrayAdapter<String>
{
    String objects[];
    Context context;
    int resource;

    public FavoritesAdapter(Context context, int resource, String[] objects)
    {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
        this.resource = resource;
    }

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

        String object = objects[position];
        holder.name.setText(object);
        holder.url.setText(object);

        return row;
    }

    private static class Holder
    {
        TextView name;
        TextView url;
    }
}

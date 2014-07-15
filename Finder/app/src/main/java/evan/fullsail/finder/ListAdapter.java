package evan.fullsail.finder;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 7/15/2014.
 */
public class ListAdapter extends ArrayAdapter<Item>
{
    List<Item> objects = new ArrayList<Item>();
    int layoutResourceID;
    Context context;

    public ListAdapter(Context context, int layoutResourceID, List<Item> objects)
    {
        super(context, layoutResourceID, objects);
        this.objects = objects;
        this.layoutResourceID = layoutResourceID;
        this.context = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View row = view;
        Holder holder = null;

        if (row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceID, viewGroup, false);

            holder = new Holder();
            //set holder bindings here
            holder.imageView = (ImageView)row.findViewById(R.id.itemIV);
            holder.textView = (TextView)row.findViewById(R.id.itemTV);

            row.setTag(holder);
        }
        else
        {
            holder = (Holder)row.getTag();
        }

        Uri uri = Uri.parse(objects.get(i).imageSource);
        holder.imageView.setImageURI(uri);
        holder.textView.setText(objects.get(i).name);

        return row;
    }

    private class Holder
    {
        ImageView imageView;
        TextView textView;
    }
}

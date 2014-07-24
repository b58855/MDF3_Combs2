/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: ListAdapter.java
 * Purpose: Adapter that controls what is displayed in the MainActivity list
 */

package evan.fullsail.finder;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        Uri uri = null;
        //if no picture it defualts to the placeholder image
        holder.imageView.setImageResource(R.drawable.placeholder);
        if (objects.get(i).imageSource != null) {
            uri = Uri.parse(objects.get(i).imageSource);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 6;
        try {
            //scales down a bitmap to reduce memory usage
            Bitmap image = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            image = Bitmap.createScaledBitmap(image, (int)(image.getWidth() * 0.05), (int)(image.getHeight() * 0.05), false);
            holder.imageView.setImageBitmap(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Bitmap image = BitmapFactory.decodeFile(uri.toString(), options);
        //holder.imageView.setImageURI(uri);
        String name = "Untitled";
        if (objects.get(i).name != null)
        {
            name = objects.get(i).name;
        }
        holder.textView.setText(name);

        return row;
    }

    private class Holder
    {
        ImageView imageView;
        TextView textView;
    }
}

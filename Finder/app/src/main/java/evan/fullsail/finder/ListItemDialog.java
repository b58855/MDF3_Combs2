/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: ListItemDialog.java
 * Purpose: Dialog asking if the user wants to delete the item, or search for it.
 */

package evan.fullsail.finder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;

public class ListItemDialog extends DialogFragment
{
    Item item;
    ListAdapter adapter;

    public ListItemDialog(Item item, ListAdapter adapter)
    {
        super();
        this.item = item;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        //creates and displays the options
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //sends the user to the find item screen
                Intent intent = new Intent(getActivity(), FindItemActivity.class);
                intent.putExtra("name", item.name);
                intent.putExtra("locName", item.locationName);
                intent.putExtra("imageUri", item.imageSource);
                startActivity(intent);
                dismiss();
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dismiss();
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //deletes item from list
                DataManager.items.remove(0);
                try
                {
                    DataManager.SaveItems(getActivity());
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void dismiss()
    {
        //updates the list in case an item was deleted
        super.dismiss();
        adapter.notifyDataSetChanged();
    }
}

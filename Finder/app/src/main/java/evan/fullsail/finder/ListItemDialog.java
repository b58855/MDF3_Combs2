package evan.fullsail.finder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Evan on 7/15/2014.
 */
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_list_item, null);
        builder.setView(view);
        builder.setPositiveButton("Search for Item", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                Intent intent = new Intent(getActivity(), FindItemActivity.class);
                intent.putExtra("name", item.name);
                intent.putExtra("locName", item.locationName);
                intent.putExtra("longitude", item.location.getLongitude());
                intent.putExtra("latitude", item.location.getLatitude());
                intent.putExtra("imageUri", item.imageSource);
                startActivity(intent);
                dismiss();
            }
        });
        builder.setNegativeButton("Delete Item", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                DataManager.items.remove(0);
                dismiss();
            }
        });
        return builder.create();
    }

    @Override
    public void dismiss()
    {
        super.dismiss();
        adapter.notifyDataSetChanged();
    }
}

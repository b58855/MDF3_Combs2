package evan.fullsail.finder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Evan on 7/15/2014.
 */
public class ListAdapter extends ArrayAdapter<Item>
{
    public ListAdapter(Context context, int resource, List<Item> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        return null;
    }

    private class Holder
    {

    }
}

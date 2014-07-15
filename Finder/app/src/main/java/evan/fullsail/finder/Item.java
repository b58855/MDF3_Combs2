package evan.fullsail.finder;

import android.location.Location;

/**
 * Created by Evan on 7/15/2014.
 */
public class Item
{
    long id;
    String name;
    String imageSource;
    String locationName;
    Location location;

    public Item (long id, String name, String imageSource, String locationName, Location location)
    {
        this.id = id;
        this.name = name;
        this.imageSource = imageSource;
        this.locationName = locationName;
        this.location = location;
    }
}

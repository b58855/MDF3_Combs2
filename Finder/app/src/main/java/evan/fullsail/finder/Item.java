/**
 * Created by: Evan on 7/15/2014
 * Last Edited: 7/15/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: Item.java
 * Purpose: Holds the information for each item
 */

package evan.fullsail.finder;

import android.location.Location;

public class Item
{
    long id;
    public String name;
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

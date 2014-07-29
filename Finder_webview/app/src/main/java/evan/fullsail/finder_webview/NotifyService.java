/**
 * Created by: Evan on 7/17/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: NotifyService.java
 * Purpose: Service that recieves location updates. If the location is near to an item, it sends a notification letting the user know.
 */

package evan.fullsail.finder_webview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class NotifyService extends Service
{
    LocationManager locationManager;
    Location current;
    LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            current = location;
            for (int i = 0; i < DataManager.items.size(); i++)
            {
                Location target = DataManager.items.get(i).location;
                float distance = current.distanceTo(target);
                Log.i("NotifyService: Distance", String.valueOf(distance));
                if(distance <= 10)
                {
                    //builds notification
                    Notification.Builder builder = new Notification.Builder(getBaseContext());
                    builder.setSmallIcon(R.drawable.ic_launcher);
                    builder.setContentTitle("Finder");
                    builder.setContentText("You are near: " + DataManager.items.get(i).name);
                    builder.setAutoCancel(true);

                    //intent to open the app
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);

                    //send notification
                    NotificationManager notificationManager = (NotificationManager)getSystemService(getBaseContext().NOTIFICATION_SERVICE);
                    notificationManager.notify(46, builder.build());
                }
            }
        }
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle){}
        @Override
        public void onProviderEnabled(String s){}
        @Override
        public void onProviderDisabled(String s){}
    };

    public NotifyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.i("NotifyService", "Service Started");
        locationManager = (LocationManager)getSystemService(getBaseContext().LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

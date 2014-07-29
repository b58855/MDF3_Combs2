/**
 * Created by: Evan on 7/17/2014
 * Last Edited: 7/17/2014
 * Project: Finder
 * Package: evan.fullsail.finder
 * File: SettingsDialog.java
 * Purpose: Allows the user to turn on and off the Notification Service
 */

package evan.fullsail.finder_webview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class SettingsDialog extends DialogFragment
{
    CheckBox checkBox;

    public SettingsDialog()
    {
        super();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_settings, null);
        checkBox = (CheckBox)view.findViewById(R.id.checkBox);
        //gets the saved setting
        final SharedPreferences preferences = getActivity().getSharedPreferences("finder_pref", getActivity().MODE_PRIVATE);
        if (preferences != null)
        {
            boolean notify = preferences.getBoolean("notify", false);
            checkBox.setChecked(notify);
        }
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                SharedPreferences.Editor prefEditor = preferences.edit();
                Intent intent = new Intent(getActivity(), NotifyService.class);
                if (checkBox.isChecked())
                {
                    Log.i("SettingsDialog", "Turning Service is On");
                    prefEditor.putBoolean("notify", true);
                    //turn on service
                    getActivity().startService(intent);
                }
                else
                {
                    Log.i("SettingsDialog", "Turning Service is Off");
                    prefEditor.putBoolean("notify", false);
                    //turn off service
                    getActivity().stopService(intent);
                }
                prefEditor.commit();
                dismiss();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dismiss();
            }
        });
        return builder.create();
    }
}

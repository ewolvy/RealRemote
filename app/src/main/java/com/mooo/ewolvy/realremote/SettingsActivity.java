package com.mooo.ewolvy.realremote;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;

public class SettingsActivity extends AppCompatActivity {
    static final int PERMISSION_SHOW_FILE_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_SHOW_FILE_DIALOG: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FileChooserDialog dialog = new FileChooserDialog(this);
                    dialog.loadFolder(Environment.getExternalStorageDirectory().toString());
                    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                        public void onFileSelected(Dialog source, File file) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(),
                                    "File selected: " + file.getName(),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        public void onFileSelected(Dialog source, File folder, String name) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(),
                                    "File created: " + folder.getName() + "/" + name,
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    dialog.show();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static class RealRemotePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference certificatePreference = findPreference(getString(R.string.settings_certificate_key));
            certificatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    FileChooserDialog dialog = new FileChooserDialog(getActivity());
                    dialog.loadFolder(Environment.getExternalStorageDirectory().toString());
                    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                        public void onFileSelected(Dialog source, File file) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(),
                                    "File selected: " + file.getName(),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        public void onFileSelected(Dialog source, File folder, String name) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(),
                                    "File created: " + folder.getName() + "/" + name,
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });

                    // Check if have permission to read files.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED){
                        // We don't have permission. Ask for it.
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSION_SHOW_FILE_DIALOG);
                    }else {
                        // We have permission. Show dialog to choose file.
                        dialog.show();
                    }
                    return false;
                }
            });
        }
    }
}

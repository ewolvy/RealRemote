package com.mooo.ewolvy.realremote;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;

import ar.com.daidalos.afiledialog.FileChooserDialog;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class RealRemotePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            Preference certificatePreference = (Preference) findPreference(getString(R.string.settings_certificate_key));
            certificatePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    FileChooserDialog dialog = new FileChooserDialog(getActivity());
                    dialog.loadFolder(Environment.getExternalStorageDirectory() + "/Download/");
                    dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {
                        public void onFileSelected(Dialog source, File file) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(), "File selected: " + file.getName(), Toast.LENGTH_LONG);
                            toast.show();
                        }
                        public void onFileSelected(Dialog source, File folder, String name) {
                            source.hide();
                            Toast toast = Toast.makeText(source.getContext(), "File created: " + folder.getName() + "/" + name, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
                    dialog.show();
                    return false;
                }
            });
        }
    }
}

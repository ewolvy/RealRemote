package com.mooo.ewolvy.realremote.aalist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.io.File;
import java.util.Objects;

import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AAEditItemActivity extends AppCompatActivity {

    static final int PERMISSION_SHOW_FILE_DIALOG = 1;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String POSITION = "POSITION";
    private boolean mAAHasChanged = false;
    private boolean mNewAA;
    private int mModifiedItem;

    EditText nameEdit, serverEdit, portEdit, usernameEdit, passwordEdit, certificateEdit, aliasEdit;
    Spinner brandSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_edit_item);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        nameEdit = (EditText) findViewById(R.id.edit_name);
        brandSpinner = (Spinner) findViewById(R.id.edit_brand);
        serverEdit = (EditText) findViewById(R.id.edit_server);
        portEdit = (EditText) findViewById(R.id.edit_port);
        usernameEdit = (EditText) findViewById(R.id.edit_username);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        certificateEdit = (EditText) findViewById(R.id.edit_certificate);
        aliasEdit = (EditText) findViewById(R.id.edit_alias);

        if (!Objects.equals(extras.getString(AvailableAA.COLUMN_NAME_NAME), "")) {
            nameEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_NAME));
            brandSpinner.setSelection(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
            serverEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
            portEdit.setText(extras.getInt(AvailableAA.COLUMN_NAME_PORT));
            usernameEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
            passwordEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
            certificateEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE));

            aliasEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));

            mModifiedItem = extras.getInt(POSITION);
            mNewAA = false;
        } else {
            mNewAA = true;
        }

        nameEdit.setOnTouchListener(mTouchListener);
        brandSpinner.setOnTouchListener(mTouchListener);
        serverEdit.setOnTouchListener(mTouchListener);
        portEdit.setOnTouchListener(mTouchListener);
        usernameEdit.setOnTouchListener(mTouchListener);
        passwordEdit.setOnTouchListener(mTouchListener);
        certificateEdit.setOnTouchListener(mTouchListener);
        aliasEdit.setOnTouchListener(mTouchListener);
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
                    showFileDialog(this);
                } else {
                    Toast toast = Toast.makeText(this, R.string.no_file_permission, Toast.LENGTH_LONG);
                    toast.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
            default:{
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.edit_save:
                saveAA();
                finish();
                return true;
            case android.R.id.home:
                if (!mAAHasChanged) {
                    NavUtils.navigateUpFromSameTask(AAEditItemActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AAEditItemActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mAAHasChanged = true;
            if (view.getId() == R.id.edit_certificate){
                showFileDialog(view.getContext());
                return true;
            } else {
                return false;
            }
        }
    };

    @Override
    public void onBackPressed(){
        if (!mAAHasChanged){
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
        DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the AA.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void saveAA(){
        AAItem newItem = new AAItem();

        newItem.setName(nameEdit.getText().toString());
        newItem.setBrand(brandSpinner.getSelectedItemPosition());
        newItem.setServer(serverEdit.getText().toString());
        newItem.setPort(Integer.parseInt(portEdit.getText().toString()));
        newItem.setUsername(usernameEdit.getText().toString());
        newItem.setPassword(passwordEdit.getText().toString());
        newItem.setCertificate(certificateEdit.getText().toString());
        newItem.setAlias(aliasEdit.getText().toString());

        if (mNewAA){
            newItem.setPosition(AAListActivity.listData.size() + 1);
            AAListActivity.listData.add(newItem);
        } else {
            AAItem oldItem = (AAItem) AAListActivity.listData.get(mModifiedItem);
            newItem.setPosition(oldItem.getPosition());
            AAListActivity.listData.set(mModifiedItem, newItem);
        }
    }

    public void showFileDialog (final Context context){
        String fullPath, path;
        FileChooserDialog dialog = new FileChooserDialog(context);
        fullPath = certificateEdit.getText().toString();
        if (fullPath.equals(Environment.getExternalStorageState())){
            path = fullPath;
        }else{
            File file = new File (fullPath);
            path = file.getParent();
        }
        dialog.loadFolder(path);
        dialog.addListener(new FileChooserDialog.OnFileSelectedListener() {

            public void onFileSelected(Dialog source, File file) {
                source.hide();
                Toast toast = Toast.makeText(source.getContext(),
                        source.getContext().getString(R.string.settings_selected_file) + file.getAbsoluteFile(),
                        Toast.LENGTH_LONG);
                toast.show();
                certificateEdit.setText(file.getAbsolutePath());
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
    }
}
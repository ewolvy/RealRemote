package com.mooo.ewolvy.realremote.aalist;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.io.File;
import java.util.Objects;

import ar.com.daidalos.afiledialog.FileChooserDialog;

public class AAEditItemActivity extends AppCompatActivity implements View.OnClickListener {

    static final int PERMISSION_SHOW_FILE_DIALOG = 1;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    private String mCertificateFile = "";
    private boolean mAAHasChanged = false;
    private boolean mSavePressed = false, mNewItem = true;
    private int mItemPosition, mItemID;

    EditText nameEdit, serverEdit, portEdit, usernameEdit, passwordEdit, aliasEdit;
    Spinner brandSpinner;
    Button certificateButton;
    TextView certificateText;

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
        certificateButton = (Button) findViewById(R.id.edit_certificate_button);
        certificateText = (TextView) findViewById(R.id.edit_certificate_text);
        aliasEdit = (EditText) findViewById(R.id.edit_alias);

        if (!Objects.equals(extras.getString(AvailableAA.COLUMN_NAME_NAME), "")) {
            nameEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_NAME));
            brandSpinner.setSelection(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
            serverEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
            portEdit.setText(String.valueOf(extras.getInt(AvailableAA.COLUMN_NAME_PORT)));
            usernameEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
            passwordEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
            mCertificateFile = extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE);
            certificateText.setText(mCertificateFile);
            aliasEdit.setText(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));
            mItemID = extras.getInt(AvailableAA._ID);
            mNewItem = false;

            mItemPosition = extras.getInt(AvailableAA.COLUMN_NAME_POSITION);
        }

        nameEdit.setOnTouchListener(mTouchListener);
        brandSpinner.setOnTouchListener(mTouchListener);
        serverEdit.setOnTouchListener(mTouchListener);
        portEdit.setOnTouchListener(mTouchListener);
        usernameEdit.setOnTouchListener(mTouchListener);
        passwordEdit.setOnTouchListener(mTouchListener);
        aliasEdit.setOnTouchListener(mTouchListener);

        certificateButton.setOnClickListener(this);
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
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast toast = Toast.makeText(this, R.string.no_file_permission, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
            default: {
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
                mSavePressed = true;
                finish();
                return true;
            case android.R.id.home:
                // If there are no changes
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
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.edit_certificate_button) {
            // Check if have permission to read files.
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                // We don't have permission. Ask for it.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_SHOW_FILE_DIALOG);
            } else {
                // We have permission. Show dialog to choose file.
                showFileDialog(this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!mAAHasChanged) {
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

    @Override
    public void finish() {
        if (mSavePressed && mAAHasChanged) {
            if (checkData()) {
                Bundle data = new Bundle();
                Intent intent = new Intent();
                data.putString(AvailableAA.COLUMN_NAME_NAME, nameEdit.getText().toString());
                data.putInt(AvailableAA.COLUMN_NAME_BRAND, brandSpinner.getSelectedItemPosition());
                data.putString(AvailableAA.COLUMN_NAME_SERVER, serverEdit.getText().toString());
                data.putInt(AvailableAA.COLUMN_NAME_PORT, Integer.parseInt(portEdit.getText().toString()));
                data.putString(AvailableAA.COLUMN_NAME_USERNAME, usernameEdit.getText().toString());
                data.putString(AvailableAA.COLUMN_NAME_PASSWORD, passwordEdit.getText().toString());
                data.putString(AvailableAA.COLUMN_NAME_CERTIFICATE, mCertificateFile);
                data.putString(AvailableAA.COLUMN_NAME_ALIAS, aliasEdit.getText().toString());
                data.putInt(AvailableAA.COLUMN_NAME_POSITION, mItemPosition);
                if (!mNewItem){ data.putInt(AvailableAA._ID, mItemID); }
                // Activity finished ok, return the data
                intent.putExtra(BUNDLE_EXTRAS, data);
                setResult(RESULT_OK, intent);
            } else {
                // Missing data, show message and continue editing
                new AlertDialog.Builder(AAEditItemActivity.this)
                        .setTitle(getString(R.string.edit_incomplete_title))
                        .setMessage(getString(R.string.edit_incomplete_message))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                            }
                        }).show();
                mSavePressed = false;
                return;
            }
        } else {
            setResult(RESULT_CANCELED);
        }
        super.finish();
    }

    private boolean checkData () {
        // If any field is empty return false, else return true
        return !nameEdit.getText().toString().equals("") &&
                !serverEdit.getText().toString().equals("") &&
                !portEdit.getText().toString().equals("") &&
                !usernameEdit.getText().toString().equals("") &&
                !passwordEdit.getText().toString().equals("") &&
                !mCertificateFile.equals("") &&
                !aliasEdit.getText().toString().equals("");
    }

    public void showFileDialog(final Context context) {
        String fullPath, path;
        FileChooserDialog dialog = new FileChooserDialog(context);
        if (mCertificateFile == null) {
            fullPath = "";
        } else {
            fullPath = mCertificateFile;
        }
        if (fullPath.equals(Environment.getExternalStorageState())) {
            path = fullPath;
        } else {
            File file = new File(fullPath);
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
                mCertificateFile = file.getAbsolutePath();
                certificateText.setText(mCertificateFile);
                mAAHasChanged = true;
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
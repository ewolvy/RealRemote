package com.mooo.ewolvy.realremote.aalist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Spinner;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

public class AAEditItemActivity extends AppCompatActivity {

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_edit_item);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);

        ((EditText) findViewById(R.id.edit_name)).setText(extras.getString(AvailableAA.COLUMN_NAME_NAME));
        ((Spinner) findViewById(R.id.edit_brand)).setSelection(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
        ((EditText) findViewById(R.id.edit_server)).setText(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
        ((EditText) findViewById(R.id.edit_port)).setText(extras.getInt(AvailableAA.COLUMN_NAME_PORT));
        ((EditText) findViewById(R.id.edit_username)).setText(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
        ((EditText) findViewById(R.id.edit_password)).setText(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
        ((EditText) findViewById(R.id.edit_certificate)).setText(extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE));
        ((EditText) findViewById(R.id.edit_alias)).setText(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));
    }
}

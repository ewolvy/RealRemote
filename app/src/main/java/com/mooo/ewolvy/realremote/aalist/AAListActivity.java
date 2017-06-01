package com.mooo.ewolvy.realremote.aalist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.util.ArrayList;

public class AAListActivity extends AppCompatActivity implements AAAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String POSITION = "POSITION";
    private RecyclerView recView;
    private AAAdapter adapter;

    public static ArrayList listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_list);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AAListActivity.this, AAEditItemActivity.class);
                Bundle extras = new Bundle();
                extras.putString(AvailableAA.COLUMN_NAME_NAME, "");
                extras.putInt(AvailableAA.COLUMN_NAME_BRAND, 0);
                extras.putString(AvailableAA.COLUMN_NAME_SERVER, "");
                extras.putInt(AvailableAA.COLUMN_NAME_PORT, 0);
                extras.putString(AvailableAA.COLUMN_NAME_USERNAME, "");
                extras.putString(AvailableAA.COLUMN_NAME_PASSWORD, "");
                extras.putString(AvailableAA.COLUMN_NAME_CERTIFICATE, "");
                extras.putString(AvailableAA.COLUMN_NAME_ALIAS, "");
                extras.putInt(POSITION, 0);

                intent.putExtra(BUNDLE_EXTRAS, extras);

                startActivity(intent);
            }
        });

        listData = (ArrayList) AAData.getListData(this);

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AAAdapter(AAData.getListData(this), this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
    }

    @Override
    public void onItemClick(int p) {
        AAItem item = (AAItem) listData.get(p);

        Intent intent = new Intent(this, AAEditItemActivity.class);

        Bundle extras = new Bundle();
        extras.putString(AvailableAA.COLUMN_NAME_NAME, item.getName());
        extras.putInt(AvailableAA.COLUMN_NAME_BRAND, item.getBrand());
        extras.putString(AvailableAA.COLUMN_NAME_SERVER, item.getServer());
        extras.putInt(AvailableAA.COLUMN_NAME_PORT, item.getPort());
        extras.putString(AvailableAA.COLUMN_NAME_USERNAME, item.getUsername());
        extras.putString(AvailableAA.COLUMN_NAME_PASSWORD, item.getPassword());
        extras.putString(AvailableAA.COLUMN_NAME_CERTIFICATE, item.getCertificate());
        extras.putString(AvailableAA.COLUMN_NAME_ALIAS, item.getAlias());

        extras.putInt(POSITION, p);
        intent.putExtra(BUNDLE_EXTRAS, extras);
        startActivity(intent);
    }
}

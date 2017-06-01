package com.mooo.ewolvy.realremote.aalist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.util.ArrayList;

public class AAListActivity extends AppCompatActivity implements AAAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private RecyclerView recView;
    private AAAdapter adapter;

    private static ArrayList listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_list);

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

        intent.putExtra(BUNDLE_EXTRAS, extras);
        startActivity(intent);
    }
}

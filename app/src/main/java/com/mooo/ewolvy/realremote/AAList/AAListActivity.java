package com.mooo.ewolvy.realremote.AAList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mooo.ewolvy.realremote.R;

public class AAListActivity extends AppCompatActivity {

    private RecyclerView recView;
    private AAAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aa_list);

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AAAdapter(AAData.getListData(this), this);
        recView.setAdapter(adapter);
    }
}

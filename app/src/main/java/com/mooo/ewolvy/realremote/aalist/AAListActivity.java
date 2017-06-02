package com.mooo.ewolvy.realremote.aalist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.View;

import com.mooo.ewolvy.realremote.R;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;

import java.util.ArrayList;

public class AAListActivity extends AppCompatActivity implements AAAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String POSITION = "POSITION";
    private static final int REQUEST_CODE_MODIFY = 1;
    private static final int REQUEST_CODE_NEW = 2;
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

                startActivityForResult(intent, REQUEST_CODE_NEW);
            }
        });

        listData = (ArrayList) AAData.getListData(this);

        recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AAAdapter(AAData.getListData(this), this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    @Override
    public void onItemClick(View v, int p) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade(Fade.IN));
            getWindow().setExitTransition(new Fade(Fade.OUT));
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    new Pair<>(v.findViewById(R.id.aa_list_name),
                            getString(R.string.transition_name)));

            ActivityCompat.startActivityForResult(this, intent, REQUEST_CODE_MODIFY, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_CODE_MODIFY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_MODIFY && resultCode == RESULT_OK){
            AAItem item = new AAItem();
            Bundle extras = data.getBundleExtra(BUNDLE_EXTRAS);
            item.setName(extras.getString(AvailableAA.COLUMN_NAME_NAME));
            item.setBrand(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
            item.setServer(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
            item.setPort(extras.getInt(AvailableAA.COLUMN_NAME_PORT));
            item.setUsername(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
            item.setPassword(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
            item.setCertificate(extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE));
            item.setAlias(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));
            item.setPosition(extras.getInt(POSITION));

            listData.set(extras.getInt(POSITION), item);
        } else if (requestCode == REQUEST_CODE_NEW && resultCode == RESULT_OK){
            AAItem newItem = new AAItem();
            Bundle extras = data.getBundleExtra(BUNDLE_EXTRAS);
            newItem.setName(extras.getString(AvailableAA.COLUMN_NAME_NAME));
            newItem.setBrand(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
            newItem.setServer(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
            newItem.setPort(extras.getInt(AvailableAA.COLUMN_NAME_PORT));
            newItem.setUsername(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
            newItem.setPassword(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
            newItem.setCertificate(extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE));
            newItem.setAlias(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));

            newItem.setPosition(listData.size() + 1);
            listData.add(newItem);
        }
        if (resultCode == RESULT_OK) {
            AAData.saveListData(listData, this);
            adapter.setListData(listData);
        }
    }

    private ItemTouchHelper.Callback createHelperCallback(){
        return(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                });
    }

    private void moveItem (int oldPos, int newPos){
        AAItem itemMoved = (AAItem) listData.get(oldPos);
        AAItem itemSwitched = (AAItem) listData.get(newPos);
        itemMoved.setPosition(newPos);
        itemSwitched.setPosition(oldPos);
        listData.remove(oldPos);
        listData.add(newPos, itemMoved);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem (final int pos){
        listData.remove(pos);
        adapter.setListData(listData);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AAData.saveListData(listData, this);
    }
}

package com.mooo.ewolvy.realremote.aalist;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.mooo.ewolvy.realremote.aaremotes.AASuper;
import com.mooo.ewolvy.realremote.database.AirConditionersContract.AvailableAA;
import com.mooo.ewolvy.realremote.database.AirConditionersDBAccess;

import java.util.ArrayList;
import java.util.List;

public class AAListActivity extends AppCompatActivity implements AAAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String POSITION = "POSITION";
    private static final int REQUEST_CODE_MODIFY = 1;
    private static final int REQUEST_CODE_NEW = 2;
    private AAAdapter adapter;

    public static List<AAItem> listData;

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

        //listData = AAData.getListData(this);
        listData = AirConditionersDBAccess.getAAItems(this);

        RecyclerView recView = (RecyclerView) findViewById(R.id.rec_list);
        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AAAdapter(listData, this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recView);
    }

    @Override
    public void onItemClick(View v, int p) {
        AAItem item = listData.get(p);

        Intent intent = new Intent(this, AAEditItemActivity.class);

        Bundle extras = new Bundle();
        extras.putInt(AvailableAA._ID, item.get_id());
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
            @SuppressWarnings("unchecked") // array creation must have wildcard
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
            int position = extras.getInt((POSITION));
            item.setPosition(position);
            /*item.setTemperature((listData.get(position)).getTemperature());
            item.setMode((listData.get(position)).getMode());
            item.setFan((listData.get(position)).getFan());
            item.setIs_on((listData.get(position)).getIs_on());*/

            listData.set(extras.getInt(POSITION), item);
            AirConditionersDBAccess.modifyAAItem(item, this);

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
            newItem.setTemperature(27);
            newItem.setMode(AASuper.AUTO_MODE);
            newItem.setFan(AASuper.AUTO_FAN);
            newItem.setIs_on(false);

            newItem.setPosition(listData.size() + 1);
            listData.add(newItem);
            AAData.saveListData(listData, this);
        }

        if (resultCode == RESULT_OK) {
            //AAData.saveListData(listData, this);
            adapter.setListData((ArrayList<AAItem>)listData);
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
        AAItem itemMoved = listData.get(oldPos);
        AAItem itemSwitched = listData.get(newPos);
        itemMoved.setPosition(newPos);
        itemSwitched.setPosition(oldPos);
        listData.remove(oldPos);
        listData.add(newPos, itemMoved);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem (final int pos){
        DialogInterface.OnClickListener deleteButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Yes" button, remove the item.
                        AirConditionersDBAccess.deleteAAItem(listData.get(pos).get_id(), getApplicationContext());
                        listData.remove(pos);
                        adapter.setListData((ArrayList<AAItem>)listData);
                    }
                };
        // Show dialog to confirm deletion
        ShowConfirmDeleteDialog(deleteButtonClickListener);
    }

    private void ShowConfirmDeleteDialog(
            DialogInterface.OnClickListener deleteButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_delete_dialog_msg);
        builder.setPositiveButton(R.string.yes, deleteButtonClickListener);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "No" button, so dismiss the dialog.
                if (dialog != null) {
                    adapter.setListData((ArrayList<AAItem>)listData);
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //AAData.saveListData(listData, this);
    }
}

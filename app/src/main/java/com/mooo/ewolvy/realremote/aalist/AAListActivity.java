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

import java.util.List;

public class AAListActivity extends AppCompatActivity implements AAAdapter.ItemClickCallback{

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final int REQUEST_CODE_MODIFY = 1;
    private static final int REQUEST_CODE_NEW = 2;

    public static AAAdapter adapter;
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
                extras.putInt(AvailableAA.COLUMN_NAME_POSITION, 0);

                intent.putExtra(BUNDLE_EXTRAS, extras);

                startActivityForResult(intent, REQUEST_CODE_NEW);
            }
        });

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
        extras.putInt(AvailableAA.COLUMN_NAME_POSITION, p);
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
            item.set_id(extras.getInt(AvailableAA._ID));
            item.setName(extras.getString(AvailableAA.COLUMN_NAME_NAME));
            item.setBrand(extras.getInt(AvailableAA.COLUMN_NAME_BRAND));
            item.setServer(extras.getString(AvailableAA.COLUMN_NAME_SERVER));
            item.setPort(extras.getInt(AvailableAA.COLUMN_NAME_PORT));
            item.setUsername(extras.getString(AvailableAA.COLUMN_NAME_USERNAME));
            item.setPassword(extras.getString(AvailableAA.COLUMN_NAME_PASSWORD));
            item.setCertificate(extras.getString(AvailableAA.COLUMN_NAME_CERTIFICATE));
            item.setAlias(extras.getString(AvailableAA.COLUMN_NAME_ALIAS));

            listData.set(extras.getInt(AvailableAA.COLUMN_NAME_POSITION), item);
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
            newItem.setPosition(listData.size());

            long id = AirConditionersDBAccess.addItem(newItem, this);

            newItem.set_id((int) id);
            listData.add(newItem);
        }

        if (resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged();
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
        // This method is called not only on release, but every time the selected item is moving
        // through the list. That's why it's working to just swap the old and new positions
        AAItem itemOldPos = listData.get(oldPos);
        AAItem itemNewPos = listData.get(newPos);

        AirConditionersDBAccess.modifyItemPosition(itemOldPos, newPos, this);
        AirConditionersDBAccess.modifyItemPosition(itemNewPos, oldPos, this);

        itemOldPos.setPosition(newPos);
        itemNewPos.setPosition(oldPos);

        listData.remove(oldPos);
        listData.add(newPos, itemOldPos);
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
                        adapter.notifyDataSetChanged();
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
                    //adapter.setDataSet((ArrayList<AAItem>)listData);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

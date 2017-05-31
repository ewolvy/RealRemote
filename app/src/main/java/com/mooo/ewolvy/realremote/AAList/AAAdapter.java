package com.mooo.ewolvy.realremote.AAList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mooo.ewolvy.realremote.R;

import java.util.List;

public class AAAdapter extends RecyclerView.Adapter<AAAdapter.AAHolder>{

    private List<AAItem> listData;
    private LayoutInflater inflater;

    public AAAdapter (List<AAItem> listData, Context c){
        this.inflater = LayoutInflater.from(c);
        this.listData = listData;
    }

    @Override
    public AAHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.aa_list_item, parent, false);
        return new AAHolder(view);
    }

    @Override
    public void onBindViewHolder(AAHolder holder, int position) {
        AAItem item = listData.get(position);
        holder.name.setText(item.getName());
        holder.server.setText(item.getServer());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class AAHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView server;
        private View container;

        public AAHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.aa_list_name);
            server = (TextView) itemView.findViewById(R.id.aa_list_link);
            container = itemView.findViewById(R.id.container);

        }
    }

}

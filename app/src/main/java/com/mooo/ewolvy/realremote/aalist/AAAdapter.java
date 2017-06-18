package com.mooo.ewolvy.realremote.aalist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mooo.ewolvy.realremote.R;

import java.util.List;

class AAAdapter extends RecyclerView.Adapter<AAAdapter.AAHolder>{

    private List<AAItem> dataSet;
    private LayoutInflater inflater;

    private ItemClickCallback itemClickCallback;

    interface ItemClickCallback{
        void onItemClick(View v, int p);
    }

    void setItemClickCallback(final ItemClickCallback itemClickCallback){
        this.itemClickCallback = itemClickCallback;
    }

    AAAdapter (List<AAItem> dataSet, Context c){
        this.inflater = LayoutInflater.from(c);
        this.dataSet = dataSet;
    }

    @Override
    public AAHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.aa_list_item, parent, false);
        return new AAHolder(view);
    }

    @Override
    public void onBindViewHolder(AAHolder holder, int position) {
        AAItem item = dataSet.get(position);
        holder.name.setText(item.getName());
        String serverText = item.getServer();
        serverText = serverText.concat(":");
        serverText = serverText.concat(String.valueOf(item.getPort()));
        serverText = serverText.concat("/");
        serverText = serverText.concat(item.getAlias());
        holder.server.setText(serverText);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class AAHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView server;
        private View container;

        AAHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.aa_list_name);
            server = (TextView) itemView.findViewById(R.id.aa_list_link);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(view, getAdapterPosition());
        }
    }
}

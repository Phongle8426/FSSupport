package com.example.fssupport.Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fssupport.History;
import com.example.fssupport.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapterRecyclerView  extends RecyclerView.Adapter {

    private HistoryRecyclerViewClickInterface recyclerViewClickInterface;
    List<ObjectHistory> historyList;
    public HistoryAdapterRecyclerView(List<ObjectHistory> historyList, History recyclerViewClickInterface){
        this.historyList = historyList;
        this.recyclerViewClickInterface = recyclerViewClickInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_layout,parent,false);
        HistoryAdapterRecyclerView.ViewAdapterClass viewAdapterClass = new HistoryAdapterRecyclerView.ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoryAdapterRecyclerView.ViewAdapterClass viewAdapterClass = (HistoryAdapterRecyclerView.ViewAdapterClass)holder;
        ObjectHistory history = historyList.get(position);
        viewAdapterClass.name.setText(history.getNameCenter());
        viewAdapterClass.type.setText(history.getTypeCenter());
        viewAdapterClass.time.setText(history.getDayTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name,type,time;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtv_nameCenter);
            type = itemView.findViewById(R.id.txtv_typeCenter);
            time = itemView.findViewById(R.id.txtv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    recyclerViewClickInterface.onItemClick(position);
                }
            });
        }
    }
}

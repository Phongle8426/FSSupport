package com.example.fssupport.Object;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fssupport.History;
import com.example.fssupport.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapterRecyclerView  extends RecyclerView.Adapter {

    private HistoryRecyclerViewClickInterface recyclerViewClickInterface;
    List<ObjectHistory> historyList;
    public DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
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
        Random r = new Random();
        int red = r.nextInt(255+1);
        int green = r.nextInt(255+1);
        int blu = r.nextInt(255+1);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setColor(Color.rgb(red,green,blu));
        HistoryAdapterRecyclerView.ViewAdapterClass viewAdapterClass = (HistoryAdapterRecyclerView.ViewAdapterClass)holder;
        ((ViewAdapterClass) holder).background.setBackground(drawable);
        ObjectHistory history = historyList.get(position);
        if(history.getNameCenter().length()>12)
            viewAdapterClass.name.setText(history.getNameCenter().substring(0,11)+"...");
        else  viewAdapterClass.name.setText(history.getNameCenter());
        viewAdapterClass.type.setText(history.getTypeCenter());
        viewAdapterClass.time.setText(history.getDayTime());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name,type,time;
        RelativeLayout background;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtv_nameCenter);
            type = itemView.findViewById(R.id.txtv_typeCenter);
            time = itemView.findViewById(R.id.txtv_time);
            background = itemView.findViewById(R.id.bg);
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

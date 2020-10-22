package com.example.fssupport.Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fssupport.Contact;
import com.example.fssupport.R;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapterRecyclerView extends RecyclerView.Adapter{
    private RecyclerViewClickInterface recyclerViewClickInterface;
    List<ObjectContact> contactList;
     public ContactAdapterRecyclerView(List<ObjectContact> contactList,RecyclerViewClickInterface recyclerViewClickInterface){
         this.contactList = contactList;
         this.recyclerViewClickInterface = recyclerViewClickInterface;
     }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemcontact_layout,parent,false);
        ViewAdapterClass viewAdapterClass = new ViewAdapterClass(view);
        return viewAdapterClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewAdapterClass viewAdapterClass = (ViewAdapterClass)holder;
        ObjectContact contact = contactList.get(position);
        viewAdapterClass.phone.setText(contact.getPhone_number());
        viewAdapterClass.name.setText(contact.getName_contact());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewAdapterClass extends RecyclerView.ViewHolder {
        TextView name,phone;
        Button edit;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtv_nameContact);
            phone = itemView.findViewById(R.id.txtv_phoneContact);
            edit = itemView.findViewById(R.id.btn_edit);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    recyclerViewClickInterface.onItemClick(position);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    recyclerViewClickInterface.onLongItemClick(getAbsoluteAdapterPosition());
                    return false;
                }
            });
        }

    }
}


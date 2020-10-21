package com.example.fssupport.Object;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fssupport.Contact;
import com.example.fssupport.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapterRecyclerView extends RecyclerView.Adapter{
    Contact conn = new Contact();
    List<ObjectContact> contactList;
     public ContactAdapterRecyclerView(List<ObjectContact> contactList){
         this.contactList = contactList;
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

    public class ViewAdapterClass extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name,phone;
        Button edit;
        public ViewAdapterClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtv_nameContact);
            phone = itemView.findViewById(R.id.txtv_phoneContact);
            edit = itemView.findViewById(R.id.btn_edit);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
           int position = getAbsoluteAdapterPosition();
           String xxx =  contactList.get(position).getName_contact();
            Toast.makeText(view.getContext(), "" + xxx, Toast.LENGTH_SHORT).show();

        }
    }
}


package com.example.fssupport.Object;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fssupport.R;

import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<ObjectContact> contactList;

    public ContactAdapter(Context context, int layout, List<ObjectContact> contactList) {
        this.context = context;
        this.layout = layout;
        this.contactList = contactList;
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(layout,null);
        TextView nameContact = (TextView)view.findViewById(R.id.txtv_phoneContact);
        TextView phoneNumber = (TextView)view.findViewById(R.id.txtv_nameContact);

        ObjectContact contact = contactList.get(i);
        nameContact.setText(contact.getName_contact());
        phoneNumber.setText(contact.getPhone_number());
        return view;
    }
}

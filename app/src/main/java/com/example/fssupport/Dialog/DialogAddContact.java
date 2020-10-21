package com.example.fssupport.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fssupport.Object.ObjectContact;
import com.example.fssupport.ObjectInfoUser;
import com.example.fssupport.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogAddContact extends AppCompatDialogFragment {
    EditText txtName,txtPhone;
    public String uid,name,phone;
    private DatabaseReference mDatabase;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getIDCanhan();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_contact,null);
        builder.setView(view).setTitle("Add your contact").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pushContact();
                    }
                });
        txtName = view.findViewById(R.id.txt_phoneAddContact);
        txtPhone = view.findViewById(R.id.txt_nameAddContact);
        return builder.create();
    }

    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }
    }

    public void pushContact(){
        name = txtName.getText().toString();
        phone = txtPhone.getText().toString();
        ObjectContact contact = new ObjectContact(phone,name);
        mDatabase.child("ContactUser").child(uid).child(phone).setValue(contact);
    }
    public void check(){
    }

}

package com.example.fssupport.Object;

public class ObjectContact {
    private String phone_number;
    private String name_contact;

    public ObjectContact() {
    }

    public ObjectContact(String name_contact,String phone_number) {
        this.phone_number = phone_number;
        this.name_contact = name_contact;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getName_contact() {
        return name_contact;
    }

    public void setName_contact(String name_contact) {
        this.name_contact = name_contact;
    }
}

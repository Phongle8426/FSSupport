package com.example.fssupport.Object;

public class ObjectCenterSignUp {
    private String centerName, presenter,centerEmail,centerAddress,centerPhone,centerType;

    public ObjectCenterSignUp() {
    }

    public ObjectCenterSignUp(String centerName, String presenter, String centerEmail, String centerAddress, String centerPhone, String centerType) {
        this.centerName = centerName;
        this.presenter = presenter;
        this.centerEmail = centerEmail;
        this.centerAddress = centerAddress;
        this.centerPhone = centerPhone;
        this.centerType = centerType;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getPresenter() {
        return presenter;
    }

    public void setPresenter(String presenter) {
        this.presenter = presenter;
    }

    public String getCenterEmail() {
        return centerEmail;
    }

    public void setCenterEmail(String centerEmail) {
        this.centerEmail = centerEmail;
    }

    public String getCenterAddress() {
        return centerAddress;
    }

    public void setCenterAddress(String centerAddress) {
        this.centerAddress = centerAddress;
    }

    public String getCenterPhone() {
        return centerPhone;
    }

    public void setCenterPhone(String centerPhone) {
        this.centerPhone = centerPhone;
    }

    public String getCenterType() {
        return centerType;
    }

    public void setCenterType(String centerType) {
        this.centerType = centerType;
    }
}

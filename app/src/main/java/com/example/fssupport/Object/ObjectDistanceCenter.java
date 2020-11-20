package com.example.fssupport.Object;

public class ObjectDistanceCenter {
    private String latitude;
    private String longitude;
    private String namecenter;
    private String centerid;
    private String city;

    public ObjectDistanceCenter() {
    }

    public ObjectDistanceCenter(String latitude, String longitude, String namecenter, String centerid,String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.namecenter = namecenter;
        this.centerid = centerid;
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCenterid() {
        return centerid;
    }

    public void setCenterid(String centerid) {
        this.centerid = centerid;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getNamecenter() {
        return namecenter;
    }

    public void setNamecenter(String namecenter) {
        this.namecenter = namecenter;
    }
}

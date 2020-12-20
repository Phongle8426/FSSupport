package com.example.fssupport.Object;

public class ObjectHistory {
    String nameCenter,typeCenter,cityCenter,dayTime,centerID,latitude,longitude;

    public ObjectHistory() {
    }

    public ObjectHistory(String nameCenter, String typeCenter,String cityCenter, String dayTime, String centerID, String latitude, String longitude) {
        this.nameCenter = nameCenter;
        this.typeCenter = typeCenter;
        this.cityCenter = cityCenter;
        this.dayTime = dayTime;
        this.centerID = centerID;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityCenter() {
        return cityCenter;
    }

    public void setCityCenter(String cityCenter) {
        this.cityCenter = cityCenter;
    }

    public String getCenterID() {
        return centerID;
    }

    public void setCenterID(String centerID) {
        this.centerID = centerID;
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

    public String getNameCenter() {
        return nameCenter;
    }

    public void setNameCenter(String nameCenter) {
        this.nameCenter = nameCenter;
    }

    public String getTypeCenter() {
        return typeCenter;
    }

    public void setTypeCenter(String typeCenter) {
        this.typeCenter = typeCenter;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

}

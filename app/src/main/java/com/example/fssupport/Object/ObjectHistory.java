package com.example.fssupport.Object;

public class ObjectHistory {
    String nameCenter,typeCenter,dayTime,content;

    public ObjectHistory() {
    }

    public ObjectHistory(String nameCenter, String typeCenter, String dayTime, String content) {
        this.nameCenter = nameCenter;
        this.typeCenter = typeCenter;
        this.dayTime = dayTime;
        this.content = content;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

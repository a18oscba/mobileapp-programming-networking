package com.example.networking;

public class Mountain {
    private String ID;
    private String size;
    private String name;
    private String location;
    public Mountain (String ID, String name, String location, String size) {
        ID = ID;
        this.name = name;
        this.location = location;
        this.size = size;
    }
    public String getID(){
        return ID;
    }
    public String getSize(){
        return size;
    }
    public String getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
    @Override
    public String toString(){
        return name;
    }
}

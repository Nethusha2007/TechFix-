package com.techfix.app.model;

public class Branch {
    public int id;
    public String name;
    public String address;
    public String city;
    public String distance;   
    public String hours;      
    public String phone;
    public double lat;        
    public double lng;        

    public Branch(int id, String name, String address, String city, String distance, String hours, String phone) {
        this(id, name, address, city, distance, hours, phone, 0, 0);
    }

    public Branch(int id, String name, String address, String city, String distance,
                  String hours, String phone, double lat, double lng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.distance = distance;
        this.hours = hours;
        this.phone = phone;
        this.lat = lat;
        this.lng = lng;
    }
}

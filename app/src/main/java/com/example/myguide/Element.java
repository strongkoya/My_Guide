package com.example.myguide;

public class Element {
    private String name;
    private double latitude;
    private double longitude;
    private String rating;
    private String address;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Element(String name, double latitude, double longitude, String rating, String address, String url) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.address = address;
        this.url = url;
    }

    public Element(String name, double latitude, double longitude, String rating, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Element(String name) {
        this.name = name;
    }

    public Element(String name, double lt, double lg) {
        this.name = name;
        this.latitude=lt;
        this.longitude=lg;

    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }
}


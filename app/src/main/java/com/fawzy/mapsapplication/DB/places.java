package com.fawzy.mapsapplication.DB;

public class places {

    private  int id ;
    private String latitude , longtitude ;
    private String tittle ;

    public places() {
    }

    public places(String latitude, String longtitude, String tittle) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.tittle = tittle;
    }

    public places(int id, String latitude, String longtitude, String tittle) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.tittle = tittle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

}

package com.example.liron.finalproject.model;

import java.util.ArrayList;

/**
 * Created by Oran on 01/08/2017.
 */

public class Ride {

    private String rideID;
    private User rideOwner;
    private long date;
    private long time;
    private String from;
    private String to;
    private long freeSeats;
    private ArrayList<String> hitchhikers;

    public Ride(String rideID, User rideOwner, long date, long time, String from, String to, int freeSeats, ArrayList<String> hitchhikers) {
        this.rideID = rideID;
        this.rideOwner = rideOwner;
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
        this.freeSeats = freeSeats;
        this.hitchhikers = hitchhikers;
    }

    public Ride(){}

    public String getRideID() {
        return rideID;
    }

    public void setRideID(String rideID) {
        this.rideID = rideID;
    }

    public User getRideOwner() {
        return rideOwner;
    }

    public void setRideOwner(User rideOwner) {
        this.rideOwner = rideOwner;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(long freeSeats) {
        this.freeSeats = freeSeats;
    }

    public ArrayList<String> getHitchhikers() {
        return hitchhikers;
    }

    public void setHitchhikers(ArrayList<String> hitchhikers) {
        this.hitchhikers = hitchhikers;
    }
}
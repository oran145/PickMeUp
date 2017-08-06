package com.example.liron.finalproject.model;

import java.util.List;

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
    private int freeSeats;
    private List<User> hitchhikers;

    public Ride(String rideID, User rideOwner, long date, long time, String from, String to, int freeSeats, List<User> hitchhikers) {
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

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public List<User> getHitchhikers() {
        return hitchhikers;
    }

    public void setHitchhikers(List<User> hitchhikers) {
        this.hitchhikers = hitchhikers;
    }
}
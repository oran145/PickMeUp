package com.example.liron.finalproject.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by liron on 17-Aug-17.
 */

class RideSql
{

    private static final String RIDES = "RIDES";
    private static final String RIDE_ID = "rideId";
    private static final String OWNER_ID = "ownerId";
    private static final String DATE = "date";
    private static final String TIME = "time";
    private static final String RIDE_FROM = "rideFrom";
    private static final String RIDE_TO = "rideTo";
    private static final String FREE_SEATS = "freeSeats";
    private static final String HITCHHIKERS = "hitchhikers";


    public static void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + RIDES + " (" + RIDE_ID + " TEXT PRIMARY KEY, "
                + OWNER_ID + " TEXT, " + DATE + " TEXT, "+TIME+" TEXT, "+RIDE_FROM+" TEXT, "
                + RIDE_TO + " TEXT, "  + FREE_SEATS + " INTEGER, "+HITCHHIKERS+" TEXT);");

    }

    public static void onUpgrade(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE " + RIDES);
    }


    public static void getAllRides(SQLiteOpenHelper helper,Model.GetAllRidesListener listener)
    {

        SQLiteDatabase readableDatabase = helper.getReadableDatabase();

        Cursor cursor = readableDatabase.query(true,RIDES,null,null,null,null,null,null,null);

        ArrayList<Ride> rideList = new ArrayList<Ride>();

        if(cursor.getCount()!=0)
        {
            //going throgh all list that came from local
            for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                // The Cursor is now set to the right position
                Ride ride=new Ride();
                ride.setRideID(cursor.getString(cursor.getColumnIndex(RIDE_ID)));
                ride.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
                ride.setTime(cursor.getString(cursor.getColumnIndex(TIME)));
                ride.setFrom(cursor.getString(cursor.getColumnIndex(RIDE_FROM)));
                ride.setTo(cursor.getString(cursor.getColumnIndex(RIDE_TO)));
                ride.setFreeSeats(cursor.getLong(cursor.getColumnIndex(FREE_SEATS)));

                User owner = UserSql.getUserById(readableDatabase,cursor.getString(cursor.getColumnIndex(OWNER_ID)));
                ride.setRideOwner(owner);

                String hitchhikersIdString = cursor.getString(cursor.getColumnIndex(HITCHHIKERS));
                if(!hitchhikersIdString.isEmpty())
                {
                    String[] hitchhikersSplitArray = hitchhikersIdString.split(",");
                    ArrayList<String> hitchhikersList = new ArrayList<String>(Arrays.asList(hitchhikersSplitArray));
                    ride.setHitchhikers(hitchhikersList);
                }
                rideList.add(ride);
            }

            listener.onComplete(rideList);
            listener.hideProgressBar();
        }

    }


    public static void writeDataFromFireBase(SQLiteOpenHelper helper , ArrayList<Ride> rideList,Model.GetAllRidesListener listener)
    {
        SQLiteDatabase writeableDatabase =  helper.getWritableDatabase();
        SQLiteDatabase readableDatabase =  helper.getReadableDatabase();

        Date currentTime = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = simpleDateFormat.format(currentTime);

        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
        String formatedTime = simpleTimeFormat.format(currentTime);

        for (Ride ride: rideList)
        {
            String rideDate = ride.getDate();
            String rideTime = ride.getTime();

            try
            {
                Date start = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(formatedDate + " " + formatedTime );
                Date end = new SimpleDateFormat("dd/MM/yyyy HH:mm")
                        .parse(rideDate + " " + rideTime);

                if (start.compareTo(end) > 0)
                {
                    continue;
                }

                Cursor cursor = null;
                String sql ="SELECT " + RIDE_ID + " FROM "+RIDES+" WHERE " + RIDE_ID + "='"+ride.getRideID()+"'";
                cursor = readableDatabase.rawQuery(sql,null);

                if(cursor.getCount()>0)
                {
                    readableDatabase.execSQL("delete from "+RIDES+" where " + RIDE_ID +" ='"+ride.getRideID()+"'");
                }


                ContentValues values = new ContentValues();

                values.put(RIDE_ID, ride.getRideID());
                values.put(OWNER_ID, ride.getRideOwner().getUserID());

                values.put(DATE,ride.getDate());
                values.put(TIME,ride.getTime());

                values.put(RIDE_FROM,ride.getFrom());
                values.put(RIDE_TO,ride.getTo());
                values.put(FREE_SEATS,ride.getFreeSeats());

                String hitchhikers ="";
                for (String hitchhiker : ride.getHitchhikers())
                {
                    hitchhikers += hitchhiker+",";
                }
                hitchhikers.substring(0, hitchhikers.length() - 1);
                values.put("hitchhikers",hitchhikers);
                writeableDatabase.insert(RIDES,RIDE_ID,values);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
        RideSql.getAllRides(helper,listener);
    }


    public static void removeRide(SQLiteDatabase writableDatabase, String rideID)
    {
        writableDatabase.execSQL("delete from "+RIDES+" where " + RIDE_ID +" ='"+rideID+"'");
    }
}


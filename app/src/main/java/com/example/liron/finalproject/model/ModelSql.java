package com.example.liron.finalproject.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.liron.finalproject.MyAppContext;

import java.util.ArrayList;

/**
 * Created by liron on 17-Aug-17.
 */

public class ModelSql {

    private SQLiteOpenHelper helper;

    public ModelSql()
    {
        helper = new Helper(MyAppContext.getAppContext());

    }

    public void writeDataFromFireBase(ArrayList<Ride> rideList,Model.GetAllRidesListener listener)
    {
        RideSql.writeDataFromFireBase(helper,rideList,listener);
    }

    public void writeUsersFromFireBase(ArrayList<User> userList)
    {
        UserSql.writeUsersFromFireBase(helper.getWritableDatabase(),userList);
    }

    public void removeRide(String rideID)
    {
        RideSql.removeRide(helper.getWritableDatabase(),rideID);
    }

    public class Helper extends SQLiteOpenHelper
    {
        public Helper(Context context)
        {
            super(context, "DataBase.db" , null, 3);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            RideSql.onCreate(db);
            UserSql.onCreate(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            RideSql.onUpgrade(db);
            UserSql.onUpgrade(db);
            onCreate(db);
        }
    }


}

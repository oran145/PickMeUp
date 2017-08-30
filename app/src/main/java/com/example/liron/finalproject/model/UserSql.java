package com.example.liron.finalproject.model;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.liron.finalproject.MyAppContext;
import com.example.liron.finalproject.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by liron on 17-Aug-17.
 */

class UserSql
{

    private static final String USERS = "USERS";//table name
    private static final String USER_ID = "userId";//user id primary key ,get from firebase uid at athunticaton,text
    private static final String IMAGE_LOCAL_URL = "imageLocalUrl";//the image path of the saved image on the phone,text
    private static final String IMAGE_FIREBASE_URL = "imageFireBaseUrl";//the image path of the saved image on the phone,text
    private static final String FIRST_NAME = "firstName";//user first name
    private static final String LAST_NAME = "lastName";
    private static final String BIRTHDAY="birthday";


    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + USERS + " (" + USER_ID + " TEXT PRIMARY KEY, "
                + IMAGE_LOCAL_URL + " TEXT, " + IMAGE_FIREBASE_URL + " TEXT, " + FIRST_NAME + " TEXT, "+LAST_NAME+" TEXT, "+BIRTHDAY+" INTEGER)");
    }


    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS);
    }

    public static void writeUsersFromFireBase(SQLiteDatabase writableDatabase , ArrayList<User> userList)
    {
        for (User user: userList)
        {
            String absolutePathName = saveImageToFile(user.getUserImage(),user.getImageFireBaseUrl());
            if(absolutePathName==null)
            {
                Log.e("TAG","fail to save image");
            }

            ContentValues values = new ContentValues();

            values.put(USER_ID, user.getUserID());
            //values.put(IMAGE_LOCAL_URL,absolutePathName);
            values.put(IMAGE_FIREBASE_URL,user.getImageFireBaseUrl());
            values.put(FIRST_NAME, user.getFirstName());
            values.put(LAST_NAME, user.getLastName());
            values.put(BIRTHDAY, user.getBirthday());

            long rowId = writableDatabase.insert(USERS, USER_ID, values);
            if (rowId <= 0)
            {
                Log.e("TAG","fail to insert into USERS table");
            }
        }
    }

    private static String saveImageToFile(Bitmap imageBitmap, String imageFileName)
    {
        boolean isExternalStorageWritable= Utility.isExternalStorageWritable();
        String pathName=null;
        if(isExternalStorageWritable)
        {
            try {
                File dir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                if (!dir.exists()) if (!dir.mkdir()) {
                    return null;
                }
                pathName=dir.getAbsolutePath()+"/"+imageFileName;
                File imageFile = new File(dir,imageFileName);
                if (imageFile.createNewFile()) {
                    OutputStream out = new FileOutputStream(imageFile);
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.close();
                    addPictureToGallery(imageFile);
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathName;
    }

    private static void addPictureToGallery(File imageFile)
    {
        //add the picture to the gallery so we dont need to manage the cache size
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(imageFile);
        mediaScanIntent.setData(contentUri);
        MyAppContext.getAppContext().sendBroadcast(mediaScanIntent);
    }


    public static User getUserById(SQLiteDatabase readableDatabase,String userId)
    {
        String where = USER_ID+"='"+userId+"'";

        Cursor cursor = readableDatabase.query(true,USERS,null,where,null,null,null,null,null,null);
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            User user = new User();

            user.setUserID(cursor.getString(cursor.getColumnIndex(USER_ID)));
            user.setImageFireBaseUrl(cursor.getString(cursor.getColumnIndex(IMAGE_FIREBASE_URL)));
            user.setFirstName(cursor.getString(cursor.getColumnIndex(FIRST_NAME)));
            user.setLastName(cursor.getString(cursor.getColumnIndex(LAST_NAME)));
            user.setBirthday(cursor.getLong(cursor.getColumnIndex(BIRTHDAY)));

            return user;
        }
        return new User();
    }



}

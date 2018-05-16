package org.diiage.delbano.partiel2018delbano;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Bastien on 16/05/2018.
 */

public class ReleaseDbHelper extends SQLiteOpenHelper{

    private static final int NEW_VERSION = 2;
    private static final int OLD_VERSION = 1;
    private static final String DB_NAME = "release.db";
    private static final String TABLE_NAME = "release";

    public ReleaseDbHelper(Context context){
        super (context, DB_NAME, null, NEW_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `release` ( " +
                "`status` TEXT, `thumb` TEXT, `format` INTEGER, `title` TEXT , `catno` TEXT, " +
                "`year` INTEGER, `resource_url` TEXT, `artist` TEXT , `id` INTEGER PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (NEW_VERSION == 2){
            //on récup les données nécessaires
            ArrayList<String> artists = GetAllArtist(db);
            ArrayList<Release> releases = GetAllRelease(db);
            //on supprime la table release
            db.execSQL("DROP TABLE `release`");
            //on recréer la table release comme souhaité
            db.execSQL("CREATE TABLE `release` ( " +
                    "`status` TEXT, `thumb` TEXT, `format` INTEGER, `title` TEXT , `catno` TEXT, " +
                    "`year` INTEGER, `resource_url` TEXT, `id` INTEGER PRIMARY KEY )");

            //on créer la table artist
            db.execSQL("CREATE TABLE `artist` ( `id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT )");

            //insert des données
            for (Release release : releases){
                long idLastRelease = insertRelease(db, release);
            }
            for (String artist : artists){
                long idLastArtist = insertArtist(db, artist);
            }
        }
    }

    public long insertRelease(SQLiteDatabase db, Release obj){

        ContentValues cv = new ContentValues();

        //on verifie dans quelle version on se trouve pour ne pas avoir de probleme avec artist en version 2
        if (NEW_VERSION == 2){
            cv.put("status", obj.getStatus());
            cv.put("thumb", obj.getThumb());
            cv.put("format", obj.getFormat());
            cv.put("title", obj.getTitle());
            cv.put("catno", obj.getCatno());
            cv.put("year", obj.getYear());
            cv.put("resource_url", obj.getResourceUrl());
            cv.put("id", obj.getId());
        }else {
            cv.put("status", obj.getStatus());
            cv.put("thumb", obj.getThumb());
            cv.put("format", obj.getFormat());
            cv.put("title", obj.getTitle());
            cv.put("catno", obj.getCatno());
            cv.put("year", obj.getYear());
            cv.put("resource_url", obj.getResourceUrl());
            cv.put("artist", obj.getArtist());
            cv.put("id", obj.getId());
        }
        long id = 0;

        try {
            id = db.insert("release", null, cv);
        }catch (SQLException e){
            Log.e("sqlexception", e.getMessage());
        }
        return id;
    }

    public long insertArtist(SQLiteDatabase db, String str){
        ContentValues cv = new ContentValues();
        cv.put("name", str);

        long id = 0;

        try {
            id = db.insert("artist", null, cv);
        }catch (SQLException e){
            Log.e("sqlexception", e.getMessage());
        }
        return id;
    }

    public ArrayList<String> GetAllArtist(SQLiteDatabase db){
        Cursor c = db.query("release", null, null,null, "artist", null, null);

        ArrayList<String> artists = new ArrayList<>();
        while (c.moveToNext()){
            String str = c.getString(7);
            artists.add(str);
        }
        return artists;
    }

    public ArrayList<Release> GetAllRelease(SQLiteDatabase db){
        Cursor c = db.query("release", null, null,null, null, null, null);

        ArrayList<Release> releases = new ArrayList<>();
        while (c.moveToNext()){
            if (NEW_VERSION == 2){
                Release r = new Release(c.getString(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getInt(5), c.getString(6), null, c.getInt(7));
                releases.add(r);
            }else{
                Release r = new Release(c.getString(0), c.getString(1), c.getString(2), c.getString(3),
                        c.getString(4), c.getInt(5), c.getString(6), c.getString(7), c.getInt(8));
                releases.add(r);
            }

        }
        return releases;
    }
}

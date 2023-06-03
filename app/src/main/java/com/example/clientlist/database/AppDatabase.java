package com.example.clientlist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Client.class, Notes.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final Object LOCK = new Object();
    public static final String DATABASE_NAME = "client_list_db";
    private static AppDatabase instanceDB;

    public static AppDatabase getInstance(Context context)
    {
        if (instanceDB == null)
        {
            synchronized (LOCK)
            {
                instanceDB = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
            }
            }
            return instanceDB;
        }
        public abstract clientDAO clientDAO();
        public abstract notesDAO notesDAO();
}

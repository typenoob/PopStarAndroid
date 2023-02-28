package com.example.popstar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Record.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();

    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }


    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context,
                AppDatabase.class, "record").allowMainThreadQueries().build();
    }
}
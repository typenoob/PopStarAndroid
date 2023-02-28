package com.example.popstar.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM record")
    List<Record> getAll();

    @Query("SELECT * FROM record WHERE id IN (:userIds)")
    List<Record> loadAllByIds(int[] userIds);

    @Query("DELETE FROM record")
    void deleteAll();

    @Insert
    void insertAll(Record... users);

    @Delete
    void delete(Record record);


}

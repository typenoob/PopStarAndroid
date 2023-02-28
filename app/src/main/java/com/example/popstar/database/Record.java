package com.example.popstar.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.Data;

@Data
@Entity
public class Record{
    public Record(int score){
        this.score=score;
        this.time=LocalDateTime.now();
    }
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    @ColumnInfo(name = "userId")
    public int userId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @ColumnInfo(name = "score")
    public int score;

    @ColumnInfo(name = "time")
    public LocalDateTime time;
}
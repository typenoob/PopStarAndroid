package com.example.popstar.database;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Converters {
    @TypeConverter
    public static LocalDateTime fromTimestamp(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDateTime date) {
        return date == null ? null :   date.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
    }
}
package com.example.memorybootcamp.database;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import kotlin.jvm.JvmStatic;

public class Converters {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @TypeConverter
    @JvmStatic
    public OffsetDateTime toOffsetDateTime(String value) {
        return formatter.parse(value, OffsetDateTime::from);
    }

    @TypeConverter
    @JvmStatic
    public String fromOffsetDateTime(OffsetDateTime date) {
        return date.format(formatter);
    }

    /*
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }*/
}
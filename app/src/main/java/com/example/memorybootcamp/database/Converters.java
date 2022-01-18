package com.example.memorybootcamp.database;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import kotlin.jvm.JvmStatic;

/** Converting methods for the database. */
public class Converters {

    /** Time formatter for database. */
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /** Formatter for converting from string to offset datetime. */
    @TypeConverter
    @JvmStatic
    public OffsetDateTime toOffsetDateTime(String value) {
        return formatter.parse(value, OffsetDateTime::from);
    }

    /** Formatter for converting from offset datetime to string. */
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
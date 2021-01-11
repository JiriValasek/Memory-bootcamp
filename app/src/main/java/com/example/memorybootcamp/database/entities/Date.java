package com.example.memorybootcamp.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(
        tableName = "binary_results",
        indices = {@Index(value="id",unique = true),})
public class Date {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @NotNull
    @ColumnInfo(name = "date")
    private final java.sql.Date mDate;

    public Date(@NotNull java.sql.Date date) {
        this.mDate = date;
    }

    public int getId(){return this.mId;}
    public java.sql.Date getDate(){return this.mDate;}

}
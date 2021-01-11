package com.example.memorybootcamp.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(
        tableName = "all_results",
        foreignKeys = @ForeignKey(entity = Date.class,
                        parentColumns = "date_id",
                        childColumns = "id",
                        onDelete = ForeignKey.CASCADE),
        indices =  {@Index(value = {"id", "date_id"}, unique = true)})
public class Result {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "date_id")
    private final int mDateId;

    @ColumnInfo(name = "score") // number of correct answers
    private final int mScore;

    @ColumnInfo(name = "total") // total size of a challenge
    private final int mTotal;

    @ColumnInfo(name = "include") // should be included in results
    private final boolean mInclude;

    @NotNull
    @ColumnInfo(name = "type") // binary / cards / faces / numbers / words
    private final String mType;

    public Result(int dateId, int score, int total, boolean include, @NotNull String challengeType) {
        this.mDateId = dateId;
        this.mScore = score;
        this.mTotal = total;
        this.mInclude = include;
        this.mType = challengeType;
    }

    public int getId(){return this.mId;}
    public int getDateId(){return this.mDateId;}
    public int getScore(){return this.mScore;}
    public int getTotal(){return this.mTotal;}
    public boolean getInclude(){return this.mInclude;}
    public String getType(){return this.mType;}

}

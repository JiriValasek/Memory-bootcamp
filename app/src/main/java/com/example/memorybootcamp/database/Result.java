package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

/** Result record entity. */
@Entity(
        tableName = "results",
        indices =  {@Index(value = {"id"}, unique = true)})
public class Result {

    /** ID column. */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int mId;

    /** Number of correct answers. */
    @ColumnInfo(name = "score")
    private final int mScore;

    /** Total size of a challenge tried. */
    @ColumnInfo(name = "total")
    private final int mTotal;

    /** Date when result was issued. */
    @ColumnInfo(name = "achieved")
    private final OffsetDateTime mAchieved;

    /** Type of a challenge tried - binary, cards, faces, numbers, words. */
    @NotNull
    @ColumnInfo(name = "type")
    private final String mChallengeType;

    /** Constructor */
    public Result(int score, int total, OffsetDateTime achieved, @NotNull String challengeType) {
        this.mScore = score;
        this.mTotal = total;
        this.mAchieved = achieved;
        this.mChallengeType = challengeType;
    }

    /** Getter for recorded ID. */
    public int getId(){return this.mId;}
    /** Getter for recorded score. */
    public int getScore(){return this.mScore;}
    /** Getter for total number of elements in a challenge tried. */
    public int getTotal(){return this.mTotal;}
    /** Getter for achieved score. */
    public OffsetDateTime getAchieved(){return this.mAchieved;}
    /** Getter for type of challenge tried. */
    public String getChallengeType(){return this.mChallengeType;}

}

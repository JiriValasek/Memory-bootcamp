package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;

@Entity(
        tableName = "results",
        indices =  {@Index(value = {"id"}, unique = true)})
public class Result {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    protected int mId;

    @ColumnInfo(name = "score") // number of correct answers
    private final int mScore;

    @ColumnInfo(name = "total") // total size of a challenge
    private final int mTotal;

    @ColumnInfo(name = "achieved") // date when the result was issued
    private final OffsetDateTime mAchieved;

    @NotNull
    @ColumnInfo(name = "type") // binary / cards / faces / numbers / words
    private final String mChallengeType;

    public Result(int score, int total, OffsetDateTime achieved, @NotNull String challengeType) {
        this.mScore = score;
        this.mTotal = total;
        this.mAchieved = achieved;
        this.mChallengeType = challengeType;
    }

    public int getId(){return this.mId;}
    public int getScore(){return this.mScore;}
    public int getTotal(){return this.mTotal;}
    public OffsetDateTime getAchieved(){return this.mAchieved;}
    public String getChallengeType(){return this.mChallengeType;}

}

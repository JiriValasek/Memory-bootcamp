package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;

import org.jetbrains.annotations.NotNull;

public class ScoreType {

    @ColumnInfo(name = "MAX(score)") // number of correct answers
    private final int mMaxScore;

    @NotNull
    @ColumnInfo(name = "type") // binary / cards / faces / numbers / words
    private final String mChallengeType;

    public ScoreType(@NotNull String challengeType, int maxScore) {
        this.mMaxScore = maxScore;
        this.mChallengeType = challengeType;
    }

    public int getScore(){return this.mMaxScore;}
    public String getChallengeType(){return this.mChallengeType;}
}

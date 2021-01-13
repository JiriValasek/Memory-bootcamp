package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;

import java.time.OffsetDateTime;

public class ChallengeResult {

    @ColumnInfo(name = "score") // number of correct answers
    private final int mScore;

    @ColumnInfo(name = "achieved") // date when the result was issued
    private final OffsetDateTime mAchieved;

    public ChallengeResult(int score, OffsetDateTime achieved) {
        this.mScore = score;
        this.mAchieved = achieved;
    }

    public int getScore(){return this.mScore;}
    public OffsetDateTime getDate(){return this.mAchieved;}
}

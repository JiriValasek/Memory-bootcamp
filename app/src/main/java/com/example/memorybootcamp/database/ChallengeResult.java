package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;

import java.time.OffsetDateTime;

/** Database POJO for challenge result. */
public class ChallengeResult {

    /** Number of correct answers. */
    @ColumnInfo(name = "score")
    private final int mScore;

    /** Date when result was issued. */
    @ColumnInfo(name = "achieved")
    private final OffsetDateTime mAchieved;

    /** Constructor */
    public ChallengeResult(int score, OffsetDateTime achieved) {
        this.mScore = score;
        this.mAchieved = achieved;
    }

    /** Getter for recorded score. */
    public int getScore(){return this.mScore;}
    /** Getter for date of recorded score. */
    public OffsetDateTime getDate(){return this.mAchieved;}
}

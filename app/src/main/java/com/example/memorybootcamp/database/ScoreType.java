package com.example.memorybootcamp.database;

import androidx.room.ColumnInfo;

import org.jetbrains.annotations.NotNull;

/** Database POJO for challenge score-type object. */
public class ScoreType {

    /** Maximal number of correct answers. */
    @ColumnInfo(name = "MAX(score)")
    private final int mMaxScore;

    /** Type of a challenge - binary, cards, faces, numbers or words */
    @NotNull
    @ColumnInfo(name = "type")
    private final String mChallengeType;

    /** Constructor */
    public ScoreType(@NotNull String challengeType, int maxScore) {
        this.mMaxScore = maxScore;
        this.mChallengeType = challengeType;
    }

    /** Getter for maximal score in a given challenge type. */
    public int getScore(){return this.mMaxScore;}
    /** Getter for a challenge type in which the score is accomplished. */
    public String getChallengeType(){return this.mChallengeType;}
}

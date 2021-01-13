package com.example.memorybootcamp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Dao
public interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Result word);

    @Query("DELETE FROM results")
    void deleteAll();

    @Query("SELECT * FROM results ORDER BY date(achieved) ASC")
    LiveData<List<Result>> getAllChronologicalResults();

    @Query("SELECT score, achieved FROM results WHERE type=:type ORDER BY achieved ASC")
    LiveData<List<ChallengeResult>> getChronologicalResults(String type);

    @Query("SELECT MAX(score), type FROM results WHERE type='binary' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='cards' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='faces' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='numbers' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='words'"
    )
    LiveData<List<ScoreType>> getBestResult();

    @Query("SELECT MAX(score), type FROM results " +
            "WHERE type='binary' AND date(achieved) BETWEEN date('now', '-7 days') AND date('now') UNION " +
            "SELECT MAX(score), type FROM results " +
            "WHERE type='cards' AND date(achieved) BETWEEN date('now', '-7 days') AND date('now') UNION " +
            "SELECT MAX(score), type FROM results " +
            "WHERE type='faces' AND date(achieved) BETWEEN date('now', '-7 days') AND date('now') UNION " +
            "SELECT MAX(score), type FROM results " +
            "WHERE type='numbers' AND date(achieved) BETWEEN date('now', '-7 days') AND date('now') UNION " +
            "SELECT MAX(score), type FROM results " +
            "WHERE type='words' AND date(achieved) BETWEEN date('now', '-7 days') AND date('now') "
    )
    LiveData<List<ScoreType>> getWeeksBestResults();

    @Query("UPDATE results SET score=:score WHERE date(achieved)=date('now') AND type=:type")
    int updateTodayScore(@NotNull String type, int score);
}

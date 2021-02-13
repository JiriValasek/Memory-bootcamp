package com.example.memorybootcamp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

    // TODO add score=totol condition
    @Query("SELECT MAX(score), type FROM results WHERE type='binary' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='cards' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='faces' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='numbers' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='words'"
    )
    LiveData<List<ScoreType>> getBestResult();

    // TODO add score=total condition
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

    @Query("SELECT * FROM results WHERE type=:type AND date(achieved)=date('now')")
    LiveData<Result> getTodayResult( String type);

    //@Update(onConflict = OnConflictStrategy.REPLACE)
    @Query("UPDATE results " +
            "SET achieved=strftime('%Y-%m-%dT%H:%M:%f+00:00', 'now'), score=:score, total=:total " +
            "WHERE date(achieved)=date('now') AND type=:type AND score<=:score")
    int updateTodayScore( String type, int score, int total);

   @Query("DELETE FROM results " +
           "WHERE id IN (SELECT id FROM results " +
           "WHERE type=:type AND date(achieved)=date('now') ORDER BY score  ASC " +
           "LIMIT (SELECT COUNT(*) FROM results " +
           "WHERE type=:type AND date(achieved)=date('now')) - 1);")
    void leaveOnlyOneRecordOfTheDay( String type);

    @Query("DELETE FROM results WHERE score=0 AND total=0" )
    void pruneEmptyRows();
}

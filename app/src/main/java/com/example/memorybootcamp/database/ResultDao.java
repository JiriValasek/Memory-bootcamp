package com.example.memorybootcamp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/** Database access object for Greenbridge database. */
@Dao
public interface ResultDao {

    /** Insert result. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Result word);

    /** Delete all results from the DB. */
    @Query("DELETE FROM results")
    void deleteAll();

    /** Select all results from the DB in ascending order. */
    @Query("SELECT * FROM results ORDER BY date(achieved) ASC")
    LiveData<List<Result>> getAllChronologicalResults();

    /** Select all scores and datetimes from the DB in ascending order. */
    @Query("SELECT score, achieved FROM results WHERE type=:type ORDER BY achieved ASC")
    LiveData<List<ChallengeResult>> getChronologicalResults(String type);

    //TODO add score=total condition to show only fully corrected results
    /** Get best result for each challenge type. */
    @Query("SELECT MAX(score), type FROM results WHERE type='binary' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='cards' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='faces' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='numbers' UNION " +
            "SELECT MAX(score), type FROM results WHERE type='words'"
    )
    LiveData<List<ScoreType>> getBestResult();

    //TODO add score=total condition to show only fully corrected results
    /** Get best result of a week for each challenge type. */
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

    /** Get the best result today for a given type. */
    @Query("SELECT * FROM results WHERE type=:type AND date(achieved)=date('now')")
    LiveData<Result> getTodayResult( String type);

    /** Update the best result today for a given type. */
    @Query("UPDATE results " +
            "SET achieved=strftime('%Y-%m-%dT%H:%M:%f+00:00', 'now'), score=:score, total=:total " +
            "WHERE date(achieved)=date('now') AND type=:type AND score<=:score")
    int updateTodayScore( String type, int score, int total);

    /** Deleting redundant records of a day. */
   @Query("DELETE FROM results " +
           "WHERE id IN (SELECT id FROM results " +
           "WHERE type=:type AND date(achieved)=date('now') ORDER BY score  ASC " +
           "LIMIT (SELECT COUNT(*) FROM results " +
           "WHERE type=:type AND date(achieved)=date('now')) - 1);")
    void leaveOnlyOneRecordOfTheDay( String type);

   /** Pruning empty rows of the DB. */
    @Query("DELETE FROM results WHERE score=0 AND total=0" )
    void pruneEmptyRows();
}

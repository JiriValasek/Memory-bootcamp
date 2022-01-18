package com.example.memorybootcamp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/** Database for results. */
class ResultRepository {

    /** Database access object to the DB. */
    private final ResultDao mResultDao;
    /** All-time best results. */
    private final LiveData<List<ScoreType>> mBestResults;
    /** Weeks's best results. */
    private final LiveData<List<ScoreType>> mWeeksBestResults;
    /** Binary results. */
    private final LiveData<List<ChallengeResult>> mBinaryResults;
    /** Card results. */
    private final LiveData<List<ChallengeResult>> mCardsResults;
    /** Face results. */
    private final LiveData<List<ChallengeResult>> mFacesResults;
    /** Number results. */
    private final LiveData<List<ChallengeResult>> mNumbersResults;
    /** Word results. */
    private final LiveData<List<ChallengeResult>> mWordsResults;

    /** Constructor */
    ResultRepository(Application application) {
        ResultRoomDatabase db = ResultRoomDatabase.getDatabase(application);
        mResultDao = db.resultDao();
        mBestResults = mResultDao.getBestResult();
        mWeeksBestResults = mResultDao.getWeeksBestResults();
        mBinaryResults = mResultDao.getChronologicalResults("binary");
        mCardsResults = mResultDao.getChronologicalResults("cards");
        mFacesResults = mResultDao.getChronologicalResults("faces");
        mNumbersResults = mResultDao.getChronologicalResults("numbers");
        mWordsResults = mResultDao.getChronologicalResults("words");
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    /** Getter for all-time best results. */
    LiveData<List<ScoreType>> getBestResults() { return mBestResults; }
    /** Getter for week's best results. */
    LiveData<List<ScoreType>> getWeeksBestResults() { return mWeeksBestResults; }
    /** Getter for binary results. */
    LiveData<List<ChallengeResult>> getBinaryResults() { return mBinaryResults; }
    /** Getter for card results. */
    LiveData<List<ChallengeResult>> getCardsResults() { return mCardsResults; }
    /** Getter for face results. */
    LiveData<List<ChallengeResult>> getFacesResults() { return mFacesResults; }
    /** Getter for number results. */
    LiveData<List<ChallengeResult>> getNumbersResults() { return mNumbersResults; }
    /** Getter for word results. */
    LiveData<List<ChallengeResult>> getWordsResults() { return mWordsResults; }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    /** Insert new result into the DB. */
    void insert(Result result) {
        ResultRoomDatabase.databaseWriteExecutor.execute(() -> mResultDao.insert(result));
    }
    /** Getter for all today's results. */
    Result getTodayResult(@NotNull String type){
        return mResultDao.getTodayResult(type).getValue();
    }
    /** Update today's result. */
    void update(@NotNull String type, int score, int total){
        ResultRoomDatabase.databaseWriteExecutor.execute(
                () -> mResultDao.updateTodayScore(type, score, total));
    }
    /** Method pruning all redundant today's results. */
    void leaveOnlyOneRecordOfTheDay(@NotNull String type){
        ResultRoomDatabase.databaseWriteExecutor.execute(
                () -> mResultDao.leaveOnlyOneRecordOfTheDay(type));
    }
    /** Prune empty records. */
    void pruneEmptyRows(@NotNull String type){
        ResultRoomDatabase.databaseWriteExecutor.execute(mResultDao::pruneEmptyRows);
    }
}
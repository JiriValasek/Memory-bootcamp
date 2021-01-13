package com.example.memorybootcamp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

class ResultRepository {

    private ResultDao mResultDao;
    private LiveData<List<ScoreType>> mBestResults;
    private LiveData<List<ScoreType>> mWeeksBestResults;
    private LiveData<List<ChallengeResult>> mBinaryResults;
    private LiveData<List<ChallengeResult>> mCardsResults;
    private LiveData<List<ChallengeResult>> mFacesResults;
    private LiveData<List<ChallengeResult>> mNumbersResults;
    private LiveData<List<ChallengeResult>> mWordsResults;

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
    LiveData<List<ScoreType>> getBestResults() {
        return mBestResults;
    }
    LiveData<List<ScoreType>> getWeeksBestResults() {
        return mWeeksBestResults;
    }
    LiveData<List<ChallengeResult>> getBinaryResults() {
        return mBinaryResults;
    }
    LiveData<List<ChallengeResult>> getCardsResults() {
        return mCardsResults;
    }
    LiveData<List<ChallengeResult>> getFacesResults() {
        return mFacesResults;
    }
    LiveData<List<ChallengeResult>> getNumbersResults() {
        return mNumbersResults;
    }
    LiveData<List<ChallengeResult>> getWordsResults() {
        return mWordsResults;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Result result) {
        ResultRoomDatabase.databaseWriteExecutor.execute(() -> mResultDao.insert(result));
    }

    void update(@NotNull String type, int score){
        ResultRoomDatabase.databaseWriteExecutor.execute(() -> mResultDao.updateTodayScore(type, score));
    }
}
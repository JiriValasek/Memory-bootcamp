package com.example.memorybootcamp.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/** View model retaining values */
public class ResultViewModel extends AndroidViewModel {

    /** Repository object interacting with DB DAO. */
    private final ResultRepository mRepository;
    /** All-time best results. */
    private final LiveData<List<ScoreType>> mBestResults;
    /** Week's best results. */
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
    public ResultViewModel (Application application) {
        super(application);
        mRepository = new ResultRepository(application);
        mBestResults = mRepository.getBestResults();
        mWeeksBestResults = mRepository.getWeeksBestResults();
        mBinaryResults = mRepository.getBinaryResults();
        mCardsResults = mRepository.getCardsResults();
        mFacesResults = mRepository.getFacesResults();
        mNumbersResults = mRepository.getNumbersResults();
        mWordsResults = mRepository.getWordsResults();
    }

    /** Getter for all-time best results. */
    public LiveData<List<ScoreType>> getBestResults() { return mBestResults; }
    /** Getter for week's best results. */
    public LiveData<List<ScoreType>> getWeeksBestResults() { return mWeeksBestResults; }
    /** Getter for binary results. */
    public LiveData<List<ChallengeResult>> getBinaryResults() { return mBinaryResults;}
    /** Getter for card results. */
    public LiveData<List<ChallengeResult>> getCardsResults() { return mCardsResults;}
    /** Getter for face results. */
    public LiveData<List<ChallengeResult>> getFacesResults() { return mFacesResults;}
    /** Getter for number results. */
    public LiveData<List<ChallengeResult>> getNumbersResults() { return mNumbersResults;}
    /** Getter for word results. */
    public LiveData<List<ChallengeResult>> getWordsResults() { return mWordsResults;}

    /** Insert new results into the DB. */
    public void insert(Result result) { mRepository.insert(result); }
    /** Update today's result. */
    public void update(@NotNull String type, int score, int total) {
        mRepository.update(type,score,total); }
    /** Prune empty records. */
    public void leaveOnlyOneBestOfTheDay(@NotNull String type){
        mRepository.leaveOnlyOneRecordOfTheDay(type);
    }
}

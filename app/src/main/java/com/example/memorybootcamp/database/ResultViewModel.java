package com.example.memorybootcamp.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ResultViewModel extends AndroidViewModel {

    private final ResultRepository mRepository;

    private final LiveData<List<ScoreType>> mBestResults;
    private final LiveData<List<ScoreType>> mWeeksBestResults;
    private final LiveData<List<ChallengeResult>> mBinaryResults;
    private final LiveData<List<ChallengeResult>> mCardsResults;
    private final LiveData<List<ChallengeResult>> mFacesResults;
    private final LiveData<List<ChallengeResult>> mNumbersResults;
    private final LiveData<List<ChallengeResult>> mWordsResults;

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

    public LiveData<List<ScoreType>> getBestResults() { return mBestResults; }
    public LiveData<List<ScoreType>> getWeeksBestResults() { return mWeeksBestResults; }
    public LiveData<List<ChallengeResult>> getBinaryResults() { return mBinaryResults;}
    public LiveData<List<ChallengeResult>> getCardsResults() { return mCardsResults;}
    public LiveData<List<ChallengeResult>> getFacesResults() { return mFacesResults;}
    public LiveData<List<ChallengeResult>> getNumbersResults() { return mNumbersResults;}
    public LiveData<List<ChallengeResult>> getWordsResults() { return mWordsResults;}

    public void insert(Result result) { mRepository.insert(result); }

    public void update(@NotNull String type, int score) { mRepository.update(type,score); }
}

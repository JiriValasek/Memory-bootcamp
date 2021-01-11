package com.example.memorybootcamp.ui.challenges.cards;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.charts.ProgressLineChart;
import com.example.memorybootcamp.ui.challenges.ChallengeViewModel;
import com.github.mikephil.charting.charts.LineChart;

public class CardsFragment extends Fragment {

    private ChallengeViewModel viewModel;
    private ProgressLineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ChallengeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_challenge, container, false);
        final TextView textView = root.findViewById(R.id.text_header);
        //challengeIntroViewModel.setHeader(getActivity().getString(R.string.cards_challenge_header));
        viewModel.getHeader().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LineChart lineChart = getActivity().findViewById(R.id.progress_chart);
        chart = new ProgressLineChart(lineChart);
        viewModel.getScores().observe(getViewLifecycleOwner(), floats -> {
            chart.updateValues(floats[0], floats[1]);
            chart.updateColors();
        });

        float[] day = {0,3,5,30,31,32,35};
        float[] score = {10,5,9,20,8,20,40};
        float[][] scores = {day, score};
        //challengeIntroViewModel.setScores(scores);
    }
}

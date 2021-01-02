package com.example.memorybootcamp.ui.home;

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
import com.example.memorybootcamp.charts.ProgressRadarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

//TODO - add more graphs, setup graphs to show current results from SharedPreferences & persistent files

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ProgressRadarChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.setHeader(getActivity().getString(R.string.home_page_header));
        homeViewModel.getHeader().observe(getViewLifecycleOwner(), textView::setText);

        FloatingActionButton fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.VISIBLE) fab.setVisibility(View.VISIBLE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadarChart radarChart = getActivity().findViewById(R.id.home_chart);
        chart = new ProgressRadarChart(radarChart);
        homeViewModel.getScores().observe(getViewLifecycleOwner(), floats -> {
            chart.updateValues(floats[0], floats[1]);
            chart.updateColors();
        });

        float[] bestScores = {3, 10, 60, 20, 40};
        float[] weeksScores = {2, 9, 60, 20, 20};
        float[][] scores = {bestScores,weeksScores};
        homeViewModel.setScores(scores);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton fab =  (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.GONE) fab.setVisibility(View.GONE);
        this.chart = null;
    }
}
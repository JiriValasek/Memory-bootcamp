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
import com.example.memorybootcamp.database.ResultViewModel;
import com.github.mikephil.charting.charts.RadarChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ResultViewModel mResultViewModel;
    private ProgressRadarChart chart;
    private final int NUMBER_OF_TYPES = 5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.setHeader(getActivity().getString(R.string.home_page_header));
        homeViewModel.getHeader().observe(getViewLifecycleOwner(), textView::setText);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.VISIBLE) fab.setVisibility(View.VISIBLE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadarChart radarChart = getActivity().findViewById(R.id.home_chart);
        chart = new ProgressRadarChart(radarChart);
        mResultViewModel.getBestResults().observe(getViewLifecycleOwner(),
                bestResults -> chart.updateBests(bestResults));
        mResultViewModel.getWeeksBestResults().observe(getViewLifecycleOwner(),
                weeksBestResults -> chart.updateWeeksBests(weeksBestResults));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.GONE) fab.setVisibility(View.GONE);
        this.chart = null;
    }
}
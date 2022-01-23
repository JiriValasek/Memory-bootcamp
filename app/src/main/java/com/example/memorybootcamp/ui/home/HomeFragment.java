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

/** Home fragment showing all progress. */
public class HomeFragment extends Fragment {

    /** View-binding to a view data. */
    private HomeViewModel viewModel;
    /** View-binding to the database. */
    private ResultViewModel mResultViewModel;
    /** Chart showing progress. */
    private ProgressRadarChart chart;

    /** "onCreateView" setting up fragment, and showing floating action button. */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // adding view binding
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // retrieving view binding for graph and database
        mResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // setting up description for user
        final TextView textView = root.findViewById(R.id.text_home);
        viewModel.setHeader(requireActivity().getString(R.string.home_page_header));
        viewModel.getHeader().observe(getViewLifecycleOwner(), textView::setText);
        // hiding fab
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.VISIBLE) fab.setVisibility(View.VISIBLE);

        return root;
    }

    /** "onViewCreated" setting up progress chart. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RadarChart radarChart = requireActivity().findViewById(R.id.home_chart);
        chart = new ProgressRadarChart(radarChart);
        mResultViewModel.getBestResults().observe(getViewLifecycleOwner(),
                bestResults -> chart.updateBests(bestResults));
        mResultViewModel.getWeeksBestResults().observe(getViewLifecycleOwner(),
                weeksBestResults -> chart.updateWeeksBests(weeksBestResults));
    }

    /** "onDestroyView" hiding FAB. */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // show fab
        FloatingActionButton fab = requireActivity().findViewById(R.id.fab);
        if (fab.getVisibility() != View.GONE) fab.setVisibility(View.GONE);
    }
}
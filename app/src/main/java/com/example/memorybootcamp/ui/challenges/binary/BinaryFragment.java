package com.example.memorybootcamp.ui.challenges.binary;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.charts.ProgressLineChart;
import com.example.memorybootcamp.ui.challenges.ChallengeIntroViewModel;
import com.github.mikephil.charting.charts.LineChart;

public class BinaryFragment extends Fragment {

    private ChallengeIntroViewModel challengeIntroViewModel;
    private ProgressLineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        challengeIntroViewModel = new ViewModelProvider(this).get(ChallengeIntroViewModel.class);
        View root = inflater.inflate(R.layout.fragment_challenge, container, false);
        TextView headerTextView = root.findViewById(R.id.text_header);
        challengeIntroViewModel.setHeader(getActivity().getString(R.string.binary_challenge_header));
        challengeIntroViewModel.getHeader().observe(getViewLifecycleOwner(), headerTextView::setText);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String size = sharedPreferences.getString(getString(R.string.binary_size_key),"");
        String time = sharedPreferences.getString(getString(R.string.binary_time_key),"");
        if (size.equals("") || time.equals("")) {
            challengeIntroViewModel.setDescription("Unable to load binary settings.");
            root.findViewById(R.id.start_challenge_button).setEnabled(false);
        } else {
            float parsedTime = Float.parseFloat(time);
            int hours = Math.round(parsedTime / 60);
            int minutes = Math.round(parsedTime % 60);
            int seconds = (int) Math.round((parsedTime - Math.floor(parsedTime)) * 60);
            String description = "You have ";
            description += hours > 0 ? hours + "h " : "";
            description += minutes > 0 ? minutes + "m " : "";
            description += seconds > 0 ? seconds + "s " : "";
            description += "to memorize " + size + "bits.";
            challengeIntroViewModel.setDescription(description);
            root.findViewById(R.id.start_challenge_button).setEnabled(true);
        }
        TextView descriptionTextView = root.findViewById(R.id.challenge_description);
        challengeIntroViewModel.getDescription().observe(getViewLifecycleOwner(), descriptionTextView::setText);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        LinearLayout startButton = getActivity().findViewById(R.id.start_challenge_button);
        startButton.setOnClickListener(v -> {
            NavDirections action = BinaryFragmentDirections.actionNavBinaryToBinaryTrainingFragment();
            Navigation.findNavController(getView()).navigate(action);
        });

        // Setup graph
        LineChart lineChart = getActivity().findViewById(R.id.progress_chart);
        chart = new ProgressLineChart(lineChart);
        challengeIntroViewModel.getScores().observe(getViewLifecycleOwner(), floats -> {
            chart.updateValues(floats[0], floats[1]);
            chart.updateColors();
        });

        // Update graph data
        float[] day = {0,3,5,30,31,32,35};
        float[] score = {10,5,9,20,8,20,40};
        float[][] scores = {day, score};
        challengeIntroViewModel.setScores(scores);
    }

}
package com.example.memorybootcamp.ui.challenges.numbers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.charts.ProgressLineChart;
import com.example.memorybootcamp.database.ResultViewModel;
import com.example.memorybootcamp.databinding.FragmentChallengeBinding;
import com.example.memorybootcamp.ui.challenges.ChallengeViewModel;
import com.github.mikephil.charting.charts.LineChart;

public class NumbersFragment extends Fragment {

    private FragmentChallengeBinding binding;
    private ChallengeViewModel viewModel;
    private ResultViewModel mResultViewModel;
    private ProgressLineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChallengeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ChallengeViewModel.class);
        viewModel.setHeader(getActivity().getString(R.string.numbers_challenge_header));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        mResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String size = sharedPreferences.getString(getString(R.string.numbers_size_key), getString(R.string.numbers_size_default));
        String time = sharedPreferences.getString(getString(R.string.numbers_time_key), getString(R.string.numbers_size_default));

        viewModel.setDescription(getDescription(time, size));
        viewModel.setStartChallengeAllowed(true);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        binding.startChallengeButton.setOnClickListener(v -> {
            NavDirections action = NumbersFragmentDirections.actionNumbersToNumbersTraining();
            Navigation.findNavController(getView()).navigate(action);
        });

        // Setup graph from mResultViewModel
        LineChart lineChart = binding.progressChart;
        chart = new ProgressLineChart(lineChart);
        mResultViewModel.getNumbersResults().observe(getViewLifecycleOwner(),
                results -> chart.updateValues(results));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getDescription(String time, String size){
        // Get challenge description
        float parsedTime = Float.parseFloat(time);
        int hours = Math.round(parsedTime / 60);
        int minutes = Math.round(parsedTime % 60);
        int seconds = (int) Math.round((parsedTime - Math.floor(parsedTime)) * 60);
        String description = "You have ";
        description += hours > 0 ? hours + "h " : "";
        description += minutes > 0 ? minutes + "m " : "";
        description += seconds > 0 ? seconds + "s " : "";
        description += "to memorize " + size + " numbers.";
        return description;
    }
}

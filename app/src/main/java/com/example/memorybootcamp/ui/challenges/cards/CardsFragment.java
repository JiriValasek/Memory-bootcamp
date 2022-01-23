package com.example.memorybootcamp.ui.challenges.cards;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.memorybootcamp.database.ResultViewModel;
import com.example.memorybootcamp.databinding.FragmentChallengeBinding;
import com.example.memorybootcamp.generators.CardsGenerator;
import com.example.memorybootcamp.ui.challenges.ChallengeViewModel;
import com.example.memorybootcamp.ui.challenges.cards.CardsFragmentDirections;
import com.github.mikephil.charting.charts.LineChart;

/** Introduction fragment for cards challenge. */
public class CardsFragment extends Fragment {

    /** Data-binding to a fragment. */
    private FragmentChallengeBinding binding;
    /** View-binding to a view data. */
    private ChallengeViewModel viewModel;
    /** View-binding to the database. */
    private ResultViewModel mResultViewModel;
    /** Chart showing progress. */
    private ProgressLineChart chart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // adding view binding
        viewModel = new ViewModelProvider(this).get(ChallengeViewModel.class);
        // seting data binding
        binding = FragmentChallengeBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        // retrieving view binding for graph and database
        mResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        // size and tiem preferences
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());
        String size = sharedPreferences.getString(getString(R.string.cards_size_key), getString(R.string.cards_size_default));
        String time = sharedPreferences.getString(getString(R.string.cards_time_key), getString(R.string.cards_size_default));
        // setting up the view
        viewModel.setHeader(requireActivity().getString(R.string.cards_challenge_header));
        viewModel.setDescription(getDescription(time, size));
        viewModel.setStartChallengeAllowed(true);
        return binding.getRoot();
    }

    /** "onViewCreated" generating cards, setting up the graph and button to start the challenge. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        binding.startChallengeButton.setOnClickListener(v -> {
            CardsFragmentDirections.ActionCardsToCardsTraining action =
                    CardsFragmentDirections.actionCardsToCardsTraining();
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(requireContext());
            String size = sharedPreferences.getString(getString(R.string.cards_size_key), getString(R.string.cards_size_default));
            CardsGenerator gen = new CardsGenerator();
            Cards taskContent = gen.generateSequence(Integer.parseInt(size));
            action.setTaskContent(taskContent);
            Navigation.findNavController(requireView()).navigate(action);
        });
        // Setup graph from mResultViewModel
        LineChart lineChart = binding.progressChart;
        chart = new ProgressLineChart(lineChart);
        mResultViewModel.getCardsResults().observe(getViewLifecycleOwner(),
                results -> chart.updateValues(results));
    }

    /** "onDestroyView" destroys binding as well. */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /** Method generating description string from preferences. */
    public String getDescription(String time, String size){
        // Get challenge description
        float parsedTime = Float.parseFloat(time);
        int hours = (int) Math.floor(((float) parsedTime) / 60);
        int minutes = (int) Math.floor(((float) parsedTime) % 60);
        int seconds = (int) Math.round((parsedTime - Math.floor(parsedTime)) * 60);
        String description = "You have ";
        description += hours > 0 ? hours + "h " : "";
        description += minutes > 0 ? minutes + "m " : "";
        description += seconds > 0 ? seconds + "s " : "";
        description += "to memorize " + size + " cars.";
        return description;
    }
}

package com.example.memorybootcamp.ui.challenges.faces;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.charts.ProgressLineChart;
import com.example.memorybootcamp.databinding.FragmentChallengeBinding;
import com.example.memorybootcamp.web.faceretrival.FaceRetriever;
import com.example.memorybootcamp.web.faceretrival.Faces;
import com.github.mikephil.charting.charts.LineChart;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacesFragment extends Fragment implements Callback<Faces> {
    private FragmentChallengeBinding binding;
    private FacesViewModel viewModel;
    private ProgressLineChart chart;
    private static final int TOTAL_RETRIES = 3;
    private int retryCount = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentChallengeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(FacesViewModel.class);
        viewModel.setHeader(requireActivity().getString(R.string.faces_challenge_header));
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String size = sharedPreferences.getString(getString(R.string.faces_size_key), getString(R.string.faces_size_default));

        FaceRetriever faceRetriever = new FaceRetriever();
        faceRetriever.getFaces(Integer.parseInt(size), this);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        binding.startChallengeButton.setOnClickListener(v -> {
            FacesFragmentDirections.ActionFacesToFacesTraining action =
                    FacesFragmentDirections.actionFacesToFacesTraining();
            action.setTaskContent(viewModel.getFaces().getValue());
            Navigation.findNavController(requireView()).navigate(action);
        });

        // Setup graph
        LineChart lineChart = binding.progressChart;
        chart = new ProgressLineChart(lineChart);
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
        description += "to memorize " + size + " faces.";
        return description;
    }


    // Callback<Faces> part ------------------------------------------------------------------------

    @Override
    public void onResponse(@NotNull Call<Faces> call,@NotNull Response<Faces> response) {
        if (response.body() != null) {
                Log.d("MEMORYBOOTCAMP", "Faces recieved." );
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.requireContext());
                String size = sharedPreferences.getString(getString(R.string.faces_size_key), getString(R.string.faces_size_default));
                String time = sharedPreferences.getString(getString(R.string.faces_time_key), getString(R.string.faces_size_default));
                viewModel.setDescription(getDescription(time, size));
                viewModel.setFaces(response.body());
                viewModel.setStartChallengeAllowed(true);
        } 
    }

    @Override
    public void onFailure(@NotNull Call<Faces> call, @NotNull Throwable t) {
        Log.d("MEMORYBOOTCAMP", "Image loading failed.");
        if (retryCount++ < TOTAL_RETRIES) {
            Log.v("MEMORYBOOTCAMP", "Retrying... (" + retryCount + " out of " + TOTAL_RETRIES + ")");
            retry(call);
        } else {
            Log.v("MEMORYBOOTCAMP", "Tried " + TOTAL_RETRIES + " times. Giving up.");
            viewModel.setDescription("Unable to load faces,\nplease try again later.");
            viewModel.setStartChallengeAllowed(false);
        }
    }

    private void retry(Call<Faces> call) {
            call.clone().enqueue(this);
        }
}

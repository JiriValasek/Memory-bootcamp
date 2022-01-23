package com.example.memorybootcamp.ui.challenges.faces;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.database.ResultViewModel;
import com.example.memorybootcamp.databinding.FragmentFacesTrainingBinding;
import com.example.memorybootcamp.web.faceretrival.Faces;
import com.example.memorybootcamp.web.faceretrival.Name;
import com.example.memorybootcamp.web.faceretrival.Picture;
import com.example.memorybootcamp.web.faceretrival.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Maybe switch to glide - inspire by tutorials:
 * https://github.com/elodieferrais/SampleApp/
 * https://bumptech.github.io/glide/doc/caching.html
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 *
 * Currently based on:
 * https://www.learningsomethingnew.com/how-to-use-a-recycler-view-to-show-images-from-storage/
 * https://github.com/syonip/android-recycler-example/tree/master/app/src/main/java/com/example/myapplication
 */

public class FacesTrainingFragment extends Fragment {

    private final String RESULTS = "results";
    private final String TASK = "task";
    private final String RECOLLECTION = "recollection";
    private FragmentFacesTrainingBinding binding;
    private FacesTrainingViewModel viewModel;
    private FaceListRecyclerViewAdapter recyclerViewAdapter;
    private DownloadManager downloadManager;
    private BroadcastReceiver onComplete = null;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private NavDirections exitAction;
    private String mode;
    private Faces taskContent, answers;
    private int correctCount;


    // TODO divide name into title, first and last edittexts

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(FacesTrainingViewModel.class);
        binding = FragmentFacesTrainingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        View view;

        // get arguments
        progressBar = binding.getRoot().findViewById(R.id.indeterminateBar);
        mode = FacesTrainingFragmentArgs.fromBundle(getArguments()).getMode();
        switch (mode) {
            case TASK:
                taskContent = FacesTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                setupChallenge(mode.equals(TASK), "Memorize in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                downloadManager = (DownloadManager) requireActivity()
                        .getSystemService(Context.DOWNLOAD_SERVICE);
                onComplete = new DownloadReceiver();
                requireContext().registerReceiver(onComplete,
                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                break;
            case RECOLLECTION:
                taskContent = FacesTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                setupChallenge(mode.equals(TASK), "Answer in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case RESULTS:
                taskContent = FacesTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                answers = FacesTrainingFragmentArgs.fromBundle(getArguments()).getAnswers();
                break;
        }
        setupRecyclerView();

        // disable going back and settings
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setDisplayHomeAsUpEnabled(false);
        view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        binding.endPracticeButton.setOnClickListener(v -> exitFunction());
        if ( mode.equals(TASK)) {
            startDownload();
        } else {
            Log.d("MEMORYBOOTCAMP", "Loading saved images.");
            FaceContent.loadSavedImages(requireContext(),false);
            if (mode.equals(RESULTS)) {
                showResults();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        // return settings button
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.VISIBLE);
        if (timer != null) timer.cancel();
        binding = null;
    }


    @Override
    public void onStop() {
        super.onStop();
        if ( onComplete != null) requireContext().unregisterReceiver(onComplete);
    }

    @Override
    public void onResume() {
        super.onResume();
        if ( onComplete != null) requireContext().registerReceiver(onComplete,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title); // Setting Dialog Title
        alertDialog.setMessage(message); // Setting Dialog Message
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            NavDirections action = FacesTrainingFragmentDirections.actionFacesTrainingToFaces();
            Navigation.findNavController(requireView()).navigate(action);
        });
        // Setting Cancel button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show(); // Showing Alert Message
    }

    private void setupChallenge( boolean fillInTask, String timeOutTitle){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        // Setup timer
        String time;
        if (mode.equals(TASK)) {
            time = sharedPreferences.getString(getString(R.string.faces_time_key), getString(R.string.faces_time_default));
        } else {
            time = sharedPreferences.getString(getString(R.string.faces_answer_key), getString(R.string.faces_answer_default));
        }
        long timeMs = Math.round(Float.parseFloat(time)*60*1000);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        timer = new CountDownTimer( timeMs, 1000) {

            public void onTick(long millisUntilFinished) {
                SpannableString firstPart = new SpannableString(timeOutTitle);
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (60*1000)) % 60;
                long hours = (millisUntilFinished / (60*60*1000));
                String time = String.format(
                        Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
                SpannableString lastPart = new SpannableString(time);
                firstPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(),
                        R.color.white)), 0, firstPart.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(),
                        R.color.red)), 0, lastPart.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar.setTitle(TextUtils.concat(firstPart, lastPart));
            }

            public void onFinish() { exitFunction(); }
        };
        timer.start();
    }

    private void showResults() {
        String itemName;
        Result taskResult = null, answerResult = null;
        correctCount = 0; // correct
        List<FaceContent.FaceItem> items = recyclerViewAdapter.getItems();
        for(FaceContent.FaceItem item : items){
            itemName = FaceContent.getFaceNameFromUri(item.uri);
            for(int i=0; i < taskContent.getResults().size(); i++){
                taskResult = taskContent.getResults().get(i);
                String resultName = taskResult.getName().toString();
                if (itemName.equals(resultName)){
                    break;
                }
            }
            for(int i=0; i < answers.getResults().size(); i++){
                answerResult = answers.getResults().get(i);
                assert taskResult != null;
                if (answerResult.getPicture().equals(taskResult.getPicture())){
                    break;
                }
            }
            assert answerResult != null;
            if (answerResult.getName().toString().equals(taskResult.getName().toString())){
                correctCount++;
            }
            item.faceName = getColoredAnswer(taskResult.getName().toString(),
                    answerResult.getName().toString());
            recyclerViewAdapter.notifyDataSetChanged();
            Log.d("TEST",
                    taskResult.getName().toString() + " -> " + answerResult.getName().toString() );
        }
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Your Results");
        setDescription();
        viewModel.setButtonText("Back to faces stats");

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.last_challenge_key), getString(R.string.challenge_faces));
        editor.apply();
        editor.commit();

        ResultViewModel mResultViewModel = new ViewModelProvider(this)
                .get(ResultViewModel.class);
        mResultViewModel.update("faces", correctCount, taskContent.getResults().size());
        mResultViewModel.leaveOnlyOneBestOfTheDay("faces");
    }

    private SpannableStringBuilder getColoredAnswer(String task, String answer){
        int i;
        boolean correct = true;
        SpannableString letter;
        SpannableStringBuilder evaluatedText = new SpannableStringBuilder("");
        for (i=0; i < task.length(); i++){
            if (i < answer.length()){
                if (answer.charAt(i) == task.charAt(i)){
                    letter = new SpannableString(Character.toString(task.charAt(i)));
                    letter.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    letter = new SpannableString(Character.toString(task.charAt(i)));
                    letter.setSpan(new ForegroundColorSpan(Color.RED), 0, 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    correct = false;
                }
            } else {
                letter = new SpannableString(Character.toString(task.charAt(i)));
                letter.setSpan(new ForegroundColorSpan(Color.GRAY), 0, 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                correct =false;
            }
            evaluatedText.append(letter);
        }
        if (correct) correctCount++;
        return evaluatedText;
    }

    private void redirectBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showAlertDialog(getContext(), "Training in progress",
                        "Do you really wish to stop your Training?");
            }
        };
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), callback);
    }

    private void exitFunction() {
        switch (mode) {
            case TASK:
                exitAction = FacesTrainingFragmentDirections.actionFacesTrainingToWaiting();
                ((FacesTrainingFragmentDirections.ActionFacesTrainingToWaiting) exitAction)
                        .setChallengeType("faces");
                ((FacesTrainingFragmentDirections.ActionFacesTrainingToWaiting) exitAction)
                        .setFacesTaskContent(taskContent);
                break;
            case "recollection":
                exitAction = FacesTrainingFragmentDirections.actionFacesTrainingSelf();
                ((FacesTrainingFragmentDirections.ActionFacesTrainingSelf) exitAction)
                        .setMode(RESULTS);
                ((FacesTrainingFragmentDirections.ActionFacesTrainingSelf) exitAction)
                        .setAnswers(retrieveAnswers());
                ((FacesTrainingFragmentDirections.ActionFacesTrainingSelf) exitAction)
                        .setTaskContent(taskContent);
                break;
            case RESULTS:
                exitAction = FacesTrainingFragmentDirections.actionFacesTrainingToFaces();
                recyclerViewAdapter.clearItems(getContext());
        }
        Navigation.findNavController(requireView()).navigate(exitAction);
    }

    private Faces retrieveAnswers(){
        String title, itemName;
        Result res = null;
        List<FaceContent.FaceItem> items = recyclerViewAdapter.getItems();
        Faces answers = new Faces(new ArrayList<>(), taskContent.getInfo());
        for(FaceContent.FaceItem item : items){
            itemName = FaceContent.getFaceNameFromUri(item.uri);
            for(int i=0; i < taskContent.getResults().size(); i++){
                res = taskContent.getResults().get(i);
                String resultName = res.getName().getTitle() +
                        " " + res.getName().getFirst() + " " +
                        res.getName().getLast();
                if (itemName.equals(resultName)){
                    break;
                }
            }

            title =  item.faceName.toString();
            answers.getResults().add(new Result(
                    res == null? "" : res.getGender(),
                    new Name(title, "", ""),
                    res == null? new Picture() : res.getPicture()));
        }
        return answers;
    }

    private void setDescription(){
        TypedValue typedValue = new TypedValue();
        requireContext().getTheme()
                .resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        String text;
        SpannableStringBuilder description = new SpannableStringBuilder();
        description.append(colorText("These are ", foregroundColor));
        description.append(colorText("correct", Color.BLUE));
        description.append(colorText(" answers, these are ", foregroundColor));
        description.append(colorText("wrong", Color.RED));
        description.append(colorText(" and ", foregroundColor));
        description.append(colorText("unanswered", Color.GRAY));
        if (correctCount != taskContent.getResults().size()) {
            text = ". To have your achievement counted, it must be all correct.";
        } else {
            text = ". All correct, your result is recorded. Congratulations!";
        }
        description.append(colorText(text, foregroundColor));
        viewModel.setDescriptionText(description);
    }

    private SpannableString colorText(String text, @ColorInt int c){
        SpannableString colorText = new SpannableString(text);
        colorText.setSpan(new ForegroundColorSpan(c), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return colorText;
    }

    private void startDownload() {
        for (int i = 0; i < taskContent.getResults().size(); i++) {
            Result res = taskContent.getResults().get(i);
            String name = res.getName().toString();
            Log.d("MEMORYBOOTCAMP", "Downloading " + name);
            requireActivity().runOnUiThread(() -> {
                progressBar.setVisibility(View.VISIBLE);
                FaceContent.downloadImage(downloadManager, getContext(),
                        res.getPicture().getLarge(), name);
            });
        }
    }

    private void setupRecyclerView(){
        // Set the adapter
        if (recyclerViewAdapter == null) {
            View view = binding.getRoot().findViewById(R.id.face_list);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(
                        new FaceListRecyclerViewAdapter(
                                FaceContent.ITEMS, mode.equals("recollection")));
                recyclerViewAdapter = (FaceListRecyclerViewAdapter) recyclerView.getAdapter();
                if (mode.equals(TASK) && recyclerViewAdapter != null) {
                    recyclerViewAdapter.clearItems(getContext());
                }
            }
        }
    }

    public class DownloadReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Log.d("MEMORYBOOTCAMP","Download received.");
            String filePath="";
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(intent.getExtras().getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = downloadManager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    String downloadFileLocalUri = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    filePath = Uri.parse(downloadFileLocalUri).getPath();
                } else if (status == DownloadManager.STATUS_FAILED){
                    downloadFailed();
                }
            }
            c.close();
            recyclerViewAdapter.notifyItemInserted(
                    FaceContent.loadImage(new File(filePath), mode.equals(TASK)));
            progressBar.setVisibility(View.GONE);
        }
    }


    private void downloadFailed() {
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle("Download failed"); // Setting Dialog Title
        alertDialog.setMessage("Unfortunately, download failed, pleas try it later."); // Setting Dialog Message
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", (dialog, which) -> {
            NavDirections action = FacesTrainingFragmentDirections.actionFacesTrainingToFaces();
            Navigation.findNavController(requireView()).navigate(action);
        });
    }

}
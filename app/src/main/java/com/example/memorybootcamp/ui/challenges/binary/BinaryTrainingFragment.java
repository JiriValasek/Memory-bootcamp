package com.example.memorybootcamp.ui.challenges.binary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.database.ResultViewModel;
import com.example.memorybootcamp.databinding.FragmentBinaryTrainingBinding;
import com.example.memorybootcamp.generators.BinaryNumberGenerator;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/*TODO use icons on toolbar and button
https://www.flaticon.com/free-icon/thinking_1491165?term=brain&page=1&position=7&related_item_id=1491165
https://www.flaticon.com/free-icon/idea_1491174?term=brain&page=1&position=34&related_item_id=1491174
https://www.flaticon.com/free-icon/autism_1491171?term=brain&page=1&position=58&related_item_id=1491171
 */

public class BinaryTrainingFragment extends Fragment {

    private FragmentBinaryTrainingBinding binding;
    private BinaryTrainingViewModel viewModel;
    private CountDownTimer timer;
    private NavDirections exitAction;
    private String mode;
    private String[] taskContent, answers;
    private int correctCount;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // adding view binding
        viewModel = new ViewModelProvider(this).get(BinaryTrainingViewModel.class);
        // setting up data binding
        binding = FragmentBinaryTrainingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        // getting arguments
        mode = BinaryTrainingFragmentArgs.fromBundle(getArguments()).getMode();
        prepareTextEdit(mode.equals("recollection"));
        switch (mode) {
            case "task":
                setupChallenge(true, "Memorize in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case "recollection":
                taskContent = BinaryTrainingFragmentArgs.fromBundle(getArguments())
                        .getTaskContent();
                setupChallenge(false, "Answer in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case "results":
                taskContent = BinaryTrainingFragmentArgs.fromBundle(getArguments())
                        .getTaskContent();
                answers = BinaryTrainingFragmentArgs.fromBundle(getArguments()).getAnswers();
                processResults();
                break;
        }

        // disable going back and settings
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setDisplayHomeAsUpEnabled(false);
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Add callback to start practice
        binding.endPracticeButton.setOnClickListener(v -> exitFunction());
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

    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title); // Setting Dialog Title
        alertDialog.setMessage(message); // Setting Dialog Message
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            NavDirections action = BinaryTrainingFragmentDirections.actionBinaryTrainingToBinary();
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

        // Setup challenge
        if (fillInTask) {
            String size = sharedPreferences.getString(getString(R.string.binary_size_key), getString(R.string.binary_size_default));
            BinaryNumberGenerator generator = new BinaryNumberGenerator();
            ArrayList<String> challenge = generator.generateSequence(Integer.parseInt(size));
            StringBuilder challengeText = new StringBuilder();
            for (String s : challenge) {
                challengeText.append(s);
            }
            viewModel.setChallengeText(challengeText.toString());
        }

        // Setup timer
        String time;
        if (mode.equals("task")) {
            time = sharedPreferences.getString(getString(R.string.binary_time_key),
                    getString(R.string.binary_time_default));
        } else {
            time = sharedPreferences.getString(getString(R.string.binary_answer_key),
                    getString(R.string.binary_answer_default));
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
                firstPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.white)), 0, firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastPart.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.red)), 0, lastPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar.setTitle(TextUtils.concat(firstPart, lastPart));
            }

            public void onFinish() { exitFunction(); }
        };
        timer.start();
    }

    /**
     * Method processing and showing results.
     */
    private void processResults() {
        // coloring the answer if it matches with the task i.e. text to remember.
        correctCount = 0;
        SpannableString letter; // letter to be colored and added to the shown text
        SpannableStringBuilder evaluatedText = new SpannableStringBuilder("");
        for (int i=0; i < taskContent[0].length(); i++){
            if (answers[0].length() > i){
                if (answers[0].charAt(i) == taskContent[0].charAt(i)){
                    letter = new SpannableString(Character.toString(taskContent[0].charAt(i)));
                    letter.setSpan(new ForegroundColorSpan(Color.rgb(0,0,255)),
                            0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    correctCount++;
                } else {
                    letter = new SpannableString(Character.toString(taskContent[0].charAt(i)));
                    letter.setSpan(new ForegroundColorSpan(Color.rgb(255,0,0)),
                            0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                letter = new SpannableString(Character.toString(taskContent[0].charAt(i)));
                letter.setSpan(new ForegroundColorSpan(Color.rgb(64,64,64)),
                        0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            evaluatedText.append(letter);
        }
        viewModel.setChallengeText(evaluatedText);
        // setting up toolbar
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Your Results");
        // setting up view description
        setDescription();
        // setting up button text
        viewModel.setButtonText("Back to binary stats");
        // changing last challenge to binary for quick navigation
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.last_challenge_key), getString(R.string.challenge_binary));
        editor.apply();
        editor.commit();
        // updating statistics
        ResultViewModel mResultViewModel = new ViewModelProvider(this)
                .get(ResultViewModel.class);
        mResultViewModel.update("binary", correctCount, taskContent[0].length());
        mResultViewModel.leaveOnlyOneBestOfTheDay("binary");
    }

    /**
     * Prepare challengeText TextEdit to be editable or not according to the current view mode.
     * @param editable - should it be editable (is mode recollection).
     */
    private void prepareTextEdit(boolean editable){
        binding.challengeText.setFocusable(editable);
        binding.challengeText.setCursorVisible(editable);
        if (editable){
            binding.challengeText.setInputType( InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_CLASS_TEXT);
            binding.challengeText.requestFocus();
        }
    }

    /**
     * Method that makes user confirm going back to challenge overview.
     */
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

    /**
     * Navigation function based on current view mode (state).
     */
    private void exitFunction() {
        // select based on current view mode
        switch (mode) {
            case "task":
                // go to waiting and bring task along to compare it with answers
                exitAction = BinaryTrainingFragmentDirections.actionBinaryTrainingToWaiting();
                ((BinaryTrainingFragmentDirections.ActionBinaryTrainingToWaiting) exitAction)
                        .setChallengeType("binary");
                String[] task = {String.valueOf(binding.challengeText.getText())};
                ((BinaryTrainingFragmentDirections.ActionBinaryTrainingToWaiting) exitAction)
                        .setBinaryTaskContent(task);
                break;
            case "recollection":
                // go to showing results and bring both task and answers along
                exitAction = BinaryTrainingFragmentDirections.actionBinaryTrainingSelf();
                String[] answer = {String.valueOf(binding.challengeText.getText())};
                ((BinaryTrainingFragmentDirections.ActionBinaryTrainingSelf) exitAction)
                        .setMode("results");
                ((BinaryTrainingFragmentDirections.ActionBinaryTrainingSelf) exitAction)
                        .setAnswers(answer);
                ((BinaryTrainingFragmentDirections.ActionBinaryTrainingSelf) exitAction)
                        .setTaskContent(taskContent);
                break;
            case "results":
                // go to overview
                exitAction = BinaryTrainingFragmentDirections.actionBinaryTrainingToBinary();
        }
        Navigation.findNavController(requireView()).navigate(exitAction);
    }

    /**
     * Make a result summary and set it to the view description.
     */
    private void setDescription(){
        TypedValue typedValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int foregroundColor = typedValue.data;
        SpannableStringBuilder description = new SpannableStringBuilder();
        SpannableString text = new SpannableString("These are ");
        text.setSpan(new ForegroundColorSpan(foregroundColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        text = new SpannableString("correct");
        text.setSpan(new ForegroundColorSpan(Color.rgb(0,0,255)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        text = new SpannableString(" answers, these are ");
        text.setSpan(new ForegroundColorSpan(foregroundColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        text = new SpannableString("wrong");
        text.setSpan(new ForegroundColorSpan(Color.rgb(255,0,0)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        text = new SpannableString(" and ");
        text.setSpan(new ForegroundColorSpan(foregroundColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        text = new SpannableString("unanswered");
        text.setSpan(new ForegroundColorSpan(Color.rgb(64,64,64)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        if (correctCount != taskContent[0].length()) {
            text = new SpannableString(". To have your achievement counted, it must be all correct.");
        } else {
            text = new SpannableString(". All correct, your result is recorded. Congratulations!");
        }
        text.setSpan(new ForegroundColorSpan(foregroundColor), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        description.append(text);
        viewModel.setDescriptionText(description);
    }
}
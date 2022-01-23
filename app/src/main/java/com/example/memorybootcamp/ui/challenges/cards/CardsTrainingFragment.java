package com.example.memorybootcamp.ui.challenges.cards;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

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

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.database.ResultViewModel;
import com.example.memorybootcamp.databinding.FragmentCardsTrainingBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Fragment for training card memorization. */
public class CardsTrainingFragment extends Fragment {

    /** Fragment mode for showing results. */
    private static final String RESULTS = "results";
    /** Fragment mode for memorization. */
    private static final String TASK = "task";
    /** Fragment mode for filling in memorized information. */
    private static final String RECOLLECTION = "recollection";

    /** Data binding for the layout. */
    private FragmentCardsTrainingBinding binding;
    /** View model for maintaining data between restarts etc. */
    private CardsTrainingViewModel viewModel;
    /** Static recycler view adapter for showing cards. */
    private CardListRecyclerViewAdapter recyclerViewAdapter;
    /** Count-down timer for memorization and recollection. */
    private CountDownTimer timer;
    /** Action to be executed after time is out. */
    private NavDirections exitAction;
    /** Mode in which the fragment is. */
    private String mode;
    /** Cards shown during the memorization, and recollected answers. */
    private Cards taskContent, answers;
    /** Number of correct results. */
    private int correctCount;

    /**
     * "onCreateView" setting up a fragment, starting timer, changing redirect back,
     * changing button text and hiding settings button.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CTF", "onCreate");
        // adding view binding
        viewModel = new ViewModelProvider(this).get(CardsTrainingViewModel.class);
        // setting up data binding
        binding = FragmentCardsTrainingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        // get arguments from previous fragment
        mode = CardsTrainingFragmentArgs.fromBundle(getArguments()).getMode();
        taskContent = CardsTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
        switch (mode) {
            case TASK:
                setupChallenge("Memorize in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case RECOLLECTION:
                setupChallenge("Answer in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case RESULTS:
                answers = CardsTrainingFragmentArgs.fromBundle(getArguments()).getAnswers();
                break;
        }
        setupRecyclerView();
        // disable going back and settings
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setDisplayHomeAsUpEnabled(false);
        // hide settings button
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);

        if (viewModel.getScreenOrientation().getValue() == -1) {
            viewModel.setScreenOrientation(getResources().getConfiguration().orientation);
        }

        return binding.getRoot();
    }

    /** "onViewCreated" showing cards and changing mode button. */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("CTF", "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        // add callback to end mode of a practice
        binding.endPracticeButton.setOnClickListener(v -> exitFunction());
        // show cards
        if (viewModel.getScreenOrientation().getValue() !=
                getResources().getConfiguration().orientation) {
            viewModel.setScreenOrientation(getResources().getConfiguration().orientation);
        } else {
            if (mode.equals(TASK)) {
                CardContent.loadCards(getActivity(), taskContent, true, true);
            } else if (mode.equals(RESULTS)) {
                CardContent.loadCards(getActivity(), taskContent, false, true);
                showResults();
            } else {
                CardContent.loadCards(getActivity(), taskContent, false, false);
            }
        }
        recyclerViewAdapter.notifyItemRangeInserted(0, taskContent.getCards().size());
        Log.d("CTF", "Orientation - " + getResources().getConfiguration().orientation +
                " - " + viewModel.getScreenOrientation().getValue());
    }

    /** "onPause" stopping timer on to run out when fragment not active. */
    @Override
    public void onPause() {
        Log.d("CTF", "onPause");
        super.onPause();
        // display settings button again
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.VISIBLE);
        // stop timer
        if (timer != null) timer.cancel();
    }

    /** "onResume" restarting timer when fragment became active again. */
    @Override
    public void onResume() {
        Log.d("CTF", "onResume");
        super.onResume();
        // disable settings button again
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);
        // restart timer
        if (viewModel != null && viewModel.getRemainingTime() != null &&
                viewModel.getRemainingTime().getValue() != null) {
            /*if ( viewModel.getRemainingTime().getValue() <= 0) {
                exitFunction();
            } else */
            if (timer != null) timer.cancel();
            if (mode.equals(TASK)) {
                setupChallenge("Memorize in: ");
            } else if (mode.equals(RECOLLECTION)){
                setupChallenge("Answer in: ");
            }
        }
    }

    /** "onDestroyView" showing setting button again and stopping timer. */
    @Override
    public void onDestroyView() {
        Log.d("CTF","onDestroyView");
        super.onDestroyView();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
        // display settings button again
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.VISIBLE);
        // stop timer
        if (timer != null) timer.cancel();
        binding = null;
    }

    /** Method setting up timer according to preferences. */
    private void setupChallenge(String timeOutTitle){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireActivity());

        // Setup timer
        String time;
        if (mode.equals(TASK)) {
            time = sharedPreferences.getString(getString(R.string.cards_time_key), getString(R.string.cards_time_default));
        } else {
            time = sharedPreferences.getString(getString(R.string.cards_answer_key), getString(R.string.cards_answer_default));
        }
        long timeMs;
        if (viewModel.getRemainingTime().getValue() == null) {
            timeMs = Math.round(Float.parseFloat(time)*60*1000);
        } else {
            timeMs = viewModel.getRemainingTime().getValue();
        }

        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        timer = new CountDownTimer( timeMs, 1000) {

            public void onTick(long millisUntilFinished) {
                viewModel.setRemainingTime(millisUntilFinished);
                SpannableString firstPart = new SpannableString(timeOutTitle);
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (60*1000)) % 60;
                long hours = (millisUntilFinished / (60*60*1000));
                String time =
                        String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
                SpannableString lastPart = new SpannableString(time);
                if (isAdded()){
                    firstPart.setSpan(
                            new ForegroundColorSpan(
                                    ContextCompat.getColor(requireActivity(), R.color.white)),
                            0, firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lastPart.setSpan(
                            new ForegroundColorSpan(
                                    ContextCompat.getColor(requireActivity(), R.color.red)),
                            0, lastPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    toolbar.setTitle(TextUtils.concat(firstPart, lastPart));
                }
            }

            public void onFinish() { exitFunction(); }
        };
        timer.start();
    }

    /** Method showing results from memorization practice. */
    private void showResults() {
        Card taskResult, answerResult;
        correctCount = 0; // correct
        List<CardContent.CardItem> items = recyclerViewAdapter.getItems();
        for (int i=0; i < items.size(); i++){
            taskResult = taskContent.getCards().get(i);
            answerResult = answers.getCards().get(i);
            items.get(i).cardName = getColoredAnswer(
                    taskResult.getName(),
                    answerResult.getName());
            recyclerViewAdapter.notifyDataSetChanged();
            Log.d("TEST", taskResult.getName() + " -> " + answerResult.getName() );
        }
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Your Results");
        setDescription();
        viewModel.setButtonText("Back to cards stats");

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.last_challenge_key),
                getString(R.string.challenge_cards));
        editor.apply();
        editor.commit();

        ResultViewModel mResultViewModel = new ViewModelProvider(this)
                .get(ResultViewModel.class);
        mResultViewModel.update("cards", correctCount, taskContent.getCards().size());
        mResultViewModel.leaveOnlyOneBestOfTheDay("cards");
    }

    /** Coloring correct answers, bad answers and no answers. */
    private SpannableStringBuilder getColoredAnswer(String task, String answer){
        SpannableStringBuilder evaluatedText;
        if (answer == null || answer.length() == 0){
            String text = "No answer";
            evaluatedText = new SpannableStringBuilder(text);
            evaluatedText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, text.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (task.equals(answer)){
            evaluatedText = new SpannableStringBuilder(answer);
            evaluatedText.setSpan(new ForegroundColorSpan(Color.BLUE), 0, answer.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            correctCount++;
        } else {
            evaluatedText = new SpannableStringBuilder(answer);
            evaluatedText.setSpan(new ForegroundColorSpan(Color.RED), 0, answer.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return evaluatedText;
    }

    /** Method changing redirect back to show a warning dialog. */
    private void redirectBack() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showAlertDialog(getActivity(), "Training in progress",
                        "Do you really wish to stop your Training?");
            }
        };
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), callback);
    }

    /** Method showing dialog to caution user against returning. */
    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title); // Setting Dialog Title
        alertDialog.setMessage(message); // Setting Dialog Message
        // Setting OK Button
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialog, which) -> {
            NavDirections action = CardsTrainingFragmentDirections.actionCardsTrainingToCards();
            Navigation.findNavController(requireView()).navigate(action);
        });
        // Setting Cancel button
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> alertDialog.dismiss());
        alertDialog.show(); // Showing Alert Message
    }

    /** Method deciding on next fragment according to current mode and user action/timer. */
    private void exitFunction() {
        Log.d("CTF","exitFunction with mode: " + mode);
        switch (mode) {
            case TASK:
                exitAction = CardsTrainingFragmentDirections.actionCardsTrainingToWaiting();
                ((CardsTrainingFragmentDirections.ActionCardsTrainingToWaiting) exitAction)
                        .setMode(RECOLLECTION);
                ((CardsTrainingFragmentDirections.ActionCardsTrainingToWaiting) exitAction)
                        .setChallengeType("cards");
                ((CardsTrainingFragmentDirections.ActionCardsTrainingToWaiting) exitAction)
                        .setCardsTaskContent(taskContent);
                break;
            case RECOLLECTION:
                exitAction = CardsTrainingFragmentDirections.actionCardsTrainingSelf();
                ((CardsTrainingFragmentDirections.ActionCardsTrainingSelf) exitAction)
                        .setMode(RESULTS);
                ((CardsTrainingFragmentDirections.ActionCardsTrainingSelf) exitAction)
                        .setAnswers(retrieveAnswers());
                ((CardsTrainingFragmentDirections.ActionCardsTrainingSelf) exitAction)
                        .setTaskContent(taskContent);
                break;
            case RESULTS:
                exitAction = CardsTrainingFragmentDirections.actionCardsTrainingToCards();
                recyclerViewAdapter.clearItems(getActivity());
        }
        if (isAdded()) {
            Navigation.findNavController(requireView()).navigate(exitAction);
        }
    }

    /** Method retrieving user's answers from recycler view. */
    private Cards retrieveAnswers(){
        List<CardContent.CardItem> items = recyclerViewAdapter.getItems();
        Cards answers = new Cards(new ArrayList<>());
        for(int i=0; i < items.size(); i++){
            Log.d("CTF", "Retrieving " + items.get(i).cardName.toString() + "," +
                    taskContent.getCards().get(i).getPicture());
            answers.getCards().add(
                    new Card(
                            items.get(i).cardName.toString(),
                            taskContent.getCards().get(i).getPicture()
                    )
            );
        }
        return answers;
    }

    /** Method changing descriptions to explain how results are shown. */
    private void setDescription(){
        TypedValue typedValue = new TypedValue();
        requireActivity().getTheme()
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
        if (correctCount != taskContent.getCards().size()) {
            text = ". To have your achievement counted, it must be all correct.";
        } else {
            text = ". All correct, your result is recorded. Congratulations!";
        }
        description.append(colorText(text, foregroundColor));
        viewModel.setDescriptionText(description);
    }

    /** Method coloring text with selected color. */
    private SpannableString colorText(String text, @ColorInt int c){
        SpannableString colorText = new SpannableString(text);
        colorText.setSpan(new ForegroundColorSpan(c), 0, text.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return colorText;
    }

    /** Method setting up recycler view showing cards. */
    private void setupRecyclerView(){
        // Set the adapter
        if (recyclerViewAdapter == null) {
            View view = binding.getRoot().findViewById(R.id.card_list);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(
                        new CardListRecyclerViewAdapter(
                                CardContent.ITEMS, mode.equals("recollection")));
                recyclerViewAdapter = (CardListRecyclerViewAdapter) recyclerView.getAdapter();
                if (mode.equals(TASK) && recyclerViewAdapter != null) {
                    recyclerViewAdapter.clearItems(getActivity());
                }
            }
        }
    }
}
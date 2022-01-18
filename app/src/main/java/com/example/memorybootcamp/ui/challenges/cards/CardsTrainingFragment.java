package com.example.memorybootcamp.ui.challenges.cards;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.memorybootcamp.generators.CardsGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CardsTrainingFragment extends Fragment {

    private final String RESULTS = "results";
    private final String TASK = "task";
    private final String RECOLLECTION = "recollection";
    private FragmentCardsTrainingBinding binding;
    private CardsTrainingViewModel viewModel;
    private CardListRecyclerViewAdapter recyclerViewAdapter;
    private CountDownTimer timer;
    private NavDirections exitAction;
    private String mode;
    private Cards taskContent, answers;
    private int correctCount;


    // TODO divide name into title, first and last edittexts

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(CardsTrainingViewModel.class);
        binding = FragmentCardsTrainingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        View view;

        // get arguments
        mode = CardsTrainingFragmentArgs.fromBundle(getArguments()).getMode();
        switch (mode) {
            case TASK:
                taskContent = CardsTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                setupChallenge(mode.equals(TASK), "Memorize in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case RECOLLECTION:
                taskContent = CardsTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                setupChallenge(mode.equals(TASK), "Answer in: ");
                redirectBack();
                viewModel.setButtonText("I am already finished");
                break;
            case RESULTS:
                taskContent = CardsTrainingFragmentArgs.fromBundle(getArguments()).getTaskContent();
                answers = CardsTrainingFragmentArgs.fromBundle(getArguments()).getAnswers();
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
        if ( !mode.equals(TASK)) {
            Log.d("MEMORYBOOTCAMP", "Loading saved images.");
            CardContent.loadCards(getContext(), taskContent, false);
            if (mode.equals(RESULTS)) {
                showResults();
            }
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
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> alertDialog.dismiss());
        alertDialog.show(); // Showing Alert Message
    }

    private void setupChallenge( boolean fillInTask, String timeOutTitle){

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());

        // Setup challenge
        if (fillInTask) {
            String size = sharedPreferences.getString(getString(R.string.cards_size_key), getString(R.string.cards_size_default));
            CardsGenerator generator = new CardsGenerator();
            Cards challenge = generator.generateSequence(Integer.parseInt(size));
            CardContent.loadCards(this.getContext(), challenge, true);
        }

        // Setup timer
        String time;
        if (mode.equals(TASK)) {
            time = sharedPreferences.getString(getString(R.string.cards_time_key), getString(R.string.cards_time_default));
        } else {
            time = sharedPreferences.getString(getString(R.string.cards_answer_key), getString(R.string.cards_answer_default));
        }
        long timeMs = Math.round(Float.parseFloat(time)*60*1000);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        timer = new CountDownTimer( timeMs, 1000) {

            public void onTick(long millisUntilFinished) {
                SpannableString firstPart = new SpannableString(timeOutTitle);
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (60*1000)) % 60;
                long hours = (millisUntilFinished / (60*60*1000));
                String time =
                        String.format(Locale.US,"%02d:%02d:%02d", hours, minutes, seconds);
                SpannableString lastPart = new SpannableString(time);
                firstPart.setSpan(
                        new ForegroundColorSpan(
                                ContextCompat.getColor(requireContext(), R.color.white)),
                        0, firstPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lastPart.setSpan(
                        new ForegroundColorSpan(
                                ContextCompat.getColor(requireContext(), R.color.red)),
                        0, lastPart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                toolbar.setTitle(TextUtils.concat(firstPart, lastPart));
            }

            public void onFinish() { exitFunction(); }
        };
        timer.start();
    }

    private void showResults() {
        String itemName;
        Card taskResult = null, answerResult = null;
        correctCount = 0; // correct
        List<CardContent.CardItem> items = recyclerViewAdapter.getItems();
        for(CardContent.CardItem item : items){
            itemName = item.cardName.toString();
            for(int i=0; i < taskContent.getCards().size(); i++){
                taskResult = taskContent.getCards().get(i);
                String resultName = taskResult.getName();
                if (itemName.equals(resultName)){
                    break;
                }
            }
            for(int i=0; i < answers.getCards().size(); i++){
                answerResult = answers.getCards().get(i);
                if (answerResult.getPicture() == taskResult.getPicture()){
                    break;
                }
            }
            if (answerResult.getName().equals(taskResult.getName())){
                correctCount++;
            }
            item.cardName = getColoredAnswer(taskResult.getName(),
                    answerResult.getName());
            recyclerViewAdapter.notifyDataSetChanged();
            Log.d("TEST", taskResult.getName() + " -> " + answerResult.getName() );
        }
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Your Results");
        setDescription();
        viewModel.setButtonText("Back to cards stats");

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(requireContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.last_challenge_key), getString(R.string.challenge_cards));
        editor.apply();
        editor.commit();

        ResultViewModel mResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        mResultViewModel.update("cards", correctCount, taskContent.getCards().size());
        mResultViewModel.leaveOnlyOneBestOfTheDay("cards");
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
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void exitFunction() {
        switch (mode) {
            case TASK:
                exitAction = CardsTrainingFragmentDirections.actionCardsTrainingToWaiting();
                ((CardsTrainingFragmentDirections.ActionCardsTrainingToWaiting) exitAction)
                        .setChallengeType("cards");
                ((CardsTrainingFragmentDirections.ActionCardsTrainingToWaiting) exitAction)
                        .setCardsTaskContent(taskContent);
                break;
            case "recollection":
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
                recyclerViewAdapter.clearItems(getContext());
        }
        Navigation.findNavController(requireView()).navigate(exitAction);
    }

    private Cards retrieveAnswers(){
        String title, itemName;
        String[] guessedName;
        Card res = null;
        List<CardContent.CardItem> items = recyclerViewAdapter.getItems();
        Cards answers = new Cards(new ArrayList<>());
        for(CardContent.CardItem item : items){
            itemName = item.cardName.toString();
            for(int i=0; i < taskContent.getCards().size(); i++){
                res = taskContent.getCards().get(i);
                String resultName = res.getName();
                if (itemName.equals(resultName)){
                    break;
                }
            }

            title =  item.cardName.toString();
            answers.getCards().add(new Card(res.getName(),res.getPicture()));
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
        if (correctCount != taskContent.getCards().size()) {
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

    private void setupRecyclerView(){
        // Set the adapter
        if (recyclerViewAdapter == null) {
            View view = binding.getRoot().findViewById(R.id.face_list);
            if (view instanceof RecyclerView) {
                Context context = view.getContext();
                RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(
                        new CardListRecyclerViewAdapter(
                                CardContent.ITEMS, mode.equals("recollection")));
                recyclerViewAdapter = (CardListRecyclerViewAdapter) recyclerView.getAdapter();
                if (mode.equals(TASK)) {
                    recyclerViewAdapter.clearItems(getContext());
                }
            }
        }
    }
}
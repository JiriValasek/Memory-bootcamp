package com.example.memorybootcamp.ui.settings;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorybootcamp.R;
import com.example.memorybootcamp.ui.challenges.cards.CardListRecyclerViewAdapter;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

/** Fragment setting preferences for challenges. */
public class SettingsFragment extends PreferenceFragmentCompat {

    /** "onCreateView" hiding settings button. */
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /** "onViewStateRestored" hiding settings button. */
    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.GONE);
    }

    /** "onCreateView" showing settings button. */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = requireActivity().findViewById(R.id.action_settings);
        if (view != null) view.setVisibility(View.VISIBLE);
    }

    /** "onCreatePreferences" setting up button for open-source licenses. */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        prepareChallengePreferences();
        Preference button = Objects.requireNonNull(findPreference(getString(R.string.os_key)));
        button.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getContext(), OssLicensesMenuActivity.class);
            startActivity(intent);
            return true;
        });
    }

    /** Method preparing challenge preferences for user. */
    private void prepareChallengePreferences(){
        int[] timeKeys = {R.string.binary_time_key, R.string.faces_time_key, R.string.numbers_time_key, R.string.cards_time_key};
        int[] waitKeys = {R.string.binary_wait_key, R.string.faces_wait_key, R.string.numbers_wait_key, R.string.cards_wait_key};
        int[] answerKeys = {R.string.binary_answer_key, R.string.faces_answer_key, R.string.numbers_answer_key, R.string.cards_answer_key};
        int[] sizeKeys = {R.string.binary_size_key, R.string.faces_size_key, R.string.numbers_size_key, R.string.cards_size_key};
        String[] sizeSuffices = {"bits", "faces", "numbers", "cards"};

        for (int i = 0; i < timeKeys.length; i++) {
            prepareSize(sizeKeys[i], sizeSuffices[i] );
            prepareTime(timeKeys[i]);
            prepareWait(waitKeys[i]);
            prepareAnswer(answerKeys[i]);
        }
    }

    /** Method preparing buttons setting challenge sizes. */
    private void prepareSize(int sizeKeyId, String suffix){
        EditTextPreference editTextPreference = getPreferenceManager().findPreference(getString(sizeKeyId));
        editTextPreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
        editTextPreference.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    if (newValue.equals("")){
                        Toast.makeText(getContext(),
                                "No number inputed, returning to the previous value.",
                                Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        float size = Float.parseFloat((String) newValue);
                        if (size == 0){
                            Toast.makeText(getContext(),
                                    "Zero value is not allowed.",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        preference.setSummary(newValue + " " + suffix);
                        return true;
                    }
                });
        editTextPreference.setSummary(editTextPreference.getText() + " " + suffix);
    }


    /** Method preparing buttons setting time allowed for memorization. */
    private void prepareTime(int timeKeyId){
        EditTextPreference editTextPreference =
                Objects.requireNonNull(getPreferenceManager().findPreference(getString(timeKeyId)));
        setupTimePreference(editTextPreference, "Time to memorize (minutes)");
    }

    /** Method preparing buttons setting time between memorization an recollection. */
    private void prepareWait(int waitKeyId){
        EditTextPreference editTextPreference =
                Objects.requireNonNull(getPreferenceManager().findPreference(getString(waitKeyId)));
        setupTimePreference(editTextPreference, "Time before recollection (minutes)");
    }

    /** Method preparing buttons setting time for recollection. */
    private void prepareAnswer(int answerKeyId){
        EditTextPreference editTextPreference =
                Objects.requireNonNull(
                        getPreferenceManager().findPreference(getString(answerKeyId)));
        setupTimePreference(editTextPreference, "Time to answer (minutes)");
    }

    /** Method setting up buttons for all time related preferences. */
    private void setupTimePreference(EditTextPreference timePreference, String title){
        timePreference.setDialogTitle(title);
        timePreference.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL));
        timePreference.setOnPreferenceChangeListener(
                (preference, newValue) -> {
                    if (newValue.equals("")){
                        Toast.makeText(getContext(),
                                "No number inputed, returning to the previous value.",
                                Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        float time = Float.parseFloat((String) newValue);
                        if (time == 0){
                            Toast.makeText(getContext(),
                                    "Zero value is not allowed.",
                                    Toast.LENGTH_LONG).show();
                            return false;
                        }
                        int[] parsedTime = floatToTime(time);
                        preference.setSummary(
                                String.format(Locale.US,"%02d:%02d:%02d",
                                        parsedTime[0],
                                        parsedTime[1],
                                        parsedTime[2]));
                        return true;
                    }
                });

        float time = Float.parseFloat(timePreference.getText());
        int[] parsedTime = floatToTime(time);
        String timeString = String.format(Locale.US,"%02d:%02d:%02d", parsedTime[0], parsedTime[1],
                parsedTime[2]);
        timePreference.setSummary(timeString);
    }

    /** Method converting time in minutes to regular time format. */
    private int[] floatToTime(float time){
        int seconds = (int) Math.round((time - Math.floor(time)) * 60);
        int minutes = (int) Math.floor(time) % 60;
        int hours = (int) Math.floor(time - minutes) / 60;
        return new int[]{hours, minutes, seconds};
    }

}
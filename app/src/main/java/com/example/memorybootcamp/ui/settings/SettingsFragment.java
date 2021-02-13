package com.example.memorybootcamp.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Toast;

import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.memorybootcamp.R;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.Locale;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        prepareChallengePreferences();
        Preference button = findPreference(getString(R.string.os_key));
        button.setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getContext(), OssLicensesMenuActivity.class);
            startActivity(intent);
            return true;
        });
    }

    private void prepareChallengePreferences(){
        int[] timeKeys = {R.string.binary_time_key, R.string.faces_time_key, R.string.numbers_time_key};
        int[] waitKeys = {R.string.binary_wait_key, R.string.faces_wait_key, R.string.numbers_wait_key};
        int[] answerKeys = {R.string.binary_answer_key, R.string.faces_answer_key, R.string.numbers_answer_key};
        int[] sizeKeys = {R.string.binary_size_key, R.string.faces_size_key, R.string.numbers_size_key};
        String[] sizeSuffices = {" bits", " faces", "numbers"};

        for (int i = 0; i < timeKeys.length; i++) {
            prepareSize(sizeKeys[i], sizeSuffices[i] );
            prepareBinaryTime(timeKeys[i]);
            prepareBinaryWait(waitKeys[i]);
            prepareBinaryAnswer(answerKeys[i]);
        }
    }

    private void prepareSize(int sizeKeyId, String suffix){
        EditTextPreference editTextPreference =
                getPreferenceManager().findPreference(getString(sizeKeyId));
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
                        preference.setSummary(newValue + suffix);
                        return true;
                    }
                });
        editTextPreference.setSummary(editTextPreference.getText() + suffix);
    }


    private void prepareBinaryTime(int timeKeyId){
        EditTextPreference editTextPreference =
                getPreferenceManager().findPreference(getString(timeKeyId));
        setupTimePreference(editTextPreference, "Time to memorize (minutes)");
    }

    private void prepareBinaryWait(int waitKeyId){
        EditTextPreference editTextPreference =
                getPreferenceManager().findPreference(getString(waitKeyId));
        setupTimePreference(editTextPreference, "Time before recollection (minutes)");
    }

    private void prepareBinaryAnswer(int answerKeyId){
        EditTextPreference editTextPreference =
                getPreferenceManager().findPreference(getString(answerKeyId));
        setupTimePreference(editTextPreference, "Time to answer (minutes)");
    }

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

    private int[] floatToTime(float time){
        int seconds = (int) Math.round((time - Math.floor(time)) * 60);
        int minutes = (int) Math.floor(time) % 60;
        int hours = (int) Math.floor(time - minutes) / 60;
        return new int[]{hours, minutes, seconds};
    }

}
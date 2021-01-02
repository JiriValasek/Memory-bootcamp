package com.example.memorybootcamp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        /*@Override
        public boolean onPreferenceTreeClick(Preference preference){
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.main_preferences_key),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            if (preference instanceof SwitchPreferenceCompat) {
                SwitchPreferenceCompat switchPref = (SwitchPreferenceCompat) preference;
                editor.putBoolean(preference.getKey(), switchPref.isChecked());
                editor.apply();
                editor.commit();
            }
            else if (preference instanceof ListPreference) {
                ListPreference listPref = (ListPreference) preference;
                editor.putString(preference.getKey(), listPref.getValue());
                editor.apply();
                editor.commit();

            }
            return true;
        }*/
    }

    public boolean onOSNoticesClicked(Preference preference){
        startActivity(new Intent(this, OssLicensesMenuActivity.class));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

}
package com.example.memorybootcamp;

import android.app.UiModeManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.memorybootcamp.ui.challenges.binary.BinaryFragment;
import com.example.memorybootcamp.ui.challenges.binary.BinaryFragmentDirections;
import com.example.memorybootcamp.ui.challenges.cards.CardsFragment;
import com.example.memorybootcamp.ui.challenges.cards.CardsFragmentDirections;
import com.example.memorybootcamp.ui.challenges.faces.FacesFragment;
import com.example.memorybootcamp.ui.challenges.faces.FacesFragmentDirections;
import com.example.memorybootcamp.ui.challenges.numbers.NumbersFragment;
import com.example.memorybootcamp.ui.challenges.numbers.NumbersFragmentDirections;
import com.example.memorybootcamp.ui.challenges.words.WordsFragment;
import com.example.memorybootcamp.ui.challenges.words.WordsFragmentDirections;
import com.example.memorybootcamp.ui.home.HomeFragment;
import com.example.memorybootcamp.ui.home.HomeFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private UiModeManager uiModeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(
                view -> Snackbar
                        .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.home)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // initialize preferences
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Drawable drawable = menu.findItem(R.id.action_settings).getIcon();

        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorOnSecondary, typedValue, true);
        @ColorInt int color = typedValue.data;

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        menu.findItem(R.id.action_settings).setIcon(drawable);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onSettingsClicked(MenuItem item){
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Fragment currentFragment = navHostFragment == null? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
        if (currentFragment instanceof HomeFragment) {
            NavDirections action = HomeFragmentDirections.actionHomeToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        } else if (currentFragment instanceof BinaryFragment){
            NavDirections action = BinaryFragmentDirections.actionBinaryToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        } else if (currentFragment instanceof CardsFragment){
            NavDirections action = CardsFragmentDirections.actionCardsToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        } else if (currentFragment instanceof FacesFragment){
            NavDirections action = FacesFragmentDirections.actionFacesToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        } else if (currentFragment instanceof NumbersFragment){
            NavDirections action = NumbersFragmentDirections.actionNumbersToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        } else if (currentFragment instanceof WordsFragment){
            NavDirections action = WordsFragmentDirections.actionWordsToSettings();
            Navigation.findNavController(currentFragment.getView()).navigate(action);
        }
    }

}
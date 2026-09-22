package com.example.smartpantry.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.R;
import com.example.smartpantry.fragments.PantryFragment;
import com.example.smartpantry.fragments.RecipesFragment;
import com.example.smartpantry.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main Activity hosting the primary bottom navigation and fragment views.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private PantryFragment pantryFragment;
    private RecipesFragment recipesFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryFragment = new PantryFragment();
        recipesFragment = new RecipesFragment();
        settingsFragment = new SettingsFragment();

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Set default fragment
        loadFragment(pantryFragment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_pantry);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Fragment selectedFragment = null;
            int titleId = R.string.app_name;

            if (itemId == R.id.navigation_pantry) {
                selectedFragment = pantryFragment;
                titleId = R.string.title_pantry;
            } else if (itemId == R.id.navigation_recipes) {
                selectedFragment = recipesFragment;
                titleId = R.string.title_recipes;
            } else if (itemId == R.id.navigation_settings) {
                selectedFragment = settingsFragment;
                titleId = R.string.title_settings;
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(titleId);
                }
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}

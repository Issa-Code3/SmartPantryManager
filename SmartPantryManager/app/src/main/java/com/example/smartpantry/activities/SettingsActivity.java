package com.example.smartpantry.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.database.PantryDao;

/**
 * Activity for managing application settings and preferences.
 */
public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerUnitSystem;
    private Button btnClearPantry;
    private PantryDao pantryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_settings);
        }

        pantryDao = new PantryDao(this);

        spinnerUnitSystem = findViewById(R.id.spinnerUnitSystem);
        btnClearPantry = findViewById(R.id.btnClearPantry);

        String[] unitSystems = new String[]{"Metric (grams, kg, ml, litres)", "Imperial (oz, lbs, fl oz)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitSystems);
        spinnerUnitSystem.setAdapter(adapter);

        btnClearPantry.setOnClickListener(v -> confirmClearPantry());
    }

    private void confirmClearPantry() {
        new AlertDialog.Builder(this)
                .setTitle("Clear Pantry")
                .setMessage("Are you sure you want to delete all ingredients from your pantry? This cannot be undone.")
                .setPositiveButton("Clear All", (dialog, which) -> {
                    pantryDao.deleteAllPantryItems();
                    Toast.makeText(SettingsActivity.this, "Pantry cleared successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

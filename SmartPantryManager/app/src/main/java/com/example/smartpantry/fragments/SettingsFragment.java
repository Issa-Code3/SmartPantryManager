package com.example.smartpantry.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.smartpantry.R;
import com.example.smartpantry.database.PantryDao;

/**
 * Fragment displaying Settings and application preferences.
 */
public class SettingsFragment extends Fragment {

    private Spinner spinnerUnitSystem;
    private Button btnClearPantry;
    private PantryDao pantryDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_settings, container, false);

        pantryDao = new PantryDao(requireContext());

        spinnerUnitSystem = view.findViewById(R.id.spinnerUnitSystem);
        btnClearPantry = view.findViewById(R.id.btnClearPantry);

        String[] unitSystems = new String[]{"Metric (grams, kg, ml, litres)", "Imperial (oz, lbs, fl oz)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, unitSystems);
        spinnerUnitSystem.setAdapter(adapter);

        btnClearPantry.setOnClickListener(v -> confirmClearPantry());

        return view;
    }

    private void confirmClearPantry() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear Pantry")
                .setMessage("Are you sure you want to delete all ingredients from your pantry? This cannot be undone.")
                .setPositiveButton("Clear All", (dialog, which) -> {
                    pantryDao.deleteAllPantryItems();
                    Toast.makeText(getContext(), "Pantry cleared successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

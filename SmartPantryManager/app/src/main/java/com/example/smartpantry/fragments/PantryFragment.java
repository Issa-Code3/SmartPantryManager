package com.example.smartpantry.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.activities.AddEditIngredientActivity;
import com.example.smartpantry.adapters.PantryAdapter;
import com.example.smartpantry.database.PantryDao;
import com.example.smartpantry.models.PantryItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying the user's pantry ingredients (Pantry List Screen).
 */
public class PantryFragment extends Fragment implements PantryAdapter.OnPantryItemActionListener {

    private RecyclerView rvPantry;
    private LinearLayout llEmptyPantry;
    private FloatingActionButton fabAdd;

    private PantryDao pantryDao;
    private PantryAdapter pantryAdapter;
    private List<PantryItem> pantryItems = new ArrayList<>();

    private final ActivityResultLauncher<Intent> addEditLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    loadPantryItems();
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        pantryDao = new PantryDao(requireContext());

        rvPantry = view.findViewById(R.id.rvPantry);
        llEmptyPantry = view.findViewById(R.id.llEmptyPantry);
        fabAdd = view.findViewById(R.id.fabAddIngredient);

        rvPantry.setLayoutManager(new LinearLayoutManager(getContext()));
        pantryAdapter = new PantryAdapter(pantryItems, this);
        rvPantry.setAdapter(pantryAdapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
            addEditLauncher.launch(intent);
        });

        loadPantryItems();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPantryItems();
    }

    public void loadPantryItems() {
        if (pantryDao != null) {
            pantryItems = pantryDao.getAllPantryItems();
            if (pantryAdapter != null) {
                pantryAdapter.updateItems(pantryItems);
            }
            if (pantryItems.isEmpty()) {
                llEmptyPantry.setVisibility(View.VISIBLE);
                rvPantry.setVisibility(View.GONE);
            } else {
                llEmptyPantry.setVisibility(View.GONE);
                rvPantry.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent intent = new Intent(getContext(), AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_INGREDIENT_ID, item.getId());
        addEditLauncher.launch(intent);
    }

    @Override
    public void onDelete(PantryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete '" + item.getName() + "' from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    pantryDao.deletePantryItem(item.getId());
                    Toast.makeText(getContext(), "'" + item.getName() + "' deleted", Toast.LENGTH_SHORT).show();
                    loadPantryItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

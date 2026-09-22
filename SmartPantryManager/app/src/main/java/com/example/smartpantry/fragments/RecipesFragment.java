package com.example.smartpantry.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.activities.RecipeDetailActivity;
import com.example.smartpantry.adapters.RecipeAdapter;
import com.example.smartpantry.database.PantryDao;
import com.example.smartpantry.database.RecipeDao;
import com.example.smartpantry.models.PantryItem;
import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.utils.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment displaying suggested recipes based strictly on current pantry items.
 */
public class RecipesFragment extends Fragment implements RecipeAdapter.OnRecipeClickListener {

    private RecyclerView rvSuggestedRecipes;
    private LinearLayout llEmptyRecipes;

    private RecipeDao recipeDao;
    private PantryDao pantryDao;
    private RecipeAdapter recipeAdapter;
    private List<Recipe> matchingRecipes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        recipeDao = new RecipeDao(requireContext());
        pantryDao = new PantryDao(requireContext());

        rvSuggestedRecipes = view.findViewById(R.id.rvSuggestedRecipes);
        llEmptyRecipes = view.findViewById(R.id.llEmptyRecipes);

        rvSuggestedRecipes.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeAdapter = new RecipeAdapter(matchingRecipes, this);
        rvSuggestedRecipes.setAdapter(recipeAdapter);

        loadSuggestedRecipes();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSuggestedRecipes();
    }

    public void loadSuggestedRecipes() {
        if (recipeDao != null && pantryDao != null) {
            List<Recipe> allRecipes = recipeDao.getAllRecipes();
            List<PantryItem> pantryItems = pantryDao.getAllPantryItems();

            // Run strict matching algorithm
            matchingRecipes = RecipeMatcher.getMatchingRecipes(allRecipes, pantryItems);

            if (recipeAdapter != null) {
                recipeAdapter.updateRecipes(matchingRecipes);
            }

            if (matchingRecipes.isEmpty()) {
                llEmptyRecipes.setVisibility(View.VISIBLE);
                rvSuggestedRecipes.setVisibility(View.GONE);
            } else {
                llEmptyRecipes.setVisibility(View.GONE);
                rvSuggestedRecipes.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }
}

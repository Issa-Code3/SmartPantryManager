package com.example.smartpantry.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.database.RecipeDao;
import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.models.RecipeIngredient;

/**
 * Activity for displaying details of a specific recipe.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private TextView tvRecipeName;
    private LinearLayout llIngredientsContainer;
    private TextView tvInstructions;

    private RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.recipe_detail);
        }

        recipeDao = new RecipeDao(this);

        tvRecipeName = findViewById(R.id.tvDetailRecipeName);
        llIngredientsContainer = findViewById(R.id.llIngredientsListContainer);
        tvInstructions = findViewById(R.id.tvDetailInstructions);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_RECIPE_ID)) {
            long recipeId = intent.getLongExtra(EXTRA_RECIPE_ID, -1);
            if (recipeId != -1) {
                Recipe recipe = recipeDao.getRecipeById(recipeId);
                if (recipe != null) {
                    displayRecipeDetails(recipe);
                } else {
                    Toast.makeText(this, "Recipe not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                Toast.makeText(this, "Invalid Recipe ID.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "No Recipe ID provided.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayRecipeDetails(Recipe recipe) {
        tvRecipeName.setText(recipe.getName());
        tvInstructions.setText(recipe.getInstructions());

        llIngredientsContainer.removeAllViews();
        if (recipe.getIngredients() != null) {
            for (RecipeIngredient ri : recipe.getIngredients()) {
                TextView tv = new TextView(this);
                double qty = ri.getRequiredQuantity();
                String qtyStr = (qty == (long) qty) ? String.format("%d", (long) qty) : String.format("%.1f", qty);
                String line = "• " + qtyStr + " " + ri.getUnit() + "  " + ri.getIngredientName();
                tv.setText(line);
                tv.setTextSize(16);
                tv.setTextColor(getResources().getColor(R.color.text_primary));
                tv.setPadding(0, 8, 0, 8);
                llIngredientsContainer.addView(tv);
            }
        }
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

package com.example.smartpantry.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Recipe database operations.
 */
public class RecipeDao {

    private final DatabaseHelper dbHelper;

    public RecipeDao(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Retrieves all recipes along with their required ingredients.
     */
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_RECIPES + " ORDER BY " + DatabaseHelper.COL_RECIPE_NAME + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME);
                int instIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_INSTRUCTIONS);

                do {
                    long recipeId = cursor.getLong(idIdx);
                    Recipe recipe = new Recipe(
                            recipeId,
                            cursor.getString(nameIdx),
                            cursor.getString(instIdx)
                    );
                    recipe.setIngredients(getIngredientsForRecipe(db, recipeId));
                    recipes.add(recipe);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return recipes;
    }

    /**
     * Retrieves a single recipe by ID along with its ingredients.
     */
    public Recipe getRecipeById(long recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Recipe recipe = null;

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPES,
                null,
                DatabaseHelper.COL_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                recipe = new Recipe(
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_INSTRUCTIONS))
                );
                recipe.setIngredients(getIngredientsForRecipe(db, recipeId));
            }
            cursor.close();
        }

        return recipe;
    }

    /**
     * Retrieves required ingredients for a specified recipe ID.
     */
    public List<RecipeIngredient> getIngredientsForRecipe(SQLiteDatabase db, long recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                DatabaseHelper.COL_RI_RECIPE_ID + "=?",
                new String[]{String.valueOf(recipeId)},
                null, null, null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_ID);
                int nameIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_NAME);
                int qtyIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_QUANTITY);
                int unitIdx = cursor.getColumnIndexOrThrow(DatabaseHelper.COL_RI_UNIT);

                do {
                    RecipeIngredient ri = new RecipeIngredient(
                            cursor.getLong(idIdx),
                            recipeId,
                            cursor.getString(nameIdx),
                            cursor.getDouble(qtyIdx),
                            cursor.getString(unitIdx)
                    );
                    ingredients.add(ri);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return ingredients;
    }
}

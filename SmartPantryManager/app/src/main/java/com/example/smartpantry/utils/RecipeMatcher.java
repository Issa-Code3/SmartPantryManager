package com.example.smartpantry.utils;

import com.example.smartpantry.models.PantryItem;
import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.models.RecipeIngredient;

import java.util.ArrayList;
import java.util.List;

/**
 * Strict matching algorithm for Smart Pantry Manager.
 * 
 * Rules:
 * A recipe is suggested ONLY IF the user has EVERY required ingredient
 * in a quantity greater than or equal to the recipe requirement.
 */
public class RecipeMatcher {

    /**
     * Filters a list of recipes and returns ONLY those that fully match the user's pantry.
     */
    public static List<Recipe> getMatchingRecipes(List<Recipe> allRecipes, List<PantryItem> pantryItems) {
        List<Recipe> matching = new ArrayList<>();
        if (allRecipes == null || pantryItems == null) {
            return matching;
        }

        for (Recipe recipe : allRecipes) {
            if (canMakeRecipe(recipe, pantryItems)) {
                matching.add(recipe);
            }
        }
        return matching;
    }

    /**
     * Checks whether a single recipe can be prepared using available pantry items.
     */
    public static boolean canMakeRecipe(Recipe recipe, List<PantryItem> pantryItems) {
        if (recipe == null || recipe.getIngredients() == null || recipe.getIngredients().isEmpty()) {
            return false;
        }

        for (RecipeIngredient req : recipe.getIngredients()) {
            String normReqName = IngredientNormalizer.normalizeIngredientName(req.getIngredientName());
            double reqQty = req.getRequiredQuantity();
            String reqUnit = req.getUnit();

            // Total available quantity of this ingredient across all pantry entries
            double totalAvailableConverted = 0.0;
            boolean foundMatchingIngredient = false;

            for (PantryItem pItem : pantryItems) {
                if (pItem == null || pItem.getName() == null) continue;

                String normPantryName = IngredientNormalizer.normalizeIngredientName(pItem.getName());
                if (normReqName.equalsIgnoreCase(normPantryName)) {
                    // Check unit compatibility and convert quantity
                    double convertedQty = IngredientNormalizer.convertQuantity(
                            pItem.getQuantity(),
                            pItem.getUnit(),
                            reqUnit
                    );

                    if (convertedQty >= 0) {
                        foundMatchingIngredient = true;
                        totalAvailableConverted += convertedQty;
                    }
                }
            }

            if (!foundMatchingIngredient || totalAvailableConverted < reqQty - 1e-6) {
                return false; // Missing or insufficient ingredient!
            }
        }

        return true; // All required ingredients satisfied in sufficient quantity!
    }

    /**
     * Optional helper to get "Almost There" recipes (recipes missing exactly 1 ingredient).
     */
    public static List<Recipe> getAlmostThereRecipes(List<Recipe> allRecipes, List<PantryItem> pantryItems) {
        List<Recipe> almostThere = new ArrayList<>();
        if (allRecipes == null || pantryItems == null) {
            return almostThere;
        }

        for (Recipe recipe : allRecipes) {
            int missingCount = getMissingIngredientCount(recipe, pantryItems);
            if (missingCount == 1) {
                almostThere.add(recipe);
            }
        }
        return almostThere;
    }

    public static int getMissingIngredientCount(Recipe recipe, List<PantryItem> pantryItems) {
        if (recipe == null || recipe.getIngredients() == null) return 999;
        int missing = 0;

        for (RecipeIngredient req : recipe.getIngredients()) {
            String normReqName = IngredientNormalizer.normalizeIngredientName(req.getIngredientName());
            double reqQty = req.getRequiredQuantity();
            String reqUnit = req.getUnit();

            double totalAvailableConverted = 0.0;
            boolean foundMatchingIngredient = false;

            for (PantryItem pItem : pantryItems) {
                if (pItem == null || pItem.getName() == null) continue;
                String normPantryName = IngredientNormalizer.normalizeIngredientName(pItem.getName());

                if (normReqName.equalsIgnoreCase(normPantryName)) {
                    double convertedQty = IngredientNormalizer.convertQuantity(
                            pItem.getQuantity(),
                            pItem.getUnit(),
                            reqUnit
                    );
                    if (convertedQty >= 0) {
                        foundMatchingIngredient = true;
                        totalAvailableConverted += convertedQty;
                    }
                }
            }

            if (!foundMatchingIngredient || totalAvailableConverted < reqQty - 1e-6) {
                missing++;
            }
        }

        return missing;
    }
}

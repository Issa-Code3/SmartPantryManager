package com.example.smartpantry;

import com.example.smartpantry.models.PantryItem;
import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.models.RecipeIngredient;
import com.example.smartpantry.utils.IngredientNormalizer;
import com.example.smartpantry.utils.RecipeMatcher;
import com.example.smartpantry.utils.ValidationUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests verifying strict recipe matching logic, ingredient normalization, and input validation.
 */
public class RecipeMatcherTest {

    private Recipe pizzaToast;

    @Before
    public void setUp() {
        // Pizza Toast requires: Bread (2 slices), Cheese (50 g), Tomato (1 piece), Onion (1 piece)
        pizzaToast = new Recipe(1, "Pizza Toast", "Toast bread with toppings.");
        pizzaToast.addIngredient(new RecipeIngredient("Bread", 2, "slice"));
        pizzaToast.addIngredient(new RecipeIngredient("Cheese", 50, "gram"));
        pizzaToast.addIngredient(new RecipeIngredient("Tomato", 1, "piece"));
        pizzaToast.addIngredient(new RecipeIngredient("Onion", 1, "piece"));
    }

    @Test
    public void testStrictMatch_FullMatch_ReturnsTrue() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Bread", 4, "slice", null));
        pantry.add(new PantryItem("Cheese", 100, "gram", null));
        pantry.add(new PantryItem("Tomato", 2, "piece", null));
        pantry.add(new PantryItem("Onion", 1, "piece", null));

        assertTrue(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testStrictMatch_MissingIngredient_ReturnsFalse() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Bread", 4, "slice", null));
        pantry.add(new PantryItem("Cheese", 100, "gram", null));
        pantry.add(new PantryItem("Tomato", 2, "piece", null));
        // Onion is missing!

        assertFalse(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testStrictMatch_InsufficientQuantity_ReturnsFalse() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Bread", 4, "slice", null));
        pantry.add(new PantryItem("Cheese", 25, "gram", null)); // Requires 50g!
        pantry.add(new PantryItem("Tomato", 2, "piece", null));
        pantry.add(new PantryItem("Onion", 1, "piece", null));

        assertFalse(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testStrictMatch_ExactQuantity_ReturnsTrue() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Bread", 2, "slice", null));
        pantry.add(new PantryItem("Cheese", 50, "gram", null));
        pantry.add(new PantryItem("Tomato", 1, "piece", null));
        pantry.add(new PantryItem("Onion", 1, "piece", null));

        assertTrue(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testStrictMatch_MoreThanRequired_ReturnsTrue() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Bread", 10, "slice", null));
        pantry.add(new PantryItem("Cheese", 500, "gram", null));
        pantry.add(new PantryItem("Tomato", 5, "piece", null));
        pantry.add(new PantryItem("Onion", 5, "piece", null));

        assertTrue(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testNormalization_CaseAndPlural_ReturnsTrue() {
        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("breads", 4, "slices", null));
        pantry.add(new PantryItem("CHEESE", 100, "g", null));
        pantry.add(new PantryItem("Tomatoes", 2, "pcs", null));
        pantry.add(new PantryItem("onion", 1, "piece", null));

        assertTrue(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testStrictMatch_EmptyPantry_ReturnsFalse() {
        List<PantryItem> pantry = new ArrayList<>();
        assertFalse(RecipeMatcher.canMakeRecipe(pizzaToast, pantry));
    }

    @Test
    public void testUnitConversion_KilogramToGram_ReturnsTrue() {
        Recipe pastaRecipe = new Recipe(2, "Cheese Pasta", "Cook pasta with cheese.");
        pastaRecipe.addIngredient(new RecipeIngredient("Cheese", 200, "gram"));

        List<PantryItem> pantry = new ArrayList<>();
        pantry.add(new PantryItem("Cheese", 0.5, "kg", null)); // 0.5 kg = 500 grams

        assertTrue(RecipeMatcher.canMakeRecipe(pastaRecipe, pantry));
    }

    @Test
    public void testIngredientNormalizer() {
        assertEquals("tomato", IngredientNormalizer.normalizeIngredientName("Tomatoes"));
        assertEquals("egg", IngredientNormalizer.normalizeIngredientName("EGG"));
        assertEquals("gram", IngredientNormalizer.normalizeUnit("g"));
        assertEquals("kilogram", IngredientNormalizer.normalizeUnit("kg"));
    }

    @Test
    public void testValidationUtils() {
        assertTrue(ValidationUtils.validatePantryInput("Tomato", "5", "piece", "30/09/2026").isValid());
        assertFalse(ValidationUtils.validatePantryInput("", "5", "piece", null).isValid());
        assertFalse(ValidationUtils.validatePantryInput("Tomato", "-1", "piece", null).isValid());
        assertFalse(ValidationUtils.validatePantryInput("Tomato", "abc", "piece", null).isValid());
        assertFalse(ValidationUtils.validatePantryInput("Tomato", "5", "piece", "invalid-date").isValid());
    }
}

package com.example.smartpantry.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class to handle string and unit normalization to ensure robust ingredient matching.
 */
public class IngredientNormalizer {

    private static final Map<String, String> INGREDIENT_ALIASES = new HashMap<>();
    private static final Map<String, String> UNIT_ALIASES = new HashMap<>();

    static {
        // Ingredient normalization rules (plurals to singular, common variations)
        INGREDIENT_ALIASES.put("tomatoes", "tomato");
        INGREDIENT_ALIASES.put("eggs", "egg");
        INGREDIENT_ALIASES.put("onions", "onion");
        INGREDIENT_ALIASES.put("potatoes", "potato");
        INGREDIENT_ALIASES.put("cheeses", "cheese");
        INGREDIENT_ALIASES.put("breads", "bread");
        INGREDIENT_ALIASES.put("garlics", "garlic");
        INGREDIENT_ALIASES.put("rices", "rice");
        INGREDIENT_ALIASES.put("pastas", "pasta");
        INGREDIENT_ALIASES.put("carrots", "carrot");
        INGREDIENT_ALIASES.put("peppers", "pepper");
        INGREDIENT_ALIASES.put("bell peppers", "bell pepper");
        INGREDIENT_ALIASES.put("mushrooms", "mushroom");

        // Unit aliases normalization
        UNIT_ALIASES.put("g", "gram");
        UNIT_ALIASES.put("grams", "gram");
        UNIT_ALIASES.put("gram", "gram");

        UNIT_ALIASES.put("kg", "kilogram");
        UNIT_ALIASES.put("kilograms", "kilogram");
        UNIT_ALIASES.put("kilogram", "kilogram");

        UNIT_ALIASES.put("ml", "millilitre");
        UNIT_ALIASES.put("millilitres", "millilitre");
        UNIT_ALIASES.put("milliliters", "millilitre");
        UNIT_ALIASES.put("millilitre", "millilitre");

        UNIT_ALIASES.put("l", "litre");
        UNIT_ALIASES.put("litres", "litre");
        UNIT_ALIASES.put("liters", "litre");
        UNIT_ALIASES.put("litre", "litre");

        UNIT_ALIASES.put("pcs", "piece");
        UNIT_ALIASES.put("piece", "piece");
        UNIT_ALIASES.put("pieces", "piece");
        UNIT_ALIASES.put("unit", "piece");
        UNIT_ALIASES.put("units", "piece");

        UNIT_ALIASES.put("slice", "slice");
        UNIT_ALIASES.put("slices", "slice");

        UNIT_ALIASES.put("tbsp", "tablespoon");
        UNIT_ALIASES.put("tablespoons", "tablespoon");
        UNIT_ALIASES.put("tablespoon", "tablespoon");

        UNIT_ALIASES.put("tsp", "teaspoon");
        UNIT_ALIASES.put("teaspoons", "teaspoon");
        UNIT_ALIASES.put("teaspoon", "teaspoon");
    }

    /**
     * Normalizes ingredient names for reliable comparison.
     */
    public static String normalizeIngredientName(String rawName) {
        if (rawName == null) {
            return "";
        }
        String cleaned = rawName.trim().toLowerCase(Locale.ROOT);
        if (cleaned.endsWith("s") && !INGREDIENT_ALIASES.containsKey(cleaned)) {
            // Basic fallback for singular form if ending with 's' and not explicitly listed
            if (cleaned.endsWith("es") && cleaned.length() > 3) {
                String stem = cleaned.substring(0, cleaned.length() - 2);
                if (INGREDIENT_ALIASES.containsKey(stem)) {
                    return INGREDIENT_ALIASES.get(stem);
                }
            }
            String stem = cleaned.substring(0, cleaned.length() - 1);
            if (INGREDIENT_ALIASES.containsKey(stem)) {
                return INGREDIENT_ALIASES.get(stem);
            }
        }
        return INGREDIENT_ALIASES.getOrDefault(cleaned, cleaned);
    }

    /**
     * Normalizes unit names to standard category representations.
     */
    public static String normalizeUnit(String rawUnit) {
        if (rawUnit == null) {
            return "";
        }
        String cleaned = rawUnit.trim().toLowerCase(Locale.ROOT);
        return UNIT_ALIASES.getOrDefault(cleaned, cleaned);
    }

    /**
     * Converts a given quantity from sourceUnit to targetUnit if compatible.
     * Returns converted quantity in targetUnit, or -1 if units are incompatible.
     */
    public static double convertQuantity(double quantity, String sourceUnitRaw, String targetUnitRaw) {
        String sourceUnit = normalizeUnit(sourceUnitRaw);
        String targetUnit = normalizeUnit(targetUnitRaw);

        if (sourceUnit.equals(targetUnit)) {
            return quantity;
        }

        // Weight conversions: gram <-> kilogram
        if (sourceUnit.equals("gram") && targetUnit.equals("kilogram")) {
            return quantity / 1000.0;
        }
        if (sourceUnit.equals("kilogram") && targetUnit.equals("gram")) {
            return quantity * 1000.0;
        }

        // Volume conversions: millilitre <-> litre
        if (sourceUnit.equals("millilitre") && targetUnit.equals("litre")) {
            return quantity / 1000.0;
        }
        if (sourceUnit.equals("litre") && targetUnit.equals("millilitre")) {
            return quantity * 1000.0;
        }

        // Incompatible units
        return -1.0;
    }

    /**
     * Checks if source unit can be matched with target unit.
     */
    public static boolean areUnitsCompatible(String unit1, String unit2) {
        String u1 = normalizeUnit(unit1);
        String u2 = normalizeUnit(unit2);
        if (u1.equals(u2)) return true;
        if ((u1.equals("gram") || u1.equals("kilogram")) && (u2.equals("gram") || u2.equals("kilogram"))) return true;
        if ((u1.equals("millilitre") || u1.equals("litre")) && (u2.equals("millilitre") || u2.equals("litre"))) return true;
        return false;
    }
}

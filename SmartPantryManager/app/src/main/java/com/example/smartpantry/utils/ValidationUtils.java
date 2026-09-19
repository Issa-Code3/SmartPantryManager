package com.example.smartpantry.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Utility class for validating user input when adding/editing pantry ingredients.
 */
public class ValidationUtils {

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        public ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    /**
     * Validates name, quantity, unit, and optional expiry date.
     */
    public static ValidationResult validatePantryInput(String name, String quantityStr, String unit, String expiryDate) {
        if (name == null || name.trim().isEmpty()) {
            return new ValidationResult(false, "Ingredient name is required.");
        }

        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            return new ValidationResult(false, "Quantity is required.");
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityStr.trim());
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Quantity must be a valid number.");
        }

        if (quantity <= 0) {
            return new ValidationResult(false, "Quantity must be greater than 0.");
        }

        if (unit == null || unit.trim().isEmpty()) {
            return new ValidationResult(false, "Unit must be selected.");
        }

        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
            if (!isValidDate(expiryDate.trim())) {
                return new ValidationResult(false, "Expiry date must be in dd/MM/yyyy or yyyy-MM-dd format.");
            }
        }

        return new ValidationResult(true, null);
    }

    /**
     * Validates date formats: dd/MM/yyyy, yyyy-MM-dd, dd-MM-yyyy.
     */
    public static boolean isValidDate(String dateStr) {
        String[] formats = {"dd/MM/yyyy", "yyyy-MM-dd", "dd-MM-yyyy"};
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                sdf.setLenient(false);
                sdf.parse(dateStr);
                return true;
            } catch (ParseException ignored) {
            }
        }
        return false;
    }
}

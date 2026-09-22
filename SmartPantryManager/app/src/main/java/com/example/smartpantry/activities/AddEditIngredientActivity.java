package com.example.smartpantry.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.database.PantryDao;
import com.example.smartpantry.models.PantryItem;
import com.example.smartpantry.utils.ValidationUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Activity for adding a new ingredient or editing an existing ingredient.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_INGREDIENT_ID = "extra_ingredient_id";

    private TextView tvFormTitle;
    private EditText etIngredientName;
    private EditText etQuantity;
    private Spinner spinnerUnit;
    private EditText etExpiryDate;
    private Button btnSaveIngredient;

    private PantryDao pantryDao;
    private long editIngredientId = -1;
    private PantryItem existingItem = null;

    private final String[] UNITS = new String[]{
            "piece", "pieces", "gram", "g", "kilogram", "kg",
            "millilitre", "ml", "litre", "l", "slice", "slices",
            "tablespoon", "teaspoon"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pantryDao = new PantryDao(this);

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);

        // Populate unit spinner
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, UNITS);
        spinnerUnit.setAdapter(unitAdapter);

        // DatePicker dialog setup
        etExpiryDate.setOnClickListener(v -> showDatePickerDialog());

        // Check if editing
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_INGREDIENT_ID)) {
            editIngredientId = intent.getLongExtra(EXTRA_INGREDIENT_ID, -1);
            if (editIngredientId != -1) {
                existingItem = pantryDao.getPantryItemById(editIngredientId);
                if (existingItem != null) {
                    tvFormTitle.setText(R.string.edit_ingredient);
                    setTitle(R.string.edit_ingredient);
                    populateFields(existingItem);
                }
            }
        } else {
            tvFormTitle.setText(R.string.add_ingredient);
            setTitle(R.string.add_ingredient);
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }

    private void populateFields(PantryItem item) {
        etIngredientName.setText(item.getName());
        double qty = item.getQuantity();
        etQuantity.setText(qty == (long) qty ? String.valueOf((long) qty) : String.valueOf(qty));

        if (item.getExpiryDate() != null) {
            etExpiryDate.setText(item.getExpiryDate());
        }

        // Set spinner selection
        String unit = item.getUnit();
        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equalsIgnoreCase(unit)) {
                spinnerUnit.setSelection(i);
                break;
            }
        }
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthOfYear, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.US, "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, yearSelected);
                    etExpiryDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveIngredient() {
        String name = etIngredientName.getText() != null ? etIngredientName.getText().toString().trim() : "";
        String qtyStr = etQuantity.getText() != null ? etQuantity.getText().toString().trim() : "";
        String unit = spinnerUnit.getSelectedItem() != null ? spinnerUnit.getSelectedItem().toString() : "";
        String expiry = etExpiryDate.getText() != null ? etExpiryDate.getText().toString().trim() : "";

        ValidationUtils.ValidationResult validation = ValidationUtils.validatePantryInput(name, qtyStr, unit, expiry);

        if (!validation.isValid()) {
            Toast.makeText(this, validation.getErrorMessage(), Toast.LENGTH_LONG).show();
            if (name.isEmpty()) {
                etIngredientName.setError("Name is required");
            } else if (qtyStr.isEmpty()) {
                etQuantity.setError("Quantity is required");
            }
            return;
        }

        double quantity = Double.parseDouble(qtyStr);

        if (existingItem != null) {
            // Update existing item
            existingItem.setName(name);
            existingItem.setQuantity(quantity);
            existingItem.setUnit(unit);
            existingItem.setExpiryDate(expiry);
            pantryDao.updatePantryItem(existingItem);
            Toast.makeText(this, "Ingredient updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Insert new item
            PantryItem newItem = new PantryItem(name, quantity, unit, expiry);
            pantryDao.insertPantryItem(newItem);
            Toast.makeText(this, "Ingredient added to pantry", Toast.LENGTH_SHORT).show();
        }

        setResult(RESULT_OK);
        finish();
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

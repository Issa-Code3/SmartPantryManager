package com.example.smartpantry.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelper class for managing database creation, upgrades, and seeding pre-loaded recipes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "smart_pantry.db";
    public static final int DATABASE_VERSION = 1;

    // Table: pantry_items
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QUANTITY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Table: recipes
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_INSTRUCTIONS = "instructions";

    // Table: recipe_ingredients
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "ingredient_name";
    public static final String COL_RI_QUANTITY = "required_quantity";
    public static final String COL_RI_UNIT = "unit";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create pantry_items table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QUANTITY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT NOT NULL, " +
                COL_PANTRY_EXPIRY + " TEXT" +
                ");";
        db.execSQL(createPantryTable);

        // Create recipes table
        String createRecipesTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_INSTRUCTIONS + " TEXT NOT NULL" +
                ");";
        db.execSQL(createRecipesTable);

        // Create recipe_ingredients table
        String createRecipeIngredientsTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QUANTITY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " + TABLE_RECIPES + "(" + COL_RECIPE_ID + ") ON DELETE CASCADE" +
                ");";
        db.execSQL(createRecipeIngredientsTable);

        // Seed 20 standard recipes into the database
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void seedRecipes(SQLiteDatabase db) {
        // 1. Cheese Omelette
        long r1 = insertRecipe(db, "Cheese Omelette",
                "1. Crack eggs into a bowl and beat well.\n" +
                "2. Chop the onion finely.\n" +
                "3. Heat a pan over medium flame and pour beaten eggs.\n" +
                "4. Sprinkle chopped onions and shredded cheese on top.\n" +
                "5. Fold omelette and cook until cheese melts. Serve hot.");
        insertIngredient(db, r1, "Egg", 2, "piece");
        insertIngredient(db, r1, "Cheese", 50, "gram");
        insertIngredient(db, r1, "Onion", 1, "piece");

        // 2. Tomato Toast
        long r2 = insertRecipe(db, "Tomato Toast",
                "1. Toast bread slices until golden brown.\n" +
                "2. Slice tomatoes thinly.\n" +
                "3. Place tomato slices on toast and top with shredded cheese.\n" +
                "4. Grill or microwave for 1 minute until cheese melts.");
        insertIngredient(db, r2, "Bread", 2, "slice");
        insertIngredient(db, r2, "Tomato", 1, "piece");
        insertIngredient(db, r2, "Cheese", 30, "gram");

        // 3. Vegetable Sandwich
        long r3 = insertRecipe(db, "Vegetable Sandwich",
                "1. Slice tomato, onion, and cucumber into thin rounds.\n" +
                "2. Layer vegetables evenly between two slices of bread.\n" +
                "3. Press sandwich firmly, cut diagonally and serve.");
        insertIngredient(db, r3, "Bread", 2, "slice");
        insertIngredient(db, r3, "Tomato", 1, "piece");
        insertIngredient(db, r3, "Onion", 1, "piece");
        insertIngredient(db, r3, "Cucumber", 1, "piece");

        // 4. Egg Fried Rice
        long r4 = insertRecipe(db, "Egg Fried Rice",
                "1. Heat oil in a wok or large frying pan.\n" +
                "2. Scramble eggs and set aside.\n" +
                "3. Saute chopped onion until soft.\n" +
                "4. Add cooked rice, scrambled eggs, and soy sauce.\n" +
                "5. Toss on high heat for 3 minutes and serve.");
        insertIngredient(db, r4, "Rice", 200, "gram");
        insertIngredient(db, r4, "Egg", 2, "piece");
        insertIngredient(db, r4, "Onion", 1, "piece");
        insertIngredient(db, r4, "Soy Sauce", 2, "tablespoon");

        // 5. Garlic Bread
        long r5 = insertRecipe(db, "Garlic Bread",
                "1. Mince garlic cloves finely and mix with softened butter.\n" +
                "2. Spread garlic butter generously over bread slices.\n" +
                "3. Bake or toast in a pan until golden and crispy.");
        insertIngredient(db, r5, "Bread", 4, "slice");
        insertIngredient(db, r5, "Butter", 20, "gram");
        insertIngredient(db, r5, "Garlic", 2, "piece");

        // 6. Tomato Pasta
        long r6 = insertRecipe(db, "Tomato Pasta",
                "1. Boil pasta according to package instructions and drain.\n" +
                "2. Heat olive oil and saute minced garlic.\n" +
                "3. Add chopped tomatoes and simmer for 10 minutes into a sauce.\n" +
                "4. Toss pasta in tomato sauce and serve warm.");
        insertIngredient(db, r6, "Pasta", 150, "gram");
        insertIngredient(db, r6, "Tomato", 2, "piece");
        insertIngredient(db, r6, "Garlic", 2, "piece");
        insertIngredient(db, r6, "Olive Oil", 1, "tablespoon");

        // 7. Vegetable Pasta
        long r7 = insertRecipe(db, "Vegetable Pasta",
                "1. Cook pasta until al dente.\n" +
                "2. Chop tomato, onion, and bell pepper.\n" +
                "3. Stir-fry veggies in a pan until tender.\n" +
                "4. Combine pasta with sauteed veggies and mix well.");
        insertIngredient(db, r7, "Pasta", 150, "gram");
        insertIngredient(db, r7, "Tomato", 1, "piece");
        insertIngredient(db, r7, "Onion", 1, "piece");
        insertIngredient(db, r7, "Bell Pepper", 1, "piece");

        // 8. Cheese Sandwich
        long r8 = insertRecipe(db, "Cheese Sandwich",
                "1. Spread butter on one side of each bread slice.\n" +
                "2. Place cheese between unbuttered sides.\n" +
                "3. Toast in a pan until both sides are golden and cheese is melted.");
        insertIngredient(db, r8, "Bread", 2, "slice");
        insertIngredient(db, r8, "Cheese", 50, "gram");
        insertIngredient(db, r8, "Butter", 10, "gram");

        // 9. French Toast
        long r9 = insertRecipe(db, "French Toast",
                "1. Whisk egg, milk, and sugar in a shallow bowl.\n" +
                "2. Dip bread slices into the egg mixture until soaked.\n" +
                "3. Cook on a greased pan for 2-3 minutes per side until golden.");
        insertIngredient(db, r9, "Bread", 2, "slice");
        insertIngredient(db, r9, "Egg", 1, "piece");
        insertIngredient(db, r9, "Milk", 50, "millilitre");
        insertIngredient(db, r9, "Sugar", 1, "tablespoon");

        // 10. Egg Sandwich
        long r10 = insertRecipe(db, "Egg Sandwich",
                "1. Hard-boil or fry eggs.\n" +
                "2. Slice egg and onion.\n" +
                "3. Place egg slices and onion between bread slices.");
        insertIngredient(db, r10, "Bread", 2, "slice");
        insertIngredient(db, r10, "Egg", 2, "piece");
        insertIngredient(db, r10, "Onion", 1, "piece");

        // 11. Potato Omelette
        long r11 = insertRecipe(db, "Potato Omelette",
                "1. Dice potato and onion into small cubes.\n" +
                "2. Fry potato and onion until soft and cooked through.\n" +
                "3. Pour beaten eggs over potatoes and cook on low heat until set.");
        insertIngredient(db, r11, "Egg", 2, "piece");
        insertIngredient(db, r11, "Potato", 1, "piece");
        insertIngredient(db, r11, "Onion", 1, "piece");

        // 12. Tomato Egg Scramble
        long r12 = insertRecipe(db, "Tomato Egg Scramble",
                "1. Chop tomato and onion finely.\n" +
                "2. Saute onion and tomato in a pan until soft.\n" +
                "3. Add eggs and scramble gently until fluffy.");
        insertIngredient(db, r12, "Egg", 2, "piece");
        insertIngredient(db, r12, "Tomato", 1, "piece");
        insertIngredient(db, r12, "Onion", 1, "piece");

        // 13. Garlic Noodles
        long r13 = insertRecipe(db, "Garlic Noodles",
                "1. Boil noodles and drain water.\n" +
                "2. Melt butter and fry minced garlic until fragrant.\n" +
                "3. Add soy sauce and toss in cooked noodles thoroughly.");
        insertIngredient(db, r13, "Noodles", 150, "gram");
        insertIngredient(db, r13, "Garlic", 3, "piece");
        insertIngredient(db, r13, "Soy Sauce", 2, "tablespoon");
        insertIngredient(db, r13, "Butter", 1, "tablespoon");

        // 14. Vegetable Fried Rice
        long r14 = insertRecipe(db, "Vegetable Fried Rice",
                "1. Dice carrot, onion, and bell pepper finely.\n" +
                "2. Stir fry vegetables in a hot pan.\n" +
                "3. Add cooked rice and mix well over high flame.");
        insertIngredient(db, r14, "Rice", 200, "gram");
        insertIngredient(db, r14, "Carrot", 1, "piece");
        insertIngredient(db, r14, "Onion", 1, "piece");
        insertIngredient(db, r14, "Bell Pepper", 1, "piece");

        // 15. Cheese Toast
        long r15 = insertRecipe(db, "Cheese Toast",
                "1. Place cheese slices over bread.\n" +
                "2. Toast in an oven or skillet until cheese is golden bubbly.");
        insertIngredient(db, r15, "Bread", 2, "slice");
        insertIngredient(db, r15, "Cheese", 40, "gram");

        // 16. Potato Sandwich
        long r16 = insertRecipe(db, "Potato Sandwich",
                "1. Boil and mash potato with chopped onion.\n" +
                "2. Spread potato mixture onto bread and toast until crispy.");
        insertIngredient(db, r16, "Bread", 2, "slice");
        insertIngredient(db, r16, "Potato", 1, "piece");
        insertIngredient(db, r16, "Onion", 1, "piece");

        // 17. Egg Toast
        long r17 = insertRecipe(db, "Egg Toast",
                "1. Melt butter in a pan.\n" +
                "2. Fry eggs to your preference.\n" +
                "3. Place fried eggs on toasted bread.");
        insertIngredient(db, r17, "Bread", 2, "slice");
        insertIngredient(db, r17, "Egg", 2, "piece");
        insertIngredient(db, r17, "Butter", 10, "gram");

        // 18. Tomato Salad
        long r18 = insertRecipe(db, "Tomato Salad",
                "1. Chop tomatoes, cucumber, and onion.\n" +
                "2. Toss together in a bowl with olive oil dressing.");
        insertIngredient(db, r18, "Tomato", 2, "piece");
        insertIngredient(db, r18, "Cucumber", 1, "piece");
        insertIngredient(db, r18, "Onion", 1, "piece");
        insertIngredient(db, r18, "Olive Oil", 1, "tablespoon");

        // 19. Vegetable Omelette
        long r19 = insertRecipe(db, "Vegetable Omelette",
                "1. Beat eggs in a bowl.\n" +
                "2. Add finely chopped tomato, onion, and bell pepper.\n" +
                "3. Pour into a heated pan and cook both sides until set.");
        insertIngredient(db, r19, "Egg", 2, "piece");
        insertIngredient(db, r19, "Tomato", 1, "piece");
        insertIngredient(db, r19, "Onion", 1, "piece");
        insertIngredient(db, r19, "Bell Pepper", 1, "piece");

        // 20. Garlic Pasta
        long r20 = insertRecipe(db, "Garlic Pasta",
                "1. Cook pasta in boiling salted water.\n" +
                "2. Sauté sliced garlic in olive oil until golden.\n" +
                "3. Toss pasta with garlic oil and sprinkle cheese on top.");
        insertIngredient(db, r20, "Pasta", 150, "gram");
        insertIngredient(db, r20, "Garlic", 4, "piece");
        insertIngredient(db, r20, "Olive Oil", 2, "tablespoon");
        insertIngredient(db, r20, "Cheese", 20, "gram");
    }

    private long insertRecipe(SQLiteDatabase db, String name, String instructions) {
        ContentValues cv = new ContentValues();
        cv.put(COL_RECIPE_NAME, name);
        cv.put(COL_RECIPE_INSTRUCTIONS, instructions);
        return db.insert(TABLE_RECIPES, null, cv);
    }

    private void insertIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put(COL_RI_RECIPE_ID, recipeId);
        cv.put(COL_RI_NAME, name);
        cv.put(COL_RI_QUANTITY, qty);
        cv.put(COL_RI_UNIT, unit);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, cv);
    }
}

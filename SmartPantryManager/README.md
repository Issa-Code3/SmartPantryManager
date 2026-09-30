# Smart Pantry Manager

## Mobile App Development 700 — Final Assignment Application

Smart Pantry Manager is a native Android application built entirely in **Java** designed to help users reduce food waste by tracking household ingredients and suggesting recipes that can be prepared using **only the available pantry items**.

---

## 🌟 Core Features

- 🍅 **Pantry CRUD Management**: Add, view, edit, and delete pantry ingredients with quantity, units, and expiry dates.
- 🔒 **Strict Recipe Matching Algorithm**: Only suggests recipes for which the user currently owns **100% of the required ingredients in sufficient quantities**. Missing or insufficient ingredients automatically exclude a recipe.
- 💾 **SQLite Persistent Storage**: Built with `SQLiteOpenHelper`, ensuring all pantry records remain persistently saved even after app restarts or device reboots.
- 🔄 **Ingredient & Unit Normalization**: Handles singular/plural variations (e.g. *Tomato* vs. *Tomatoes*) and unit conversions (e.g. *g* to *kg*, *ml* to *litre*).
- 📖 **Detailed Recipe Instructions**: Displays step-by-step preparation instructions and exact ingredient breakdowns.
- ⚙️ **Settings & Preferences**: Expiring soon alert toggles, unit preferences, and option to clear pantry.
- 📱 **Clean Material Navigation**: Bottom Navigation View with 3 main sections (Pantry, Recipes, Settings).

---

## 🛠️ Technology Stack

- **Language**: Java (Android SDK 34)
- **IDE**: Android Studio
- **Database**: SQLite (`SQLiteOpenHelper`, `PantryDao`, `RecipeDao`)
- **UI Architecture**: Activities, Fragments, `ConstraintLayout`, `RecyclerView`, `CardView`, Material Components
- **Testing**: JUnit 4 for unit testing strict matching algorithm and normalization

---

## 📊 Database Choice & Justification

**Selected Database: SQLite**

### Why SQLite was chosen:
1. **Local Persistent Storage**: SQLite provides structured, transactional, local relational storage without needing an external cloud server or internet connectivity.
2. **Persistence**: Data remains saved across application restarts and device power cycles.
3. **Full CRUD Support**: Supports `INSERT`, `SELECT`, `UPDATE`, and `DELETE` operations using parameterized queries (`ContentValues`) to prevent SQL injection.
4. **Relational Structure**: Ideal for managing relationships between `recipes` and `recipe_ingredients` via foreign keys.

---

## 🍽️ Pre-Loaded Recipe Database (20 Recipes)

The app ships with **20 recipes** seeded automatically on first launch. The strict matching algorithm will only suggest a recipe when you have **every ingredient listed** in sufficient quantity.

| # | Recipe | Required Ingredients |
|---|--------|----------------------|
| 1 | 🍳 **Cheese Omelette** | 2 Eggs, 50g Cheese, 1 Onion |
| 2 | 🍞 **Tomato Toast** | 2 slices Bread, 1 Tomato, 30g Cheese |
| 3 | 🥪 **Vegetable Sandwich** | 2 slices Bread, 1 Tomato, 1 Onion, 1 Cucumber |
| 4 | 🍚 **Egg Fried Rice** | 200g Rice, 2 Eggs, 1 Onion, 2 tbsp Soy Sauce |
| 5 | 🧄 **Garlic Bread** | 4 slices Bread, 20g Butter, 2 Garlic |
| 6 | 🍝 **Tomato Pasta** | 150g Pasta, 2 Tomatoes, 2 Garlic, 1 tbsp Olive Oil |
| 7 | 🥦 **Vegetable Pasta** | 150g Pasta, 1 Tomato, 1 Onion, 1 Bell Pepper |
| 8 | 🧀 **Cheese Sandwich** | 2 slices Bread, 50g Cheese, 10g Butter |
| 9 | 🥚 **French Toast** | 2 slices Bread, 1 Egg, 50ml Milk, 1 tbsp Sugar |
| 10 | 🥚 **Egg Sandwich** | 2 slices Bread, 2 Eggs, 1 Onion |
| 11 | 🥔 **Potato Omelette** | 2 Eggs, 1 Potato, 1 Onion |
| 12 | 🍅 **Tomato Egg Scramble** | 2 Eggs, 1 Tomato, 1 Onion |
| 13 | 🍜 **Garlic Noodles** | 150g Noodles, 3 Garlic, 2 tbsp Soy Sauce, 1 tbsp Butter |
| 14 | 🥕 **Vegetable Fried Rice** | 200g Rice, 1 Carrot, 1 Onion, 1 Bell Pepper |
| 15 | 🧀 **Cheese Toast** | 2 slices Bread, 40g Cheese |
| 16 | 🥪 **Potato Sandwich** | 2 slices Bread, 1 Potato, 1 Onion |
| 17 | 🍞 **Egg Toast** | 2 slices Bread, 2 Eggs, 10g Butter |
| 18 | 🥗 **Tomato Salad** | 2 Tomatoes, 1 Cucumber, 1 Onion, 1 tbsp Olive Oil |
| 19 | 🥚 **Vegetable Omelette** | 2 Eggs, 1 Tomato, 1 Onion, 1 Bell Pepper |
| 20 | 🧄 **Garlic Pasta** | 150g Pasta, 4 Garlic, 2 tbsp Olive Oil, 20g Cheese |

### 🛒 Pantry Quick-Start Tips

To see your **first recipes immediately**, try adding these ingredients to your pantry:

```
Bread   → 4 slices     — unlocks: Cheese Toast, Egg Toast, Garlic Bread, Cheese Sandwich…
Cheese  → 100g         — unlocks: Cheese Toast, Cheese Omelette, Cheese Sandwich…
Egg     → 4 pieces     — unlocks: Cheese Omelette, French Toast, Egg Sandwich…
Tomato  → 2 pieces     — unlocks: Tomato Salad, Tomato Toast (with Bread + Cheese)…
Onion   → 2 pieces
Garlic  → 4 pieces
```

---

## 🚀 Setup & Installation Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/<your-username>/smart-pantry-manager.git
   ```

2. **Open in Android Studio**:
   - Open Android Studio.
   - Select **Open an Existing Project** and choose the `SmartPantryManager` folder.

3. **Build & Run**:
   - Allow Gradle to sync dependencies.
   - Run the application on an Android Emulator (API level 24+) or physical device.

---

## 🧪 Testing & Verification

Run unit tests via command line:
```bash
./gradlew test
```
All unit tests verify:
- Full ingredient match → Recipe suggested
- Missing ingredient → Recipe excluded
- Insufficient quantity → Recipe excluded
- Case/Plural normalization → Match succeeded
- Unit conversion (e.g. 0.5 kg = 500 g) → Match succeeded

---

## 📄 License & Academic Integrity

Developed for **Mobile App Development 700**. All code is written in Java following guidelines and clean architecture principles.

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

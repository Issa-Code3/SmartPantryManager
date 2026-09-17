package com.example.smartpantry.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a Recipe with required ingredients and instructions.
 */
public class Recipe implements Serializable {
    private long id;
    private String name;
    private String instructions;
    private List<RecipeIngredient> ingredients;

    public Recipe() {
        this.ingredients = new ArrayList<>();
    }

    public Recipe(long id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }

    public Recipe(String name, String instructions) {
        this.name = name;
        this.instructions = instructions;
        this.ingredients = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        if (this.ingredients == null) {
            this.ingredients = new ArrayList<>();
        }
        this.ingredients.add(ingredient);
    }
}

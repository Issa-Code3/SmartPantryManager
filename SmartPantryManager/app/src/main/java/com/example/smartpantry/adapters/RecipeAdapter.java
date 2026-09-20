package com.example.smartpantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.models.Recipe;
import com.example.smartpantry.models.RecipeIngredient;

import java.util.List;

/**
 * Custom RecyclerView Adapter for displaying suggested recipes.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> recipes;
    private final OnRecipeClickListener clickListener;

    public RecipeAdapter(List<Recipe> recipes, OnRecipeClickListener clickListener) {
        this.recipes = recipes;
        this.clickListener = clickListener;
    }

    public void updateRecipes(List<Recipe> newRecipes) {
        this.recipes = newRecipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.tvRecipeName.setText(recipe.getName());

        // Format required ingredients preview string
        StringBuilder ingredientsSummary = new StringBuilder("Required: ");
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                RecipeIngredient ri = recipe.getIngredients().get(i);
                double qty = ri.getRequiredQuantity();
                String qtyStr = (qty == (long) qty) ? String.format("%d", (long) qty) : String.format("%.1f", qty);
                ingredientsSummary.append(qtyStr).append(" ").append(ri.getUnit()).append(" ").append(ri.getIngredientName());
                if (i < recipe.getIngredients().size() - 1) {
                    ingredientsSummary.append(", ");
                }
            }
        } else {
            ingredientsSummary.append("None");
        }
        holder.tvIngredientsSummary.setText(ingredientsSummary.toString());
        holder.tvMatchBadge.setText("✓ 100% Match — All ingredients in pantry");

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;
        TextView tvIngredientsSummary;
        TextView tvMatchBadge;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredientsSummary = itemView.findViewById(R.id.tvRecipeIngredientsSummary);
            tvMatchBadge = itemView.findViewById(R.id.tvMatchBadge);
        }
    }
}

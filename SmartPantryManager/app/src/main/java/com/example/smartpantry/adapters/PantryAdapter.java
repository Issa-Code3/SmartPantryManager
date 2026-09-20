package com.example.smartpantry.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.models.PantryItem;

import java.util.List;

/**
 * Custom RecyclerView Adapter for displaying pantry ingredients.
 */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnPantryItemActionListener {
        void onEdit(PantryItem item);
        void onDelete(PantryItem item);
    }

    private List<PantryItem> items;
    private final OnPantryItemActionListener actionListener;

    public PantryAdapter(List<PantryItem> items, OnPantryItemActionListener actionListener) {
        this.items = items;
        this.actionListener = actionListener;
    }

    public void updateItems(List<PantryItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.tvName.setText(item.getName());

        // Format quantity: display cleanly without trailing decimal if whole number
        double qty = item.getQuantity();
        String qtyFormatted = (qty == (long) qty) ? String.format("%d", (long) qty) : String.format("%.2f", qty);
        holder.tvQuantityUnit.setText(qtyFormatted + " " + item.getUnit());

        if (item.getExpiryDate() != null && !item.getExpiryDate().trim().isEmpty()) {
            holder.tvExpiryDate.setText("Expires: " + item.getExpiryDate().trim());
            holder.tvExpiryDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvExpiryDate.setText("No expiry date");
            holder.tvExpiryDate.setVisibility(View.VISIBLE);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(item);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onDelete(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQuantityUnit;
        TextView tvExpiryDate;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPantryItemName);
            tvQuantityUnit = itemView.findViewById(R.id.tvPantryItemQuantity);
            tvExpiryDate = itemView.findViewById(R.id.tvPantryItemExpiry);
            btnEdit = itemView.findViewById(R.id.btnEditItem);
            btnDelete = itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}

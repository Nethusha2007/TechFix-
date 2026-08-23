package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.SparePart;
import com.techfix.app.util.StatusUtil;

import java.util.List;

/** Binds spare-part cards for the admin inventory list. */
public class SparePartAdapter extends RecyclerView.Adapter<SparePartAdapter.VH> {

    private final List<SparePart> items;
    private final OnItemClickListener<SparePart> listener;

    public SparePartAdapter(List<SparePart> items, OnItemClickListener<SparePart> listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_spare_part, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        SparePart p = items.get(position);
        h.ivIcon.setImageResource(iconFor(p.category));
        h.tvName.setText(p.name);
        h.tvCategory.setText(p.category);
        h.tvStock.setText(p.stock + (p.stock == 1 ? " unit in stock" : " units in stock"));
        h.tvPrice.setText(p.price);
        StatusUtil.applyStatus(h.tvStatus, p.status());
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Picks a representative icon for a spare-part category. */
    private static int iconFor(String category) {
        if (category == null) return R.drawable.ic_parts;
        switch (category.trim().toLowerCase()) {
            case "screens":
                return R.drawable.ic_screen;
            case "batteries":
                return R.drawable.ic_battery;
            case "charging":
                return R.drawable.ic_charging;
            case "storage":
            case "memory":
                return R.drawable.ic_data;
            case "housing":
                return R.drawable.ic_mobile;
            default:
                return R.drawable.ic_parts;
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvCategory, tvStock, tvPrice, tvStatus;

        VH(@NonNull View v) {
            super(v);
            ivIcon = v.findViewById(R.id.ivIcon);
            tvName = v.findViewById(R.id.tvName);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvStock = v.findViewById(R.id.tvStock);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}

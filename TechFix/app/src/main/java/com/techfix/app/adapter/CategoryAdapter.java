package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.DeviceCategory;

import java.util.List;

/** Binds device-category tiles ({@code item_category}). Used on Home and the categories grid. */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VH> {

    private final List<DeviceCategory> items;
    private final int layoutRes;
    private final OnItemClickListener<DeviceCategory> listener;

    /** Uses the compact horizontal tile ({@code item_category}). */
    public CategoryAdapter(List<DeviceCategory> items, OnItemClickListener<DeviceCategory> listener) {
        this(items, R.layout.item_category, listener);
    }

    /** Uses a caller-provided tile layout (e.g. {@code item_category_grid}). */
    public CategoryAdapter(List<DeviceCategory> items, int layoutRes,
                           OnItemClickListener<DeviceCategory> listener) {
        this.items = items;
        this.layoutRes = layoutRes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        DeviceCategory c = items.get(position);
        h.ivIcon.setImageResource(c.iconResId);
        h.tvName.setText(c.name);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;

        VH(@NonNull View v) {
            super(v);
            ivIcon = v.findViewById(R.id.ivIcon);
            tvName = v.findViewById(R.id.tvName);
        }
    }
}

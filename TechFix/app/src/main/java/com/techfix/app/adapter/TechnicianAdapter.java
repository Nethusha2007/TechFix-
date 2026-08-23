package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.Technician;
import com.techfix.app.util.StatusUtil;

import java.util.List;

/** Binds technician cards for the admin technicians list. */
public class TechnicianAdapter extends RecyclerView.Adapter<TechnicianAdapter.VH> {

    private final List<Technician> items;
    private final OnItemClickListener<Technician> listener;

    public TechnicianAdapter(List<Technician> items, OnItemClickListener<Technician> listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_technician, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Technician t = items.get(position);
        h.tvInitials.setText(initials(t.name));
        h.tvName.setText(t.name);
        h.tvSpecialty.setText(t.specialty);
        h.tvBranch.setText(t.branch);
        StatusUtil.applyStatus(h.tvAvailability, t.available ? "Available" : "Busy");
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(t);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /** Builds up to two uppercase initials from a full name. */
    private static String initials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(parts[0].charAt(0)));
        if (parts.length > 1) {
            sb.append(Character.toUpperCase(parts[parts.length - 1].charAt(0)));
        }
        return sb.toString();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvInitials, tvName, tvSpecialty, tvBranch, tvAvailability;

        VH(@NonNull View v) {
            super(v);
            tvInitials = v.findViewById(R.id.tvInitials);
            tvName = v.findViewById(R.id.tvName);
            tvSpecialty = v.findViewById(R.id.tvSpecialty);
            tvBranch = v.findViewById(R.id.tvBranch);
            tvAvailability = v.findViewById(R.id.tvAvailability);
        }
    }
}

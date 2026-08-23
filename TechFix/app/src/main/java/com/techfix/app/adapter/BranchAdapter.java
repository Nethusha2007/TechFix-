package com.techfix.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.Branch;
import com.techfix.app.util.MapUtil;

import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.VH> {

    private final List<Branch> items;
    private final OnItemClickListener<Branch> listener;

    public BranchAdapter(List<Branch> items, OnItemClickListener<Branch> listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_branch, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        final Branch b = items.get(position);
        final Context ctx = h.itemView.getContext();

        h.tvName.setText(b.name);
        h.tvAddress.setText(b.address);
        h.tvDistance.setText(b.distance);
        h.tvHours.setText(b.hours);

        h.btnCall.setOnClickListener(v -> {
            try {
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + b.phone));
                ctx.startActivity(dial);
            } catch (Exception e) {
                Toast.makeText(ctx, b.phone, Toast.LENGTH_SHORT).show();
            }
        });

        h.btnDirections.setOnClickListener(v -> {
            try {
                MapUtil.openDirections(ctx, b.lat, b.lng, b.name + ", " + b.address);
            } catch (Exception e) {
                Toast.makeText(ctx, "Opening directions to " + b.name, Toast.LENGTH_SHORT).show();
            }
        });

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(b);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvAddress, tvDistance, tvHours;
        View btnCall, btnDirections;

        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvAddress = v.findViewById(R.id.tvAddress);
            tvDistance = v.findViewById(R.id.tvDistance);
            tvHours = v.findViewById(R.id.tvHours);
            btnCall = v.findViewById(R.id.btnCall);
            btnDirections = v.findViewById(R.id.btnDirections);
        }
    }
}

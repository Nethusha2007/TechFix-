package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.TrackStep;

import java.util.List;

public class TrackStepAdapter extends RecyclerView.Adapter<TrackStepAdapter.VH> {

    private final List<TrackStep> items;

    public TrackStepAdapter(List<TrackStep> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track_step, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TrackStep s = items.get(position);
        h.tvTitle.setText(s.title);

        int success = ContextCompat.getColor(h.itemView.getContext(), R.color.success);
        int divider = ContextCompat.getColor(h.itemView.getContext(), R.color.divider);
        int primary = ContextCompat.getColor(h.itemView.getContext(), R.color.textPrimary);
        int muted = ContextCompat.getColor(h.itemView.getContext(), R.color.textMuted);
        int secondary = ContextCompat.getColor(h.itemView.getContext(), R.color.textSecondary);

        switch (s.state) {
            case TrackStep.DONE:
                h.node.setBackgroundResource(R.drawable.bg_circle_success);
                h.ivCheck.setVisibility(View.VISIBLE);
                h.tvTitle.setTextColor(primary);
                h.tvTime.setTextColor(secondary);
                h.tvTime.setText(s.time.isEmpty() ? "Completed" : s.time);
                break;
            case TrackStep.CURRENT:
                h.node.setBackgroundResource(R.drawable.bg_circle_primary);
                h.ivCheck.setVisibility(View.GONE);
                h.tvTitle.setTextColor(primary);
                h.tvTime.setTextColor(ContextCompat.getColor(h.itemView.getContext(), R.color.colorPrimary));
                h.tvTime.setText(s.time.isEmpty() ? "In progress…" : s.time);
                break;
            default:
                h.node.setBackgroundResource(R.drawable.bg_circle_stroke);
                h.ivCheck.setVisibility(View.GONE);
                h.tvTitle.setTextColor(muted);
                h.tvTime.setTextColor(muted);
                h.tvTime.setText(s.time.isEmpty() ? "Pending" : s.time);
                break;
        }

        h.lineTop.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        h.lineBottom.setVisibility(position == items.size() - 1 ? View.INVISIBLE : View.VISIBLE);
        h.lineTop.setBackgroundColor(s.state == TrackStep.PENDING ? divider : success);
        h.lineBottom.setBackgroundColor(s.state == TrackStep.DONE ? success : divider);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        FrameLayout node;
        ImageView ivCheck;
        TextView tvTitle, tvTime;
        View lineTop, lineBottom;

        VH(@NonNull View v) {
            super(v);
            node = v.findViewById(R.id.node);
            ivCheck = v.findViewById(R.id.ivCheck);
            tvTitle = v.findViewById(R.id.tvStepTitle);
            tvTime = v.findViewById(R.id.tvStepTime);
            lineTop = v.findViewById(R.id.lineTop);
            lineBottom = v.findViewById(R.id.lineBottom);
        }
    }
}

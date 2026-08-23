package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.Payment;
import com.techfix.app.util.StatusUtil;

import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.VH> {

    private final List<Payment> items;
    private final OnItemClickListener<Payment> listener;

    public PaymentAdapter(List<Payment> items, OnItemClickListener<Payment> listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Payment p = items.get(position);
        h.tvService.setText(p.service);
        h.tvRef.setText(p.refNo);
        h.tvDate.setText(p.date);
        h.tvAmount.setText(p.amount);
        StatusUtil.applyStatus(h.tvStatus, p.status);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvService, tvRef, tvDate, tvAmount, tvStatus;

        VH(@NonNull View v) {
            super(v);
            tvService = v.findViewById(R.id.tvService);
            tvRef = v.findViewById(R.id.tvRef);
            tvDate = v.findViewById(R.id.tvDate);
            tvAmount = v.findViewById(R.id.tvAmount);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}

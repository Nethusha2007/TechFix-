package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.ServiceItem;

import java.util.List;

/**
 * Binds a list of {@link ServiceItem}. Works with both the compact home card
 * ({@code item_service_home}) and the full-width row ({@code item_service});
 * the row layout adds an optional description ({@code tvDesc}).
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.VH> {

    private final List<ServiceItem> items;
    private final int layoutRes;
    private final OnItemClickListener<ServiceItem> listener;

    public ServiceAdapter(List<ServiceItem> items, int layoutRes,
                          OnItemClickListener<ServiceItem> listener) {
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
        ServiceItem s = items.get(position);
        h.ivIcon.setImageResource(s.iconResId);
        h.tvName.setText(s.name);
        h.tvPrice.setText(s.priceText);
        if (h.tvDesc != null) {
            h.tvDesc.setText(s.description);
        }
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvPrice, tvDesc;

        VH(@NonNull View v) {
            super(v);
            ivIcon = v.findViewById(R.id.ivIcon);
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvDesc = v.findViewById(R.id.tvDesc); // null in the compact card
        }
    }
}

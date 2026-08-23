package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.Appointment;
import com.techfix.app.util.StatusUtil;

import java.util.List;

/** Binds appointment cards. Picks a device icon from the device name. */
public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.VH> {

    private final List<Appointment> items;
    private final OnItemClickListener<Appointment> listener;

    public AppointmentAdapter(List<Appointment> items, OnItemClickListener<Appointment> listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Appointment a = items.get(position);
        h.ivIcon.setImageResource(iconForDevice(a.device));
        h.tvDevice.setText(a.device);
        h.tvService.setText(a.service);
        h.tvRef.setText(a.refNo);
        h.tvDate.setText(a.dateTime);
        StatusUtil.applyStatus(h.tvStatus, a.status);
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(a);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static int iconForDevice(String device) {
        String d = device == null ? "" : device.toLowerCase();
        if (d.contains("macbook") || d.contains("mac ")) return R.drawable.ic_macbook;
        if (d.contains("ipad") || d.contains("tab")) return R.drawable.ic_tablet;
        if (d.contains("laptop") || d.contains("xps") || d.contains("dell")
                || d.contains("hp") || d.contains("lenovo") || d.contains("asus")) {
            return R.drawable.ic_laptop;
        }
        if (d.contains("desktop") || d.contains("pc")) return R.drawable.ic_desktop;
        return R.drawable.ic_mobile;
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvDevice, tvService, tvRef, tvDate, tvStatus;

        VH(@NonNull View v) {
            super(v);
            ivIcon = v.findViewById(R.id.ivIcon);
            tvDevice = v.findViewById(R.id.tvDevice);
            tvService = v.findViewById(R.id.tvService);
            tvRef = v.findViewById(R.id.tvRef);
            tvDate = v.findViewById(R.id.tvDate);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}

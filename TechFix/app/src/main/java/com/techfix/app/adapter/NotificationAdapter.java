package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.model.NotificationItem;

import java.util.List;

/** Binds notification rows; unread items get a highlighted background + dot. */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.VH> {

    private final List<NotificationItem> items;

    public NotificationAdapter(List<NotificationItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        NotificationItem n = items.get(position);
        h.ivIcon.setImageResource(n.iconResId);
        h.tvTitle.setText(n.title);
        h.tvTime.setText(n.time);
        int bg = n.unread ? R.color.infoBg : R.color.card;
        h.root.setBackgroundColor(ContextCompat.getColor(h.root.getContext(), bg));
        h.unreadDot.setVisibility(n.unread ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View root, unreadDot;
        ImageView ivIcon;
        TextView tvTitle, tvTime;

        VH(@NonNull View v) {
            super(v);
            root = v.findViewById(R.id.notifRoot);
            unreadDot = v.findViewById(R.id.unreadDot);
            ivIcon = v.findViewById(R.id.ivIcon);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTime = v.findViewById(R.id.tvTime);
        }
    }
}

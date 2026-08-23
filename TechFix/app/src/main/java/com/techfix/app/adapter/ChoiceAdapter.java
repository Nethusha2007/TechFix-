package com.techfix.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;

import java.util.List;

/** Single-selection list of text options (used by booking steps). */
public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.VH> {

    /** Called whenever the selected option changes. */
    public interface OnChoiceSelected {
        void onSelected(int index, String value);
    }

    private final List<String> items;
    private int selectedIndex;
    private final OnChoiceSelected listener;

    public ChoiceAdapter(List<String> items, int selectedIndex, OnChoiceSelected listener) {
        this.items = items;
        this.selectedIndex = selectedIndex;
        this.listener = listener;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedValue() {
        return (selectedIndex >= 0 && selectedIndex < items.size())
                ? items.get(selectedIndex) : null;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_choice, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String value = items.get(position);
        h.tvChoice.setText(value);

        boolean selected = position == selectedIndex;
        h.root.setBackgroundResource(selected
                ? R.drawable.bg_choice_selected : R.drawable.bg_choice);
        h.ivCheck.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);

        h.itemView.setOnClickListener(v -> {
            int old = selectedIndex;
            selectedIndex = h.getAdapterPosition();
            if (old >= 0) notifyItemChanged(old);
            notifyItemChanged(selectedIndex);
            if (listener != null) listener.onSelected(selectedIndex, value);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View root;
        TextView tvChoice;
        ImageView ivCheck;

        VH(@NonNull View v) {
            super(v);
            root = v.findViewById(R.id.choiceRoot);
            tvChoice = v.findViewById(R.id.tvChoice);
            ivCheck = v.findViewById(R.id.ivCheck);
        }
    }
}

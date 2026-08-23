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

import java.util.List;

/** Grid of repair photos (placeholder tiles with captions and rotating accent tints). */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.VH> {

    private static final int[] TINTS = {
            R.color.colorPrimary, R.color.success, R.color.warning, R.color.info
    };

    private final List<String> captions;

    public GalleryAdapter(List<String> captions) {
        this.captions = captions;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        h.tvCaption.setText(captions.get(position));
        int color = ContextCompat.getColor(h.itemView.getContext(),
                TINTS[position % TINTS.length]);
        h.ivPhoto.setColorFilter(color);
    }

    @Override
    public int getItemCount() {
        return captions.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvCaption;

        VH(@NonNull View v) {
            super(v);
            ivPhoto = v.findViewById(R.id.ivPhoto);
            tvCaption = v.findViewById(R.id.tvCaption);
        }
    }
}

package com.techfix.app.customer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.techfix.app.R;
import com.techfix.app.adapter.GalleryAdapter;
import com.techfix.app.data.SampleData;

/** Grid gallery of before / during / after repair photos. */
public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvGallery);
        rv.setAdapter(new GalleryAdapter(SampleData.galleryCaptions()));
    }
}

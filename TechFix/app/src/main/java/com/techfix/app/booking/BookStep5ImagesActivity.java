package com.techfix.app.booking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.techfix.app.R;
import com.techfix.app.model.Booking;
import com.techfix.app.util.ImageStore;

import java.io.File;
import java.util.List;

/** Booking step 5 of 8 — attach real photos of the device from the camera or gallery (optional). */
public class BookStep5ImagesActivity extends AppCompatActivity {

    private static final int MAX_PHOTOS = 5;

    private Booking booking;
    private LinearLayout thumbs;
    private TextView tvCount;

    private String pendingCameraPath;
    private ActivityResultLauncher<String> pickImages;
    private ActivityResultLauncher<Uri> takePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_step5);

        booking = (Booking) getIntent().getSerializableExtra("booking");
        if (booking == null) booking = new Booking();
        if (booking.imagePaths == null) booking.imagePaths = new java.util.ArrayList<>();

        ((TextView) findViewById(R.id.tvStepCount)).setText("Step 5 of 8");
        ((ProgressBar) findViewById(R.id.stepProgress)).setProgress(5);
        ((TextView) findViewById(R.id.tvStepTitle)).setText("Add photos");
        ((TextView) findViewById(R.id.tvStepSubtitle))
                .setText("Photos help our technicians prepare before you arrive.");
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        thumbs = findViewById(R.id.thumbsContainer);
        tvCount = findViewById(R.id.tvCount);

        // Gallery picker (multiple images). No storage permission needed via the system picker.
        pickImages = registerForActivityResult(
                new ActivityResultContracts.GetMultipleContents(), this::onGalleryPicked);

        // Camera capture writing to a FileProvider Uri we control.
        takePhoto = registerForActivityResult(
                new ActivityResultContracts.TakePicture(), this::onCameraResult);

        rebuildThumbs();

        findViewById(R.id.cardAddPhoto).setOnClickListener(v -> onAddPhoto());
        findViewById(R.id.btnContinue).setOnClickListener(v -> next());
        findViewById(R.id.btnSkip).setOnClickListener(v -> next());
    }

    private void onAddPhoto() {
        if (booking.imagePaths.size() >= MAX_PHOTOS) {
            Toast.makeText(this, "You can add up to " + MAX_PHOTOS + " photos.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new AlertDialog.Builder(this)
                .setTitle("Add a photo")
                .setItems(new CharSequence[]{"Take a photo", "Choose from gallery"},
                        (d, which) -> {
                            if (which == 0) launchCamera();
                            else pickImages.launch("image/*");
                        })
                .show();
    }

    private void launchCamera() {
        try {
            File file = ImageStore.newImageFile(this);
            pendingCameraPath = file.getAbsolutePath();
            Uri uri = FileProvider.getUriForFile(
                    this, getPackageName() + ".fileprovider", file);
            takePhoto.launch(uri);
        } catch (Exception e) {
            pendingCameraPath = null;
            Toast.makeText(this, "No camera app is available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void onCameraResult(Boolean success) {
        if (success != null && success && pendingCameraPath != null) {
            booking.imagePaths.add(pendingCameraPath);
            rebuildThumbs();
        } else if (pendingCameraPath != null) {
            // Capture cancelled — drop the empty placeholder file.
            new File(pendingCameraPath).delete();
        }
        pendingCameraPath = null;
    }

    private void onGalleryPicked(List<Uri> uris) {
        if (uris == null || uris.isEmpty()) return;
        int added = 0;
        for (Uri uri : uris) {
            if (booking.imagePaths.size() >= MAX_PHOTOS) {
                Toast.makeText(this, "Only the first " + MAX_PHOTOS + " photos were kept.",
                        Toast.LENGTH_SHORT).show();
                break;
            }
            String path = ImageStore.copyToInternal(this, uri);
            if (path != null) {
                booking.imagePaths.add(path);
                added++;
            }
        }
        if (added == 0 && !uris.isEmpty()) {
            Toast.makeText(this, "Couldn't add that photo. Please try another.",
                    Toast.LENGTH_SHORT).show();
        }
        rebuildThumbs();
    }

    /** Rebuilds the thumbnail strip from booking.imagePaths. */
    private void rebuildThumbs() {
        thumbs.removeAllViews();
        for (int i = 0; i < booking.imagePaths.size(); i++) {
            thumbs.addView(buildThumb(i, booking.imagePaths.get(i)));
        }
        updateCount();
    }

    private View buildThumb(int index, String path) {
        int size = dp(72);
        int margin = dp(10);

        FrameLayout tile = new FrameLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size, size);
        lp.setMarginEnd(margin);
        tile.setLayoutParams(lp);
        tile.setBackgroundResource(R.drawable.bg_input);

        ImageView photo = new ImageView(this);
        photo.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Bitmap bmp = ImageStore.decodeSampled(path, dp(96));
        if (bmp != null) {
            photo.setImageBitmap(bmp);
        } else {
            photo.setImageResource(R.drawable.ic_gallery);
            int pad = dp(18);
            photo.setPadding(pad, pad, pad, pad);
        }
        tile.addView(photo);

        // Small "remove" badge in the top-right corner (drawn in code — no drawable dependency).
        TextView remove = new TextView(this);
        int badge = dp(22);
        FrameLayout.LayoutParams bp = new FrameLayout.LayoutParams(badge, badge);
        bp.gravity = Gravity.TOP | Gravity.END;
        remove.setLayoutParams(bp);
        remove.setText("✕"); // ✕
        remove.setTextColor(Color.WHITE);
        remove.setTextSize(11);
        remove.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(0xCC000000);
        remove.setBackground(circle);
        remove.setOnClickListener(v -> {
            if (index < booking.imagePaths.size()) {
                String removed = booking.imagePaths.remove(index);
                new File(removed).delete();
                rebuildThumbs();
            }
        });
        tile.addView(remove);

        return tile;
    }

    private void updateCount() {
        int n = booking.imagePaths.size();
        tvCount.setText(n == 0 ? "No photos added"
                : n + (n == 1 ? " photo added" : " photos added"));
    }

    private void next() {
        Intent i = new Intent(this, BookStep6BranchActivity.class);
        i.putExtra("booking", booking);
        startActivity(i);
    }

    private int dp(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }
}

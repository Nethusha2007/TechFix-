package com.techfix.app.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import androidx.core.content.ContextCompat;

import com.techfix.app.model.Branch;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Map / GPS helpers: opening the device maps app with real coordinates,
 * reading the last known location, and ordering branches by distance.
 */
public final class MapUtil {

    private MapUtil() {
    }

    /**
     * Opens a maps app pinned at ({@code lat},{@code lng}). When coordinates are unknown
     * (both 0) it falls back to a text query, and if no maps app is installed it opens
     * Google Maps in the browser.
     */
    public static void openDirections(Context ctx, double lat, double lng, String label) {
        String safeLabel = (label == null || label.trim().isEmpty()) ? "TechFix" : label;
        Uri uri;
        if (lat != 0 || lng != 0) {
            uri = Uri.parse("geo:" + lat + "," + lng + "?q="
                    + lat + "," + lng + "(" + Uri.encode(safeLabel) + ")");
        } else {
            uri = Uri.parse("geo:0,0?q=" + Uri.encode(safeLabel));
        }
        Intent map = new Intent(Intent.ACTION_VIEW, uri);
        if (map.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(map);
            return;
        }
        // Fallback: open Google Maps on the web.
        Uri web = (lat != 0 || lng != 0)
                ? Uri.parse("https://www.google.com/maps/search/?api=1&query=" + lat + "," + lng)
                : Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(safeLabel));
        ctx.startActivity(new Intent(Intent.ACTION_VIEW, web));
    }

    /** True when the app currently holds a location permission. */
    public static boolean hasLocationPermission(Context ctx) {
        return ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns the best last-known device location, or {@code null} if unavailable
     * (no permission, no provider, or no cached fix). Never throws.
     */
    public static Location lastKnownLocation(Context ctx) {
        if (!hasLocationPermission(ctx)) return null;
        try {
            LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            if (lm == null) return null;
            Location best = null;
            for (String provider : lm.getProviders(true)) {
                Location l = lm.getLastKnownLocation(provider);
                if (l == null) continue;
                if (best == null || l.getAccuracy() < best.getAccuracy()) {
                    best = l;
                }
            }
            return best;
        } catch (SecurityException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Rewrites each branch's {@code distance} label from its real distance to
     * ({@code lat},{@code lng}) and sorts the list nearest-first. Branches without
     * coordinates are left unchanged and pushed to the end.
     */
    public static void sortByDistance(List<Branch> branches, double lat, double lng) {
        if (branches == null) return;
        for (Branch b : branches) {
            if (b.lat == 0 && b.lng == 0) continue;
            b.distance = String.format(Locale.US, "%.1f km", meters(lat, lng, b) / 1000.0);
        }
        Collections.sort(branches, (a, b) -> Float.compare(rank(lat, lng, a), rank(lat, lng, b)));
    }

    private static float meters(double lat, double lng, Branch b) {
        float[] r = new float[1];
        Location.distanceBetween(lat, lng, b.lat, b.lng, r);
        return r[0];
    }

    /** Branches without coordinates sort last. */
    private static float rank(double lat, double lng, Branch b) {
        if (b.lat == 0 && b.lng == 0) return Float.MAX_VALUE;
        return meters(lat, lng, b);
    }
}

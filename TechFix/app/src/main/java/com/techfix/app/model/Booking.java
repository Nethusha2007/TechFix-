package com.techfix.app.model;

import java.io.Serializable;
import java.util.ArrayList;

/** Carries the user's selections through the multi-step Book Repair flow. */
public class Booking implements Serializable {
    public String category = "";       // Step 1 (e.g. "Mobile Phone")
    public String deviceBrand = "";    // Step 2 (e.g. "Samsung")
    public String service = "";        // Step 3 (e.g. "Screen Replacement")
    public String priceText = "";      // Step 3 price label (e.g. "From LKR 4,500")
    public String problem = "";        // Step 4 problem description
    public ArrayList<String> imagePaths = new ArrayList<>(); // Step 5 local file paths of real photos
    public String branch = "";         // Step 6 (e.g. "TechFix Colombo")
    public String date = "";           // Step 7 (e.g. "20 May 2026")
    public String time = "";           // Step 7 (e.g. "10:00 AM")
    public String estimatedCost = "";  // Step 8 (e.g. "LKR 4,500.00")

    public Booking() {
    }

    /** Number of photos the customer attached. */
    public int imageCount() {
        return imagePaths == null ? 0 : imagePaths.size();
    }

    /** All photo paths as a single newline-separated string for storage in SQLite. */
    public String imagePathsJoined() {
        if (imagePaths == null || imagePaths.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imagePaths.size(); i++) {
            if (i > 0) sb.append("\n");
            sb.append(imagePaths.get(i));
        }
        return sb.toString();
    }
}

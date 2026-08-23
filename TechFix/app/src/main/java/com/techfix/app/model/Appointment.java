package com.techfix.app.model;

/** A booking / repair appointment. */
public class Appointment {
    public int id;
    public String refNo;          // e.g. "#AAP-10024"
    public String device;         // e.g. "Samsung Galaxy S21"
    public String service;        // e.g. "Battery Replacement"
    public String branch;         // e.g. "TechFix Colombo"
    public String dateTime;       // e.g. "18 May 2026 - 02:00 PM"
    public String status;         // "Pending" | "In Progress" | "Completed"
    public String estimatedCost;  // e.g. "LKR 3,200.00"
    public String customerName;   // e.g. "Nimal Perera"
    public String technicianName; // e.g. "Nuwan Perera"
    public String description;
    public int trackStep;         // 0..7 index into the 8-step tracking timeline
    public int userId;            // owner account id (set from the DB / session)
    public String imagePaths;     // newline-separated local file paths of the customer's photos

    public Appointment(int id, String refNo, String device, String service, String branch,
                       String dateTime, String status, String estimatedCost,
                       String customerName, String technicianName, String description, int trackStep) {
        this.id = id;
        this.refNo = refNo;
        this.device = device;
        this.service = service;
        this.branch = branch;
        this.dateTime = dateTime;
        this.status = status;
        this.estimatedCost = estimatedCost;
        this.customerName = customerName;
        this.technicianName = technicianName;
        this.description = description;
        this.trackStep = trackStep;
    }
}

package com.techfix.app.model;

/** A payment record. */
public class Payment {
    public int id;
    public String refNo;    // e.g. "#AAP-10023"
    public String service;  // e.g. "Software Repair"
    public String amount;   // e.g. "LKR 3,500.00"
    public String date;     // e.g. "15 May 2026"
    public String status;   // "Paid" | "Pending"

    public Payment(int id, String refNo, String service, String amount, String date, String status) {
        this.id = id;
        this.refNo = refNo;
        this.service = service;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }
}

package com.techfix.app.model;

public class Payment {
    public int id;
    public String refNo;    
    public String service;  
    public String amount;   
    public String date;     
    public String status;   

    public Payment(int id, String refNo, String service, String amount, String date, String status) {
        this.id = id;
        this.refNo = refNo;
        this.service = service;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }
}

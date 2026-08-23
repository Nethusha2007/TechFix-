package com.techfix.app.model;

public class TrackStep {
    public static final int DONE = 0;
    public static final int CURRENT = 1;
    public static final int PENDING = 2;

    public String title;   
    public String time;    
    public int state;      

    public TrackStep(String title, String time, int state) {
        this.title = title;
        this.time = time;
        this.state = state;
    }
}

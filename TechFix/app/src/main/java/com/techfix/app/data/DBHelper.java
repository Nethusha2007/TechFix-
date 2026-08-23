package com.techfix.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.techfix.app.model.Appointment;
import com.techfix.app.model.Payment;
import com.techfix.app.model.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "techfix.db";
    private static final int DB_VERSION = 4;
    private static final String T_USERS = "users";
    private static final String U_ID = "id";
    private static final String U_NAME = "full_name";
    private static final String U_EMAIL = "email";
    private static final String U_PHONE = "phone";
    private static final String U_PASSWORD = "password";
    private static final String U_ADDRESS = "address";
    private static final String U_IS_ADMIN = "is_admin";
    private static final String T_APPTS = "appointments";
    private static final String A_ID = "id";
    private static final String A_REF = "ref_no";
    private static final String A_DEVICE = "device";
    private static final String A_SERVICE = "service";
    private static final String A_BRANCH = "branch";
    private static final String A_DATETIME = "date_time";
    private static final String A_STATUS = "status";
    private static final String A_COST = "estimated_cost";
    private static final String A_CUSTOMER = "customer_name";
    private static final String A_TECH = "technician_name";
    private static final String A_DESC = "description";
    private static final String A_TRACK = "track_step";
    private static final String A_USER_ID = "user_id";  
    private static final String A_IMAGES = "images";      
    private static final String T_PAYMENTS = "payments";
    private static final String P_ID = "id";
    private static final String P_USER_ID = "user_id";
    private static final String P_REF = "ref_no";
    private static final String P_SERVICE = "service";
    private static final String P_AMOUNT = "amount";
    private static final String P_DATE = "date";
    private static final String P_STATUS = "status";
    public static final String DEMO_EMAIL = "john@techfix.com";
    public static final String DEMO_PASSWORD = "123456";
    public static final String ADMIN_EMAIL = "admin@techfix.com";
    public static final String ADMIN_PASSWORD = "admin123";

    public DBHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + T_USERS + " (" +
                U_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                U_NAME + " TEXT, " +
                U_EMAIL + " TEXT UNIQUE, " +
                U_PHONE + " TEXT, " +
                U_PASSWORD + " TEXT, " +
                U_ADDRESS + " TEXT, " +
                U_IS_ADMIN + " INTEGER DEFAULT 0)");

        db.execSQL("CREATE TABLE " + T_APPTS + " (" +
                A_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                A_REF + " TEXT, " +
                A_DEVICE + " TEXT, " +
                A_SERVICE + " TEXT, " +
                A_BRANCH + " TEXT, " +
                A_DATETIME + " TEXT, " +
                A_STATUS + " TEXT, " +
                A_COST + " TEXT, " +
                A_CUSTOMER + " TEXT, " +
                A_TECH + " TEXT, " +
                A_DESC + " TEXT, " +
                A_TRACK + " INTEGER DEFAULT 0, " +
                A_USER_ID + " INTEGER DEFAULT 0, " +
                A_IMAGES + " TEXT)");

        db.execSQL("CREATE TABLE " + T_PAYMENTS + " (" +
                P_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                P_USER_ID + " INTEGER DEFAULT 0, " +
                P_REF + " TEXT, " +
                P_SERVICE + " TEXT, " +
                P_AMOUNT + " TEXT, " +
                P_DATE + " TEXT, " +
                P_STATUS + " TEXT)");
        seedUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + T_PAYMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_APPTS);
        db.execSQL("DROP TABLE IF EXISTS " + T_USERS);
        onCreate(db);
    }

    private long seedUsers(SQLiteDatabase db) {
        ContentValues demo = new ContentValues();
        demo.put(U_NAME, "John Silva");
        demo.put(U_EMAIL, DEMO_EMAIL);
        demo.put(U_PHONE, "+94 77 123 4567");
        demo.put(U_PASSWORD, DEMO_PASSWORD);
        demo.put(U_ADDRESS, "No. 24, Galle Road, Colombo 03");
        demo.put(U_IS_ADMIN, 0);
        long demoId = db.insert(T_USERS, null, demo);

        ContentValues admin = new ContentValues();
        admin.put(U_NAME, "Admin User");
        admin.put(U_EMAIL, ADMIN_EMAIL);
        admin.put(U_PHONE, "+94 11 234 5678");
        admin.put(U_PASSWORD, ADMIN_PASSWORD);
        admin.put(U_ADDRESS, "TechFix HQ, Colombo 03");
        admin.put(U_IS_ADMIN, 1);
        db.insert(T_USERS, null, admin);

        return demoId;
    }

    public long registerUser(String fullName, String email, String phone,
                             String password, String address) {
        if (emailExists(email)) return -1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(U_NAME, fullName);
        v.put(U_EMAIL, email);
        v.put(U_PHONE, phone);
        v.put(U_PASSWORD, password);
        v.put(U_ADDRESS, address);
        v.put(U_IS_ADMIN, 0);
        return db.insert(T_USERS, null, v);
    }

    public boolean emailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, new String[]{U_ID}, U_EMAIL + "=?",
                new String[]{email}, null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public User login(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, null, U_EMAIL + "=? AND " + U_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);
        User user = null;
        if (c.moveToFirst()) {
            user = readUser(c);
        }
        c.close();
        return user;
    }

    public User getUserById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_USERS, null, U_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (c.moveToFirst()) {
            user = readUser(c);
        }
        c.close();
        return user;
    }

    public boolean updateUser(int id, String fullName, String email, String phone, String address) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(U_NAME, fullName);
        v.put(U_EMAIL, email);
        v.put(U_PHONE, phone);
        v.put(U_ADDRESS, address);
        int rows = db.update(T_USERS, v, U_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean updatePassword(String email, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(U_PASSWORD, newPassword);
        int rows = db.update(T_USERS, v, U_EMAIL + "=?", new String[]{email});
        return rows > 0;
    }

    private User readUser(Cursor c) {
        User u = new User();
        u.id = c.getInt(c.getColumnIndexOrThrow(U_ID));
        u.fullName = c.getString(c.getColumnIndexOrThrow(U_NAME));
        u.email = c.getString(c.getColumnIndexOrThrow(U_EMAIL));
        u.phone = c.getString(c.getColumnIndexOrThrow(U_PHONE));
        u.password = c.getString(c.getColumnIndexOrThrow(U_PASSWORD));
        u.address = c.getString(c.getColumnIndexOrThrow(U_ADDRESS));
        u.isAdmin = c.getInt(c.getColumnIndexOrThrow(U_IS_ADMIN)) == 1;
        return u;
    }
    
    public long insertAppointment(Appointment a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(A_USER_ID, a.userId);
        v.put(A_REF, a.refNo);
        v.put(A_DEVICE, a.device);
        v.put(A_SERVICE, a.service);
        v.put(A_BRANCH, a.branch);
        v.put(A_DATETIME, a.dateTime);
        v.put(A_STATUS, a.status);
        v.put(A_COST, a.estimatedCost);
        v.put(A_CUSTOMER, a.customerName);
        v.put(A_TECH, a.technicianName);
        v.put(A_DESC, a.description);
        v.put(A_TRACK, a.trackStep);
        v.put(A_IMAGES, a.imagePaths);
        return db.insert(T_APPTS, null, v);
    }

    public List<Appointment> getAllAppointments() {
        return query(null, null);
    }

    public List<Appointment> getAppointmentsByStatus(String status) {
        return query(A_STATUS + "=?", new String[]{status});
    }

    public List<Appointment> getActiveAppointments() {
        return query(A_STATUS + "!=?", new String[]{"Completed"});
    }

    public List<Appointment> getAppointmentsByUser(int userId) {
        return query(A_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public List<Appointment> getActiveAppointmentsByUser(int userId) {
        return query(A_USER_ID + "=? AND " + A_STATUS + "!=?",
                new String[]{String.valueOf(userId), "Completed"});
    }

    public List<Appointment> getAppointmentsByUserAndStatus(int userId, String status) {
        return query(A_USER_ID + "=? AND " + A_STATUS + "=?",
                new String[]{String.valueOf(userId), status});
    }

    public Appointment getAppointmentById(int id) {
        List<Appointment> list = query(A_ID + "=?", new String[]{String.valueOf(id)});
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean updateAppointmentStatus(int id, String status, int trackStep) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(A_STATUS, status);
        v.put(A_TRACK, trackStep);
        int rows = db.update(T_APPTS, v, A_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean assignTechnician(int id, String technicianName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(A_TECH, technicianName);
        int rows = db.update(T_APPTS, v, A_ID + "=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public int countByStatus(String status) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + T_APPTS + " WHERE " + A_STATUS + "=?",
                new String[]{status});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int countAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + T_APPTS, null);
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int countByStatusForUser(int userId, String status) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + T_APPTS
                        + " WHERE " + A_USER_ID + "=? AND " + A_STATUS + "=?",
                new String[]{String.valueOf(userId), status});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public int countAllForUser(int userId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + T_APPTS + " WHERE " + A_USER_ID + "=?",
                new String[]{String.valueOf(userId)});
        int count = 0;
        if (c.moveToFirst()) count = c.getInt(0);
        c.close();
        return count;
    }

    public long insertPayment(int userId, String refNo, String service,
                              String amount, String date, String status) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(P_USER_ID, userId);
        v.put(P_REF, refNo);
        v.put(P_SERVICE, service);
        v.put(P_AMOUNT, amount);
        v.put(P_DATE, date);
        v.put(P_STATUS, status);
        return db.insert(T_PAYMENTS, null, v);
    }

    public List<Payment> getPaymentsByUser(int userId) {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PAYMENTS, null, P_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, P_ID + " DESC");
        while (c.moveToNext()) {
            list.add(new Payment(
                    c.getInt(c.getColumnIndexOrThrow(P_ID)),
                    c.getString(c.getColumnIndexOrThrow(P_REF)),
                    c.getString(c.getColumnIndexOrThrow(P_SERVICE)),
                    c.getString(c.getColumnIndexOrThrow(P_AMOUNT)),
                    c.getString(c.getColumnIndexOrThrow(P_DATE)),
                    c.getString(c.getColumnIndexOrThrow(P_STATUS))));
        }
        c.close();
        return list;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_PAYMENTS, null, null, null, null, null, P_ID + " DESC");
        while (c.moveToNext()) {
            list.add(new Payment(
                    c.getInt(c.getColumnIndexOrThrow(P_ID)),
                    c.getString(c.getColumnIndexOrThrow(P_REF)),
                    c.getString(c.getColumnIndexOrThrow(P_SERVICE)),
                    c.getString(c.getColumnIndexOrThrow(P_AMOUNT)),
                    c.getString(c.getColumnIndexOrThrow(P_DATE)),
                    c.getString(c.getColumnIndexOrThrow(P_STATUS))));
        }
        c.close();
        return list;
    }

    private List<Appointment> query(String selection, String[] args) {
        List<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(T_APPTS, null, selection, args, null, null, A_ID + " DESC");
        while (c.moveToNext()) {
            Appointment a = new Appointment(
                    c.getInt(c.getColumnIndexOrThrow(A_ID)),
                    c.getString(c.getColumnIndexOrThrow(A_REF)),
                    c.getString(c.getColumnIndexOrThrow(A_DEVICE)),
                    c.getString(c.getColumnIndexOrThrow(A_SERVICE)),
                    c.getString(c.getColumnIndexOrThrow(A_BRANCH)),
                    c.getString(c.getColumnIndexOrThrow(A_DATETIME)),
                    c.getString(c.getColumnIndexOrThrow(A_STATUS)),
                    c.getString(c.getColumnIndexOrThrow(A_COST)),
                    c.getString(c.getColumnIndexOrThrow(A_CUSTOMER)),
                    c.getString(c.getColumnIndexOrThrow(A_TECH)),
                    c.getString(c.getColumnIndexOrThrow(A_DESC)),
                    c.getInt(c.getColumnIndexOrThrow(A_TRACK)));
            a.userId = c.getInt(c.getColumnIndexOrThrow(A_USER_ID));
            a.imagePaths = c.getString(c.getColumnIndexOrThrow(A_IMAGES));
            list.add(a);
        }
        c.close();
        return list;
    }
}

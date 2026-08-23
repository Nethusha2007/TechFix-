package com.techfix.app.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String PREFS = "techfix_session";
    private static final String KEY_LOGGED_IN = "logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_IS_ADMIN = "is_admin";

    private final SharedPreferences prefs;

    public Session(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void login(int userId, String name, String email, String phone,
                      String address, boolean isAdmin) {
        prefs.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PHONE, phone)
                .putString(KEY_ADDRESS, address)
                .putBoolean(KEY_IS_ADMIN, isAdmin)
                .apply();
    }

    public void updateProfile(String name, String email, String phone, String address) {
        prefs.edit()
                .putString(KEY_NAME, name)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PHONE, phone)
                .putString(KEY_ADDRESS, address)
                .apply();
    }

    public void logout() {
        prefs.edit().clear().apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public boolean isAdmin() {
        return prefs.getBoolean(KEY_IS_ADMIN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getName() {
        return prefs.getString(KEY_NAME, "Guest User");
    }

    public String getEmail() {
        return prefs.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return prefs.getString(KEY_PHONE, "");
    }

    public String getAddress() {
        return prefs.getString(KEY_ADDRESS, "");
    }
}

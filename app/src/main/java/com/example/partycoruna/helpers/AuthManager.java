package com.example.partycoruna.helpers;

import android.content.Context;
import android.util.Log;

/* AuthManager
 - Utilidad simple para guardar y leer el token y username en SharedPreferences.
 - Uso:
     AuthManager.saveToken(context, token);
     AuthManager.saveUsername(context, username);
     String u = AuthManager.getUsername(context);
 */
public class AuthManager {
    private static final String PREFS_NAME = "MyAppPrefs";
    private static final String KEY_TOKEN = "auth_token";
    private static final String KEY_USERNAME = "auth_username";
    private static final String TAG = "AuthManager";

    public static void saveToken(Context context, String token) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_TOKEN, token).apply();
        // DEBUG: loguear que guardamos token
        Log.d(TAG, "saveToken: token saved (len=" + (token != null ? token.length() : 0) + ")");
    }

    public static String getToken(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
    }

    public static void saveUsername(Context context, String username) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().putString(KEY_USERNAME, username).apply();
    }

    public static String getUsername(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_USERNAME, null);
    }

    public static boolean isLoggedIn(Context context) {
        return getToken(context) != null;
    }

    public static void logout(Context context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().clear().apply();
    }
}

package com.maktabti.Utils;

public class CurrentUser {
    private static int userId;
    private static String email;

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int id) {
        userId = id;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String userEmail) {
        email = userEmail;
    }

    public static void clear() {
        userId = 0;
        email = null;
    }
}

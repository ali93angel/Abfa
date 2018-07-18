package com.app.leon.abfa.Utils;

import android.util.Base64;

/**
 * Created by saeid on 9/9/2016.
 */
public class Crypto {

    private Crypto() {
    }

    //
    public static String encrypt(String password) {
        String encodedPassword_1 = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        String encodedPassword_2 = Base64.encodeToString(encodedPassword_1.getBytes(), Base64.NO_CLOSE);
        return encodedPassword_2;
    }

    public static String decrypt(String encodedPassword) {
        String encodedPassword_1 = new String(Base64.decode(encodedPassword, Base64.DEFAULT));
        String encodedPassword_2 = new String(Base64.decode(encodedPassword_1, Base64.DEFAULT));
        return encodedPassword_2;
    }
}

package com.example.admpapi;

public class Utils {

    public static String createKeyOtp(String email, String type){
        return email + ":" + type;
    }
}

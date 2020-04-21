package com.sorbSoft.CabAcademie.Services.Dtos.Helpers;

public class Helper {

    public static String getStringValue(String text) {
        if (text == null || text.equals("")) {
            return "";
        }
        return text;
    }

}

package com.grexoft.roomie.helper;

/**
 * Created by sukrit on 10/10/17.
 */

public class ApplicationContext {
    private static String userPhoto;

    public static String getCoverPhoto() {
        return coverPhoto;
    }

    private static String coverPhoto;

    public static String getUserPhoto() {
        return userPhoto;
    }

    public static void setUserPhoto(String userPhoto) {
        ApplicationContext.userPhoto = userPhoto;
    }

    public static void setCoverPhoto(String coverPhoto) {
        ApplicationContext.coverPhoto = coverPhoto;
    }
}

package com.grantsome.videoplayer.Util;

/**
 * Created by tom on 2017/5/21.
 */

public class ChangeUtils {

    private static boolean isPrevious = false;

    private static boolean isNext = false;

    public static boolean isNext() {
        return isNext;
    }

    public static boolean isPrevious() {
        return isPrevious;
    }

    public static void setNext(boolean next) {
        isNext = next;
    }

    public static void setPrevious(boolean previous) {
        isPrevious = previous;
    }

}

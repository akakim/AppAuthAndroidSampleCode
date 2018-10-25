package com.google.codelabs.appauth;



/**
 * @author KIM
 * @version 0.0.1
 * @date 2018-10-25
 * @since 0.0.1
 */
public class CurrentSystemClock implements CustomClock {
    public static final CurrentSystemClock INSTANCE = new CurrentSystemClock();

    private CurrentSystemClock() {
    }

    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}

package net.sunken.common.util;

import java.util.concurrent.TimeUnit;

public class TimeHelper {

    private Long time;

    public TimeHelper (Long milliseconds) {
        if (milliseconds < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        this.time = milliseconds;
    }

    public String getFullTime() {
        long days = TimeUnit.MILLISECONDS.toDays(time);
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        return String.format("%d days, %d hours, %d minutes and %d seconds",
                days, hours, minutes, seconds);
    }

    public String getShortTime() {
        long hours = TimeUnit.MILLISECONDS.toHours(time) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60;

        return String.format("%d hours, %d minutes and %d seconds",
                hours, minutes, seconds);
    }

}

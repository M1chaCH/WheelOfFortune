package ch.bbbaden.m151.wheeloffortune.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeParser {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LocalDateTimeParser() { }

    public static String dateToString(LocalDateTime date){
        return FORMATTER.format(date);
    }

    public static LocalDateTime stringToDate(String date){
        return LocalDateTime.parse(date, FORMATTER);
    }
}

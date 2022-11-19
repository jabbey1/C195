package utils;

import java.time.*;

public class TimeZoneUtil {

    public static LocalDateTime toGMT (LocalDateTime lTime) {
        ZonedDateTime zT = ZonedDateTime.of(lTime, ZoneId.systemDefault());
        zT = zT.withZoneSameInstant(ZoneOffset.UTC);
        LocalDateTime time = zT.toLocalDateTime();
        return time;
    }

    public static LocalDateTime fromGMT (LocalDateTime lT) {

        ZonedDateTime zT = ZonedDateTime.of(lT, ZoneId.of("GMT"));
        zT = zT.withZoneSameInstant(ZoneId.systemDefault());
        LocalDateTime time = zT.toLocalDateTime();
        return time;
    }
}

package eu.nighttrains.booking.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {
    @Override
    public LocalTime unmarshal(String timeInput) throws Exception {
        return LocalTime.parse(timeInput, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public String marshal(LocalTime localTime) throws Exception {
        return DateTimeFormatter.ISO_LOCAL_TIME.format(localTime);
    }
}

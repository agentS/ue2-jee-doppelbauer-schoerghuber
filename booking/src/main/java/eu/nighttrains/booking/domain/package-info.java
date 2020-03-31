@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type = LocalDate.class, value = LocalDateAdapter.class),
        @XmlJavaTypeAdapter(type = LocalTime.class, value = LocalTimeAdapter.class)
})
package eu.nighttrains.booking.domain;

import eu.nighttrains.booking.util.LocalDateAdapter;
import eu.nighttrains.booking.util.LocalTimeAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDate;
import java.time.LocalTime;

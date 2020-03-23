package eu.nighttrains.booking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
}

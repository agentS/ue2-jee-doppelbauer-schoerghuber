package eu.nighttrains.booking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public Ticket() {
    }

    public Ticket(String name) {
        this.name = name;
    }
}

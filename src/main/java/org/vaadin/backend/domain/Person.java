package org.vaadin.backend.domain;

import com.vividsolutions.jts.geom.Point;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A standard JPA entity, like in any other Java application.
 */
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT c FROM Person c")
    ,
    @NamedQuery(name = "Person.findByName",
            query = "SELECT c FROM Person c WHERE LOWER(c.firstName) LIKE :filter OR LOWER(c.lastName) LIKE :filter"),})
@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version
    int version;

    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    private PersonStatus status;

    @NotNull(message = "Email is required")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Must be valid email")
    private String email;

    @Lob
    private Point location;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "person", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ScheduleDetail> scheduleDetails;

    private List<Date> unavailableDays;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public PersonStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Point getLocation() {
        return location;
    }

    public boolean isPersisted() {
        return id > 0;
    }

    public List<ScheduleDetail> getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(List<ScheduleDetail> scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public List<Date> getUnavailableDays() {
        return unavailableDays;
    }

    public void setUnavailableDays(List<Date> unavailableDays) {
        this.unavailableDays = unavailableDays;
    }

}

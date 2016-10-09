/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.backend.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author moscac
 */
@NamedQueries({
    @NamedQuery(name = "ScheduleHeader.findAll", query = "SELECT c FROM ScheduleHeader c")
    ,
    @NamedQuery(name = "ScheduleHeader.findByDescription",
            query = "SELECT c FROM ScheduleHeader c WHERE LOWER(c.description) LIKE :filter OR LOWER(c.description) LIKE :filter"),})

@Entity
public class ScheduleHeader implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version
    int version;

    private String description;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date beginDate;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date endDate;

    @OneToMany(mappedBy = "scheduleHeader")
    private List<ScheduleDetail> scheduleDetails;
    
    @OneToMany(mappedBy = "scheduleHeader")
    private List<Shift> shifts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ScheduleDetail> getScheduleDetails() {
        return scheduleDetails;
    }

    public void setScheduleDetails(List<ScheduleDetail> scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
    
    public boolean isPersisted() {
        return id > 0;
    }
}

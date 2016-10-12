/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.backend.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

/**
 *
 * @author moscac
 */
@Entity
public class Shift implements Serializable {

    public Shift() {
        employeeCount = 1;
        shiftTime = "";
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version
    int version;

    @NotNull(message = "Weekday is required")
    private Weekday weekday;
    @NotNull(message = "Shift time is required")
    private String shiftTime;
    @Min(1)
    private int employeeCount;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleHeader scheduleHeader;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date shiftStart;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date shiftEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public String getShiftTime() {
        return shiftTime;
    }

    public void setShiftTime(String shiftTime) {
        this.shiftTime = shiftTime;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public ScheduleHeader getScheduleHeader() {
        return scheduleHeader;
    }

    public void setScheduleHeader(ScheduleHeader scheduleHeader) {
        this.scheduleHeader = scheduleHeader;
    }

    public Date getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(Date shiftStart) {
        this.shiftStart = shiftStart;
    }

    public Date getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(Date shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

}

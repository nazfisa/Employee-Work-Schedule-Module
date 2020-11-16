package com.assignment.assignment.model;

import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public class WorkSchedule {
    private LocalDate dateAt;
    private String startedAt;
    private String endAt;
    private String shiftName;

    public WorkSchedule() {
    }

    public WorkSchedule(LocalDate dateAt, String startedAt, String endAt, String shiftName) {
        this.dateAt = dateAt;
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.shiftName = shiftName;
    }

    public LocalDate getDateAt() {
        return dateAt;
    }

    public void setDateAt(LocalDate dateAt) {
        this.dateAt = dateAt;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
}

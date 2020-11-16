package com.assignment.assignment.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private long id;
    private long employeeId;
    private String name;
    @Embedded
    private WorkSchedule workSchedule;

    public Employee() {
    }

    public Employee(long id, long employeeId, String name, WorkSchedule workSchedule) {
        this.id = id;
        this.employeeId = employeeId;
        this.name = name;
        this.workSchedule = workSchedule;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkSchedule getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(WorkSchedule workSchedule) {
        this.workSchedule = workSchedule;
    }
}

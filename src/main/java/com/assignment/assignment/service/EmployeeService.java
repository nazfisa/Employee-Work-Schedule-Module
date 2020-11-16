package com.assignment.assignment.service;


import com.assignment.assignment.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface EmployeeService {

    public String checkValidity(MultipartFile file) throws IOException, ParseException;
    public void inputData(MultipartFile file) throws IOException, ParseException;
    public String manipulateData(String[] data) throws ParseException;
    public String checkHoliday(Set<LocalDate> dates);
    public String NameOfSlot(LocalDate date, int[] slots);
    public int beforeLastDayCheck(HashMap<LocalDate, int[]> workSchedule, Set<LocalDate> days, LocalDate date);
    public int[] slotAllocation(String startTime, String endTime, int[] slots, int flag) throws ParseException;
    public List<Employee> findAll();

}

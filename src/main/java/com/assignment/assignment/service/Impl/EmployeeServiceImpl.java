package com.assignment.assignment.service.Impl;

import com.assignment.assignment.model.Employee;
import com.assignment.assignment.model.WorkSchedule;
import com.assignment.assignment.repository.EmployeeRepository;
import com.assignment.assignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    private HashMap<Long, HashMap<LocalDate, int[]>> employees = new HashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M/dd/yyyy");

    @Override
    public String checkValidity(MultipartFile file) throws IOException, ParseException {
        String line="";
        String validityMessage="";
        BufferedReader bufferedReader = new BufferedReader
                (new FileReader(convertMultipartToFile(file)));

        bufferedReader.readLine();
        while ((line = bufferedReader.readLine())!=null){
            String[] data = line.split(",");
            String message= manipulateData(data);
            if(message.startsWith("error",8)){
                validityMessage = message;
                break;
            }
            validityMessage = message;

        }
        return validityMessage;
    }
    @Override
    public void inputData(MultipartFile file) throws IOException, ParseException {
        String line="";
        BufferedReader bufferedReader = new BufferedReader
                (new FileReader(convertMultipartToFile(file)));

        bufferedReader.readLine();
        while ((line = bufferedReader.readLine())!=null){
            String[] data = line.split(",");
            WorkSchedule workSchedule = new WorkSchedule();
            workSchedule.setDateAt(LocalDate.parse(data[1], dateFormatter));
            workSchedule.setStartedAt(data[3]);
            workSchedule.setEndAt(data[4]);
            workSchedule.setShiftName(manipulateData(data));

            Employee employee = new Employee();
            employee.setEmployeeId(Long.parseLong(data[0]));
            employee.setName(data[2]);
            employee.setWorkSchedule(workSchedule);
            employeeRepository.save(employee);

        }

    }
    private File convertMultipartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @Override
    public String manipulateData(String[] data) throws ParseException {
        String confirmationMessage="success";
        LocalDate date = LocalDate.parse(data[1], dateFormatter);
        if(employees.containsKey(Long.parseLong(data[0]))){
            HashMap<LocalDate, int[]> workSchedule = employees.get(Long.parseLong(data[0]));
            Set<LocalDate> days =  workSchedule.keySet();
            int flag = beforeLastDayCheck(workSchedule,days, date);
            if(days.contains(date)){
                int[] slots = slotAllocation(data[3],data[4],workSchedule.get(date),flag);
                workSchedule.put(date,slots);
                confirmationMessage = getConfirmationMessage(slots,confirmationMessage,date);
            }
            else{
                int[] slotsArray= new int[]{0, 0, 0};
                int[] slots= slotAllocation(data[3],data[4],slotsArray,flag);
                workSchedule.put(date,slots);

                confirmationMessage = getConfirmationMessage(slots,confirmationMessage,date);
            }
            if(days.size()>6){
                confirmationMessage = checkHoliday(days);
                employees.clear();
                return confirmationMessage+" from id "+Long.parseLong(data[0]);
            }
        }
        else{

            int[] slotsArray= new int[]{0, 0, 0};
            int[] slots= slotAllocation(data[3],data[4],slotsArray,0);
            confirmationMessage = getConfirmationMessage(slots,confirmationMessage,date);

            HashMap<LocalDate, int[]> slotsInADay = new HashMap<>();
            slotsInADay.put(date,slots);
            employees.put(Long.parseLong(data[0]), slotsInADay);
        }
        if(confirmationMessage.startsWith("error",8)){
           return confirmationMessage+" from id "+Long.parseLong(data[0])+" date at "+date;
        }
        else
        {
            return confirmationMessage;
        }
    }

    private String getConfirmationMessage(int[] slots, String confirmationMessage,LocalDate date){
        for (int i = 0; i < 3; i++) {
            if(slots[i]==3){
                employees.clear();
                confirmationMessage = "Getting error please check slot issue ";
                break;
            }
            else if(slots[i]==1 || slots[i]==2){
                confirmationMessage = NameOfSlot(date,slots);
            }
        }
        return confirmationMessage;
    }

    @Override
    public String checkHoliday(Set<LocalDate> dates) {
        String confirmedMessage ="success";
        TreeSet<LocalDate> sortedDays = new TreeSet<>(dates);
        ArrayList<LocalDate> sortedDaysInArray= new ArrayList<>(sortedDays);
        long daysBetween,sum=1;
        LocalDate dateTo=null;
        int i=0,j,k=1;
        while(i<=(int)sortedDaysInArray.size()%7){
            for ( j = k; j < 7; j++) {
                LocalDate dateFrom = sortedDaysInArray.get(j-1);
                dateTo = sortedDaysInArray.get(j);
                daysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo);
                sum += daysBetween;
                  }
            if(sum == 7){
                confirmedMessage="Getting error please check holiday issue at " +dateTo;
                break;
            }
            else {
                sum=0;
                k=j;
            }
            i++;
        }
        return confirmedMessage;
    }

    @Override
    public String NameOfSlot(LocalDate date, int[] slots) {
        String slotName="";
        if(slots[0]==1 || slots [0]==2){
            slotName=date.getDayOfWeek()+"-Morning";
        }
        else if(slots[1]==1 || slots [1]==2){
            slotName=date.getDayOfWeek()+"-Evening";
        }
        else if(slots[2]==1 || slots [2]==2){
            slotName=date.getDayOfWeek()+"-Night";
        }
        return slotName;
    }

    @Override
    public int beforeLastDayCheck(HashMap<LocalDate, int[]> workSchedule, Set<LocalDate> days, LocalDate date) {
        TreeSet<LocalDate> sortedDays = new TreeSet<>(days);

        if(sortedDays.size()>=1)
        {
            ArrayList<LocalDate> sortedDaysInArray= new ArrayList<>(sortedDays);
            LocalDate beforeLastDay = sortedDaysInArray.get(sortedDaysInArray.size()-1);
            LocalDate dateFrom = date;
            LocalDate dateTo = beforeLastDay;
            long daysBetween = ChronoUnit.DAYS.between(dateTo,dateFrom);

            int [] beforeLastDaySlots = workSchedule.get(beforeLastDay);
            if(daysBetween==1 && beforeLastDaySlots[2]==2){
                return 2;
            }
            if(daysBetween==1 && beforeLastDaySlots[2]==1){
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int[] slotAllocation(String startTime, String endTime, int[] slots, int flag) throws ParseException {
        int[] temporalSlots= new int[]{0, 0, 0};

        if (sdf.parse(startTime).equals(sdf.parse("9:00:00 PM"))  &&
                sdf.parse(endTime).equals(sdf.parse("7:59:00 AM"))) {
            temporalSlots[2]=slots[1]+1;
        }

        else if (sdf.parse(startTime).equals(sdf.parse("2:00:00 PM")) &&
                sdf.parse(endTime).equals(sdf.parse("8:59:00 PM"))) {

            temporalSlots[1]=slots[0]+1;

        }
        else if (sdf.parse(startTime).equals(sdf.parse("8:00:00 AM")) &&
                sdf.parse(endTime).equals(sdf.parse("1:59:00 PM"))) {

            temporalSlots[0]=flag+1;
        }
        return temporalSlots;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}

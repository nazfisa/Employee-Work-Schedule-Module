package com.assignment.assignment.controller;

import com.assignment.assignment.model.Employee;
import com.assignment.assignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {
    private final String UPLOAD_DIR = "src/main/resources/";
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/")
    public String List(Model model) {
        List<Employee> employees = employeeService.findAll();
        model.addAttribute("employees", employees);
        return "employee/employee_list";
    }

    @GetMapping("/addCSV")
    public String inputCsvFile(){
        return "employee/employee_form";
    }

    @PostMapping("/save")
    public String SaveCSVFile(@RequestParam("file") MultipartFile file, Model model) throws IOException, ParseException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String message= employeeService.checkValidity(file);
        if(message.startsWith("error",8)){
            model.addAttribute("errorMessage", message);
            return "employee/employee_form";
        }
        else{
            employeeService.inputData(file);
        }
        return "redirect:/employees/";
    }
}

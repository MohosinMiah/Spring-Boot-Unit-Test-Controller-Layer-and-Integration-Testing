package com.testcontrollerlayer.testcontrollerlayer.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;
import com.testcontrollerlayer.testcontrollerlayer.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee)
    {
        return employeeService.saveEmployee(employee);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployee()
    {
        return employeeService.getEmployeeList();
    }

    
    @GetMapping("/{employeeID}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeByID(@PathVariable("employeeID") Long employeeID)
    {
        return employeeService.getEmployeeById(employeeID);
    }

 
    @PutMapping("/{employeeID}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeID, @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(employeeID, employee);
        return ResponseEntity.ok(updatedEmployee);
    }


    @DeleteMapping("/{employeeID}")
    public String deleteEmployee(@PathVariable Long employeeID) {
        employeeService.deleteEmployeeById(employeeID);
        return "Deleted";
    }

}

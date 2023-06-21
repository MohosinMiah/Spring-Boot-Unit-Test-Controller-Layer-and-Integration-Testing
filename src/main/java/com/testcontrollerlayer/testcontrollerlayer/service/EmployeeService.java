package com.testcontrollerlayer.testcontrollerlayer.service;

import java.util.List;

import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;


public interface EmployeeService {
    public Employee saveEmployee(Employee employee);

    public List<Employee> getEmployeeList();

    public Employee getEmployeeById(Long Id);

    public Employee updateEmployee(Long employeeID, Employee employee);
    
    public String deleteEmployeeById(Long Id);
}
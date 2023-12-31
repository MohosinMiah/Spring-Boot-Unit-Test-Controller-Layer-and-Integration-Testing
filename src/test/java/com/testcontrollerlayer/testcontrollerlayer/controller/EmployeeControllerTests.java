package com.testcontrollerlayer.testcontrollerlayer.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.internal.matchers.Null;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;
import com.testcontrollerlayer.testcontrollerlayer.service.EmployeeService;

@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // Unit test create employees rest api
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws JsonProcessingException, Exception {

        // Given: Setup object or precondition
            Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();
            BDDMockito.given(employeeService.saveEmployee( ArgumentMatchers.any( Employee.class ) ) )
                .willAnswer(
                    ( invocation ) -> invocation.getArgument(0)
                );
        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( post("/api/employees").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(employee))
        );

        // Then: Verify the output or expected result

        response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    // Unit test for all employees rest api
    @Test
    public void givenListOfEmployees_whenGetAll_thenReturnEmployeeList() throws JsonProcessingException, Exception {

        // Given: Setup object or precondition
         Employee employee1 = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();
        
        Employee employee2 = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .departmentCode("IT")
                .build();
        
        // Create two separate ArrayLists of employees
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);

        BDDMockito.given(employeeService.getEmployeeList())
                .willReturn(employeeList);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( get("/api/employees") );

        // Then: Verify the output or expected result

        response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }


    // Unit test for get  employee by Id rest api
    @Test
    public void givenEmployeeObject_whenFindByID_thenReturnEmployeeObject() throws JsonProcessingException, Exception {

        // Given: Setup object or precondition
        long employeeId = 1L;

         Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();


        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(employee);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( get("/api/employees/{employeeId}" ,  employeeId) );

        // Then: Verify the output or expected result

        response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }


    // Unit test for get  employee by Id rest api - Negative Seneraio
    @Test
    public void givenEmployeeObject_whenFindByID_thenReturnEmptyEmployeeObject() throws JsonProcessingException, Exception {

        // Given: Setup object or precondition
        long employeeId = 1L;

         Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();


        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(null);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( get("/api/employees/{employeeId}" ,  employeeId) );

        // Then: Verify the output or expected result

        response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }


    // JUnit test case for update employee by ID 
    @Test
    public void givenEmployeeObject_whenFindByID_thenUpdateEmptyEmployeeObject() throws Exception {

        // Given: Setup object or precondition
        Long employeeId = 1L;
        
        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updatedemail@example.com")
                .departmentCode("CSE")
                .build();

        BDDMockito.given(employeeService.updateEmployee(eq(employeeId), ArgumentMatchers.any( Employee.class ) ))
                .willReturn(updatedEmployee);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( put("/api/employees/{employeeID}", employeeId).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then: Verify the output or expected result
        response
            .andDo(MockMvcResultHandlers.print())  
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())));
    }


    // JUnit test case for update employee by ID  - Negative
    @Test
    public void givenEmployeeObject_whenFindByID_thenEmptyEmployeeObject() throws Exception {

        // Given: Setup object or precondition
        Long employeeId = 1L;
        
        Employee updatedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updatedemail@example.com")
                .departmentCode("CSE")
                .build();

        BDDMockito.given(employeeService.updateEmployee(eq(employeeId), ArgumentMatchers.any( Employee.class ) ))
                .willReturn(null);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( put("/api/employees/{employeeID}", employeeId).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedEmployee))
        );

        // Then: Verify the output or expected result
        response
            .andDo(MockMvcResultHandlers.print())  
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(""));
    }


    // JUnit test case for Delete employee by ID
    @Test
    public void givenEmployeeObject_whenFindByID_thenDeleteEmployeeObject() throws Exception {

        // Given: Setup object or precondition
        Long employeeId = 1L;
        
        Employee updatedEmployee = Employee.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updatedemail@example.com")
                .departmentCode("CSE")
                .build();

        BDDMockito.given(employeeService.deleteEmployeeById(employeeId))
                .willReturn("Deleted");

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( delete("/api/employees/{employeeID}", employeeId) );

        // Then: Verify the output or expected result
        response
            .andDo(MockMvcResultHandlers.print())  
            .andExpect(MockMvcResultMatchers.content().string("Deleted"));
    }

}

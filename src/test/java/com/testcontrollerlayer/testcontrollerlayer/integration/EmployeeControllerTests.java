package com.testcontrollerlayer.testcontrollerlayer.integration;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;
import com.testcontrollerlayer.testcontrollerlayer.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class EmployeeControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        employeeRepository.deleteAll();
    }


    // Integration test create employees rest api
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws JsonProcessingException, Exception {

        // Given: Setup object or precondition
            Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();

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


    // Integration test for get all employee operations
    @Test
    public void givenEmployeeObject_whenGetAllEmployee_thenReturnAllEmployee() throws Exception {

       // Given: Setup objects or preconditions
        Employee employeeOne = Employee.builder()
            .id(1L)
            .firstName("MOHOSIN")
            .lastName("MIAH")
            .email("mohosinmiah1610@gmail.com")
            .departmentCode("CSE")
            .build();

        Employee employeeTwo = Employee.builder()
            .id(2L)  // Unique ID for the second employee
            .firstName("JOHN")
            .lastName("DOE")
            .email("johndoe@gmail.com")
            .departmentCode("IT")
            .build();

            employeeRepository.save(employeeOne);
            employeeRepository.save(employeeTwo);          

            // When: Perform the action or behavior being tested
             ResultActions response =  mockMvc.perform( get("/api/employees") );

            // Then: Verify the output or expected result
           response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
           
    }




    // Integration test for get employee find by ID operations
    @Test
    @DisplayName("Integration test for get employee find by ID operations")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() throws Exception {

       // Given: Setup object or precondition
        long employeeId = 1L;

         Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();

        Employee saveEmployee = employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( get("/api/employees/{employeeId}" ,  saveEmployee.getId()) );

        // Then: Verify the output or expected result

        response
            .andDo(MockMvcResultHandlers.print())   // Display The API Request Details -  Very help full for debugging 
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }





}

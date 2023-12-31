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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;
import com.testcontrollerlayer.testcontrollerlayer.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
// @Testcontainers
public class EmployeeControllerTestContainerTest extends AbstractionBaseTest{
    
    // @Container
    // private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.30"));

    // @DynamicPropertySource
    // static void setupProperties(DynamicPropertyRegistry registry) {
    //     registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    //     registry.add("spring.datasource.username", mySQLContainer::getUsername);
    //     registry.add("spring.datasource.password", mySQLContainer::getPassword);
    // }
 
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
            // .id(1L)
            .firstName("MOHOSIN")
            .lastName("MIAH")
            .email("mohosinmiah1610@gmail.com")
            .departmentCode("CSE")
            .build();

        Employee employeeTwo = Employee.builder()
            // .id(2L)  // Unique ID for the second employee
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



    // Integration test case for update employee by ID 
    @Test
    public void givenEmployeeObject_whenFindByID_thenUpdateEmptyEmployeeObject() throws Exception {

        // Given: Setup object or precondition
        Employee employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();
        employeeRepository.save(employee);

            Employee updateEmployee = Employee.builder()
                .firstName("updated")
                .lastName("updated")
                .email("updatewed@gmail.com")
                .departmentCode("CSE")
                .build();

        employeeRepository.save(employee);
        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( put("/api/employees/{employeeID}", employee.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateEmployee))
        );

        // Then: Verify the output or expected result
        response
            .andDo(MockMvcResultHandlers.print())  
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updateEmployee.getFirstName())));
    }


    
    // Integration test case for Delete employee by ID
    @Test
    public void givenEmployeeObject_whenFindByID_thenDeleteEmployeeObject() throws Exception {

        // Given: Setup object or precondition
        Employee employee = Employee.builder()
                .firstName("UpdatedFirstName")
                .lastName("UpdawtedLastName")
                .email("eeee@example.com")
                .departmentCode("CSE")
                .build();

        Employee saveEmployee = employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        ResultActions response =  mockMvc.perform( delete("/api/employees/{employeeID}", saveEmployee.getId() ) );

        // Then: Verify the output or expected result
        response
            .andDo(MockMvcResultHandlers.print())  
            .andExpect(MockMvcResultMatchers.content().string("Deleted"));
    }



}

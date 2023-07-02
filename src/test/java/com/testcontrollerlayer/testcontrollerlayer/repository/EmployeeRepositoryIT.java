package com.testcontrollerlayer.testcontrollerlayer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;


    @BeforeEach
    public void setupTestData(){
        // Given : Setup object or precondition
        employee = Employee.builder()
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();
    }

    // JUnit Test for save employee operation
    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSaveEmployee(){


        // When : Action of behavious that we are going to test
        Employee saveEmployee = employeeRepository.save(employee);

        // Then : Verify the output

        assertThat(saveEmployee).isNotNull();
        assertThat(saveEmployee.getId()).isGreaterThan(0);

    }

    // JUnit test for get Employee List operation
    @Test
    @DisplayName("JUnit test for get Employee List")
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){

        // Given : Setup object or precondition
        Employee employeeOne = Employee.builder()
                .firstName("MOHOSIN One")
                .lastName("MIAH One")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();

        Employee employeeTwo = Employee.builder()
                .firstName("MOHOSIN Two")
                .lastName("MIAH Two")
                .email("mohosinmiah161033@gmail.com")
                .departmentCode("CSE")
                .build();

        employeeRepository.save(employeeOne);
        employeeRepository.save(employeeTwo);

        // When : Action of behavious that we are going to test
        List<Employee> employees = employeeRepository.findAll();

        // Then : Verify the output
        assertThat(employees).isNotEmpty();
        assertThat(employees.size()).isEqualTo(2);
    }

    // JUnit test for get Employee By Id operation
    
    @Test
    @DisplayName("JUnit test for get Employee By Id")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject()
    {
        // Given : Setup object or precondition
        employeeRepository.save(employee);

        // When : Action of behavious that we are going to test
        Employee getEmployee = employeeRepository.findById(employee.getId()).get();

        // Then : Verify the output
        assertThat(getEmployee).isNotNull();
    }


    // JUnit test for get employee by email operation
    @Test
    @DisplayName("JUnit test for get employee by email operation")
    public void givenEmployeeEmail_whenFindByEmail_thenEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee = employeeRepository.findByEmail("mohosinmiah1610@gmail.com").get();

        // Then: Verify the output or expected result
        assertThat(getEmployee).isNotNull();
        assertThat(getEmployee.getEmail()).isEqualTo("mohosinmiah1610@gmail.com");
    }


     // JUnit test for get employee update operation
    @Test
    @DisplayName("JUnit test for get employee update operation")
    public void givenEmployeeObject_whenUpdate_thenEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee = employeeRepository.findById(employee.getId()).get();

        getEmployee.setFirstName("MOHOSINE UPDATE");
        getEmployee.setLastName("Last Name");
        getEmployee.setEmail("update@gmail.com");
        getEmployee.setDepartmentCode("EEE");

        Employee updatedEmployee = employeeRepository.save(getEmployee);

        // Then: Verify the output or expected result
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("update@gmail.com");
    }


    // JUnit test for delete employee operation
    @Test
    @DisplayName("JUnit test for delete employee operation")
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> deleteEmployee = employeeRepository.findById(employee.getId());

        // Then: Verify the output or expected result
        assertThat(deleteEmployee).isEmpty();
    }


    // JUnit test for custom query using JPQL with index param 
    @Test
    @DisplayName("JUnit test for custom query using JPQL with index")
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee =   employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

        // Then: Verify the output or expected result
        assertThat(getEmployee).isNotNull();
    }

    // JUnit test for custom query using JPQL with name param 
    @Test
    @DisplayName("JUnit test for custom query using JPQL with name param ")
    public void givenFirstNameAndLastName_whenFindByJPQLNameParams_thenReturnEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee =   employeeRepository.findByJPQLNameParams(employee.getFirstName(), employee.getLastName());
        
        // Then: Verify the output or expected result
        assertThat(getEmployee).isNotNull();
    }


    // JUnit test for custom query using findByNativeSQL with name index 
    @Test
    @DisplayName("JUnit test for custom query using findByNativeSQL with name param  ")
    public void givenFirstNameAndLastName_whenFindByfindByNativeSQL_thenReturnEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee =   employeeRepository.findByJPQLNameParams(employee.getFirstName(), employee.getLastName());
        
        // Then: Verify the output or expected result
        assertThat(getEmployee).isNotNull();
    }


    // JUnit test for custom query using findByNativeSQLNameParam with name param 
    @Test
    @DisplayName("JUnit test for custom query using findByNativeSQLNameParam with name param  ")
    public void givenFirstNameAndLastName_whenfindByNativeSQLNameParam_thenReturnEmployeeObject() {

        // Given: Setup object or precondition

        employeeRepository.save(employee);

        // When: Action or behavior that we are going to test
        Employee getEmployee =   employeeRepository.findByJPQLNameParams(employee.getFirstName(), employee.getLastName());
        
        // Then: Verify the output or expected result
        assertThat(getEmployee).isNotNull();
    }
}




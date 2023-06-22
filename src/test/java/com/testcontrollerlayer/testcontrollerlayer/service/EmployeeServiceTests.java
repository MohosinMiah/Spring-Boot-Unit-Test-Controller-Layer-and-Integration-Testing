package com.testcontrollerlayer.testcontrollerlayer.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.sql.ast.tree.expression.Collation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;
import com.testcontrollerlayer.testcontrollerlayer.exception.DataDuplicationException;
import com.testcontrollerlayer.testcontrollerlayer.repository.EmployeeRepository;
import com.testcontrollerlayer.testcontrollerlayer.service.Impl.EmployeeServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp()
    {
        // employeeRepository = Mockito.mock(EmployeeRepository.class);
        // employeeService    = new EmployeeServiceImpl(employeeRepository);
        employee =  Employee.builder()
                .id(1L)
                .firstName("MOHOSIN")
                .lastName("MIAH")
                .email("mohosinmiah1610@gmail.com")
                .departmentCode("CSE")
                .build();
    }

    // JQunit test for save employee opertaions
    @Test
    @DisplayName("JQunit test for save employee opertaions")
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //   Given : Setup object or precondition

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());

        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // When: Action or behavior that we are going to test
        Employee saveEmployee = employeeService.saveEmployee(employee);

        // Then: Verify the output or expected result
        assertThat(saveEmployee).isNotNull();
    }

    // JQunit test for save employee method which throws exception
    @Test
    @DisplayName("JQunit test for save employee method which throws exception")
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //   Given : Setup object or precondition

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // BDDMockito.given(employeeRepository.save(employee)).willReturn(employee); As I will get error so there is no logical meaning to save

        // When: Action or behavior that we are going to test
        Assertions.assertThrows(DataDuplicationException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        
    }

    // JQunit test for get all employee operations
    @Test
    @DisplayName("JQunit test for getEmployeeList operations")
    public void givenEmployeeObject_whenGetAllEmployee_thenReturnAllEmployee() {

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

            // Mock the behavior of the employeeRepository
            List<Employee> mockedEmployees = Arrays.asList(employeeOne, employeeTwo);
            Mockito.when(employeeRepository.findAll()).thenReturn(mockedEmployees);

            // When: Perform the action or behavior being tested
            List<Employee> employees = employeeService.getEmployeeList();

            // Then: Verify the output or expected result
            assertThat(employees).isNotNull();
            assertThat(employees).hasSize(2);
            assertThat(employees).containsExactly(employeeOne, employeeTwo);
    }

    // JQunit test for get all employee operations
    @Test
    @DisplayName("JQunit test for getEmployeeList operations")
    public void givenEmptyEmployeeObject_whenGetAllEmployee_thenReturnAllEmptyEmployee() {

       // Given: Setup objects or preconditions
        Employee employeeOne = Employee.builder()
            .id(1L)
            .firstName("MOHOSIN")
            .lastName("MIAH")
            .email("mohosinmiah1610@gmail.com")
            .departmentCode("CSE")
            .build();


            // Mock the behavior of the employeeRepository
            Mockito.when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

            // When: Perform the action or behavior being tested
            List<Employee> employees = employeeService.getEmployeeList();

            // Then: Verify the output or expected result
            assertThat(employees).isNotNull();
            assertThat(employees).hasSize(0);
    }


    // JQunit test for get employee find by ID operations
    @Test
    @DisplayName("JQunit test for get employee find by ID operations")
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

       // Given: Setup objects or preconditions
        // BDDMockito.given(employeeRepository.findById(employee.getId()).get()).willReturn(employee);
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // Mock the behavior of the employeeRepository
        Mockito.when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        // When: Perform the action or behavior being tested
        Employee employeeByID = employeeService.getEmployeeById(employee.getId());

        // Then: Verify the output or expected result
        assertThat(employeeByID).isNotNull();

    }

    // JQunit test for update employee by ID operations
    @Test
    @DisplayName("JQunit test for update employee by ID operations")
    public void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployeeObject() {
    // Given: Setup objects or preconditions
        Long employeeId = 123L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        // Mock the behavior of the employeeRepository
        Employee existingEmployee = new Employee();
        existingEmployee.setId(employeeId);
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        Mockito.when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        // When: Perform the action or behavior being tested
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee);

        // Then: Verify the output or expected result
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getId()).isEqualTo(employeeId);
    }

    // JQunit test for delete employee by ID operations
    @Test
    @DisplayName("JUnit test for deleteEmployee operation")
    public void givenValidEmployeeId_whenDeleteEmployee_thenEmployeeDeletedSuccessfully() {
        // Given: Setup objects or preconditions
        final Long employeeId = employee.getId();
        Mockito.when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When: Perform the action or behavior being tested
        String result = employeeService.deleteEmployeeById(employeeId);

        // Then: Verify the output or expected result
        Mockito.verify(employeeRepository).deleteById(employeeId);
        assertThat(result).isEqualTo("Delete Successfully");
    }


}

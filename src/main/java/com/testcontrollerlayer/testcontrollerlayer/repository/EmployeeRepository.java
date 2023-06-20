package com.testcontrollerlayer.testcontrollerlayer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.testcontrollerlayer.testcontrollerlayer.entity.Employee;



@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long>{

    Optional<Employee> findByEmail(String email);

    // Define custom query using JPQL with index params
    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);
    
    // Define custom query using JPQL with name params
    @Query("select e from Employee e where e.firstName = :firstName and e.lastName = :lastName")
    // Employee findByJPQLNameParams(String firstName, String lastName);  // This is also works fine because params name and parameter name is sanme
    Employee findByJPQLNameParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    // Define custom query native sql query with index param
    @Query(value= "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true )
    Employee findByNativeSQL(String firstName, String lastName);


    // Define custom query native sql query with name param
    @Query(value= "select * from employees e where e.first_name =:firstName and e.last_name = :lastName", nativeQuery = true )
    Employee findByNativeSQLNameParam(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
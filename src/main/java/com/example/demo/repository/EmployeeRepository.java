package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Integer >
{

    @Query("SELECT e FROM Employee e WHERE CONCAT(e.firstName,e.lastName,e.nickName) LIKE CONCAT('%',:q ,'%')")
    List<Employee> newQuery(@Param("q") String q);
}

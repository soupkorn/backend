package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "Employee" )
public class Employee {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column ( name = "id" )
    private int id;

    @Column ( name = "first_name" )
    private String firstName;

    @Column ( name = "last_name" )
    private String lastName;

    @Column ( name = "nickname" )
    private String nickName;

    @Column ( name = "salary" )
    private Double salary;

    @Column ( name = "status" )
    private String status;

    @Column ( name = "address" )
    private String address;

    @Column ( name = "position" )
    private String position;

    public Employee(String firstName, String lastName, String nickName, Double salary, String status, String address, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.nickName = nickName;
        this.salary = salary;
        this.status = status;
        this.address = address;
        this.position = position;
    }
}

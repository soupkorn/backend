package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(EmployeeRepository repo){
		return runner -> {
			repo.save(new Employee("f1","l1","n1",1.0,"current","a1","p1"));
			repo.save(new Employee("f2","l2","n2",2.0,"current","a2","p2"));
			repo.save(new Employee("f3","l3","n3",3.0,"current","a3","p3"));
			repo.save(new Employee("f4","l4","n4",4.0,"current","a4","p4"));
		};
	}

}

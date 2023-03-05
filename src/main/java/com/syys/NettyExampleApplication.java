package com.syys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@SpringBootApplication
public class NettyExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyExampleApplication.class, args);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener(EmployeeService employeeService){
        return event -> {
            System.out.println(event);
            employeeService.all().forEach(System.out::println);
        };
    }

    @RestController
    @RequestMapping("employee")
    class EmployeeController {
        private final EmployeeService employeeService;


        EmployeeController(EmployeeService employeeService) {
            this.employeeService = employeeService;
        }

        @RequestMapping("all")
        Collection<Employee> all() {
            return employeeService.all();
        }

        @RequestMapping("byName/{name}")
        Employee byName(@PathVariable String name){
            return employeeService.byName(name);
        }

        @RequestMapping("byId/{id}")
        Employee byId(@PathVariable Long id){
            return employeeService.byId(id);
        }
    }


    @Service
    class EmployeeService {
        private final JdbcTemplate jdbcTemplate;
        private final RowMapper<Employee> employeeRowMapper =
            (rs, rowNum) -> new Employee(rs.getLong("id"), rs.getString("name"));


        EmployeeService(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        Collection<Employee> all() {
            return jdbcTemplate.query("select * from employee", employeeRowMapper);
        }

        Employee byId(Long id) {
            return jdbcTemplate.queryForObject("select * from employee where id = ?",this.employeeRowMapper, id);
        }

        Employee byName(String name) {
            return jdbcTemplate.queryForObject("select * from employee where name = ?",this.employeeRowMapper, name);
        }
    }


    record Employee(long id, String name) {
    }


}

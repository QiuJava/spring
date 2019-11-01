package com.example;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.entity.Employee;
import com.example.mapper.EmployeeMapper;

/**
 * 应用测试
 *
 * @author Qiu Jian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisApplicationTest {

	@Autowired
	private EmployeeMapper employeeMapper;

	@Test
	public void contextLoads() {
		Employee employee = new Employee();
		employee.setUsername("admin2");
		employee.setPassword("admin2");
		Employee employee2 = new Employee();
		employee2.setUsername("admin2");
		employee2.setPassword("admin2");

		List<Employee> employeeList = new ArrayList<>();
		employeeList.add(employee);
		employeeList.add(employee2);
		employeeList.add(employee);
		employeeList.add(employee2);
		employeeList.add(employee);
		employeeList.add(employee2);
		employeeList.add(employee);
		employeeList.add(employee2);
		employeeList.add(employee);
		employeeList.add(employee2);
		employeeList.add(employee);
		employeeList.add(employee2);
		try {
			employeeMapper.insertList(employeeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

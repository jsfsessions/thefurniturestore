package com.web.view;

import com.business.entity.Employee;
import com.business.service.EmployeeService;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class EmployeesBean implements Serializable {

    private static final long serialVersionUID = 1659701197857945265L;

    @EJB
    private EmployeeService employeeService;

    private List<Employee> employees;

    public List<Employee> getEmployees() {
        if (employees == null) {
            employees = employeeService.findAll();
        }
        return employees;
    }

    /* To avoid hitting the database with SELECT queries when we switch between pages, keep all employees
     * into a session bean. After a new employees has been registered, destroy the List holding the employees
     * so that it will reinitialize.
     */
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

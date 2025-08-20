package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 保存员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工状态
     * @param status 状态 0禁用 1启用
     * @param id 员工id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}

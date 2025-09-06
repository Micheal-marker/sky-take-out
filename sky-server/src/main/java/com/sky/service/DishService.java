package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜品管理服务接口
 * 负责处理菜品相关的业务逻辑
 */
public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 启用、禁用菜品
     * @param status 状态 0禁用 1启用
     * @param id 菜品ID
     */
    void startOrStop(Integer status, Long id);

    /**
     * 批量删除菜品
     * @param ids 菜品ID列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品，同时更新对应的口味数据
     * @param dishDTO 菜品数据传输对象，包含菜品信息和口味信息
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据分类ID查询菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据条件查询菜品及其口味信息
     * @param dish 菜品实体对象，包含查询条件
     * @return 包含口味信息的菜品视图对象列表
     */
    List<DishVO> listWithFlavor(Dish dish);
}

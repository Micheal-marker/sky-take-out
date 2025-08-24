package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    /**
     * 根据分类ID查询菜品数量
     * @param id 分类ID
     * @return 菜品数量
     */
    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM dish WHERE category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 新增菜品
     * @param dish 菜品实体对象
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
}

package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品ID查询关联的套餐ID
     * @param dishIds 菜品ID列表
     * @return 关联的套餐ID列表
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}

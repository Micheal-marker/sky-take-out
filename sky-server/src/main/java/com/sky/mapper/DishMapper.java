package com.sky.mapper;

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
}

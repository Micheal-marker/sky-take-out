package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID查询套餐数量
     * @param id 分类ID
     * @return 套餐数量
     */
    @org.apache.ibatis.annotations.Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{id}")
    Integer countByCategoryId(Long id);
}

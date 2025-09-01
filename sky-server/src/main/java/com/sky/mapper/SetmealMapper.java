package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
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

    /**
     * 新增套餐
     * @param setmeal 套餐实体对象
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);
}

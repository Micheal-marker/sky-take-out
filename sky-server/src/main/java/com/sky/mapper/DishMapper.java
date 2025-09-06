package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据ID查询菜品
     * @param id 菜品ID
     * @return 菜品实体对象
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据主键删除菜品
     * @param id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 批量根据主键删除菜品数据
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 更新菜品
     * @param dish 菜品实体对象
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类ID查询菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    @Select("select * from dish where category_id = #{categoryId} order by update_time desc")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据条件查询菜品
     * @param dish 菜品实体对象，包含查询条件
     * @return 菜品列表
     */
    List<Dish> list(Dish dish);
}

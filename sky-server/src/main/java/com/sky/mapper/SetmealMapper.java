package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类ID查询套餐数量
     * @param id 分类ID
     * @return 套餐数量
     */
    @Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{id}")
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal 套餐实体对象
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 更新套餐信息
     * @param setmeal 套餐实体对象
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 根据ID查询套餐信息
     * @param id
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal getById(Long id);

    /**
     * 批量根据主键删除套餐数据
     * @param ids
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据条件查询套餐及其分类信息
     * @param setmeal 条件封装对象
     * @return 套餐列表
     */
    List<SetmealVO> list(Setmeal setmeal);

    /**
     * 根据套餐ID查询对应的菜品信息
     * @param setmealId 套餐ID
     * @return 菜品信息列表
     */
    @Select("SELECT sd.name, sd.copies, d.image, d.description " +
            "FROM setmeal_dish sd " +
            "LEFT JOIN dish d ON d.id = sd.dish_id " +
            "WHERE sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemById(Long setmealId);

    /**
     * 根据动态条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}

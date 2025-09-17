package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealOverViewVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDTO
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据ID查询套餐信息和对应的菜品信息
     * @param id
     * @return
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐信息，同时更新套餐和菜品的关联关系
     * @param setmealDTO
     */
    void updateWithDish(SetmealDTO setmealDTO);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据条件查询套餐
     * @param setmeal
     * @return
     */
    List<SetmealVO> list(Setmeal setmeal);

    /**
     * 根据套餐id查询对应的菜品信息
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}

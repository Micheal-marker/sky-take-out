package com.sky.service.impl;

import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     * @param setmealDTO 套餐数据传输对象，包含套餐信息和关联的菜品信息
     */
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        // 将DTO对象的属性复制到实体对象中
         BeanUtils.copyProperties(setmealDTO, setmeal);

        // 保存套餐信息到数据库
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();

        // 保存套餐和菜品的关联关系到数据库
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->{
            item.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }
}

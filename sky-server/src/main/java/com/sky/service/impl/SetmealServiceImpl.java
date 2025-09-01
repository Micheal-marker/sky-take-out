package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
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

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 设置分页参数
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        // 执行分页查询
        Page<Setmeal> setmealList = setmealMapper.pageQuery(setmealPageQueryDTO);

        // 获取总记录数和当前页的数据
        long total = setmealList.getTotal();
        List<Setmeal> records = setmealList.getResult();

        // 返回分页结果
        return new PageResult(total, records);
    }

    /**
     * 套餐起售、停售
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据ID查询套餐信息和对应的菜品信息
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        // 查询套餐信息
        Setmeal setmeal = setmealMapper.getById(id);

        // 查询套餐对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        // 封装数据到SetmealVO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐信息，同时更新套餐和菜品的关联关系
     * @param setmealDTO
     */
    public void updateWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        // 将DTO对象的属性复制到实体对象中
        BeanUtils.copyProperties(setmealDTO, setmeal);

        // 更新套餐信息到数据库
        setmealMapper.update(setmeal);
        Long setmealId = setmeal.getId();

        // 删除套餐和菜品的关联关系
        setmealDishMapper.deleteBySetmealId(setmealId);

        // 保存套餐和菜品的关联关系到数据库
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(item->{
            item.setSetmealId(setmealId);
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }
}

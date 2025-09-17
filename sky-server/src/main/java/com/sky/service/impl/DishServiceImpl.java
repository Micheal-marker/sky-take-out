package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.DishVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDTO 菜品数据传输对象，包含菜品信息和口味信息
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 将DTO对象的属性复制到实体对象中
        BeanUtils.copyProperties(dishDTO, dish);

        // 保存菜品信息到数据库
        dishMapper.insert(dish);

        // 获取菜品ID
        Long dishId = dish.getId();

        // 将口味信息保存到数据库
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0){
            flavors.forEach(flavor->{
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 分页查询数据传输对象
     * @return 分页结果
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用、禁用菜品
     * @param status 状态 0禁用 1启用
     * @param id 菜品ID
     */
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 批量删除菜品
     * @param ids 菜品ID列表
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断菜品是否起售
        for(Long id : ids){
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断菜品是否关联了套餐
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 删除菜品数据
        // 删除菜品口味数据
//        for(Long id : ids){
//            dishMapper.deleteById(id);
//            dishFlavorMapper.deleteByDishId(id);
//        }

        // 批量删除菜品数据
        dishMapper.deleteByIds(ids);

        // 批量删除菜品口味数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    public DishVO getByIdWithFlavor(Long id) {
        // 查询菜品基本信息
        Dish dish = dishMapper.getById(id);

        // 查询菜品口味信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        // 封装成DishVO对象并返回
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品，同时更新对应的口味数据
     * @param dishDTO 菜品数据传输对象，包含菜品信息和口味信息
     */
    public void updateWithFlavor(DishDTO dishDTO) {
        // 修改菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 删除原有的口味数据
        Long dishId = dishDTO.getId();
        dishFlavorMapper.deleteByDishId(dishId);

        // 添加新的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
        }
        dishFlavorMapper.insertBatch(flavors);
    }

    /**
     * 根据分类ID查询菜品
     * @param categoryId 分类ID
     * @return 菜品列表
     */
    public List<Dish> getByCategoryId(Long categoryId) {
        List<Dish> dishList = dishMapper.getByCategoryId(categoryId);
        return dishList;
    }

    /**
     * 根据条件查询菜品及其口味信息
     * @param dish 菜品实体对象，包含查询条件
     * @return 包含口味信息的菜品视图对象列表
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        // 创建返回列表
        List<DishVO> dishVOList = new ArrayList<>();

        // 查询菜品列表
        List<Dish> dishList = dishMapper.list(dish);

        // 查询口味列表
        for(Dish d : dishList){
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            Long dishId = d.getId();
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dishId);

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}

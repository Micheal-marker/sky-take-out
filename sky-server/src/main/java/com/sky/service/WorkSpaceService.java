package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

public interface WorkSpaceService {

    /**
     * 查询今日运营数据
     * @param beginTime
     * @param endTime
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime beginTime, LocalDateTime endTime);

    /**
     * 获取订单数据总览
     * @return
     */
    OrderOverViewVO getOrderOverview();

    /**
     * 获取菜品总览
     * @return
     */
    DishOverViewVO getDishOverview();

    /**
     * 获取套餐总览
     * @return
     */
    SetmealOverViewVO getSetmealOverView();
}

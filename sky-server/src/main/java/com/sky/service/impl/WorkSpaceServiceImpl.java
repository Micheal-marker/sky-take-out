package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 查询今日运营数据
     * @param beginTime
     * @param endTime
     * @return
     */
    public BusinessDataVO getBusinessData(LocalDateTime beginTime, LocalDateTime endTime) {
        /**
         * 新增用户：当日新增用户的数量
         * 营业额：当日已完成订单的总金额
         * 有效订单：当日已完成订单的数量
         * 订单完成率：有效订单数 / 总订单数
         * 平均客单价：营业额 / 有效订单数
         */
        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);

        // 查询新增用户数
        Integer newUsers = userMapper.sumByMap(map);

        // 查询总订单数
        Integer orderCount = orderMapper.countByMap(map);

        map.put("status", Orders.COMPLETED);

        // 查询今日营业额
        Double turnover = orderMapper.sumByMap(map);
        turnover = turnover == null? 0.0 : turnover;

        // 查询有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);
        // 计算订单完成率
        Double orderCompletionRate = 0.0;
        if(orderCount > 0) {
            orderCompletionRate = validOrderCount.doubleValue() / orderCount;
        }

        // 计算平均客单价
        Double unitPrice = 0.0;
        if(validOrderCount > 0) {
            unitPrice = turnover.doubleValue() / validOrderCount;
        }

        return BusinessDataVO.builder()
                    .newUsers(newUsers)
                    .orderCompletionRate(orderCompletionRate)
                    .turnover(turnover)
                    .unitPrice(unitPrice)
                    .validOrderCount(validOrderCount)
                    .build();
    }

    /**
     * 获取订单数据总览
     * @return
     */
    public OrderOverViewVO getOrderOverview() {
        LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);

        // 查询待接单订单
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map);
        // 查询待派送订单
        map.put("status", Orders.CONFIRMED);
        Integer deliveredOrders = orderMapper.countByMap(map);
        // 查询已完成订单
        map.put("status", Orders.COMPLETED);
        Integer completedOrders = orderMapper.countByMap(map);
        // 查询已取消订单
        map.put("status", Orders.CANCELLED);
        Integer cancelledOrders = orderMapper.countByMap(map);
        // 查询全部订单
        map.put("status", null);
        Integer allOrders = orderMapper.countByMap(map);

        return OrderOverViewVO.builder()
                    .allOrders(allOrders)
                    .waitingOrders(waitingOrders)
                    .deliveredOrders(deliveredOrders)
                    .completedOrders(completedOrders)
                    .cancelledOrders(cancelledOrders)
                    .build();
    }

    /**
     * 获取菜品总览
     * @return
     */
    public DishOverViewVO getDishOverview() {
        Map map = new HashMap();

        // 已启售菜品数量
        map.put("status", StatusConstant.ENABLE);
        Integer sold = dishMapper.countByMap(map);

        // 已停售菜品数量
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map);

        return DishOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }

    /**
     * 获取套餐总览
     * @return
     */
    public SetmealOverViewVO getSetmealOverView() {
        Map map = new HashMap();

        // 已启售菜品数量
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map);

        // 已停售菜品数量
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map);

        return SetmealOverViewVO.builder()
                .sold(sold)
                .discontinued(discontinued)
                .build();
    }
}

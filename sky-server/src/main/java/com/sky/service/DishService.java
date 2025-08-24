package com.sky.service;

import com.sky.dto.DishDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 菜品管理服务接口
 * 负责处理菜品相关的业务逻辑
 */
public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);
}

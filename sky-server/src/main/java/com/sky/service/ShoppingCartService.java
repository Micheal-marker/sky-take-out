package com.sky.service;

import com.sky.dto.ShoppingCartDTO;

public interface ShoppingCartService {

    /**
     * 添加商品到购物车
     * @param shoppingCartDTO 购物车实体
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);
}

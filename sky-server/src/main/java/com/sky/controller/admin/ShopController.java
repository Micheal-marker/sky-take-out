package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺的营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺的营业状态")
    public Result setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为：status={}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, String.valueOf(status));
        return Result.success();
    }

    /**
     * 获取店铺营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus() {
        String statusStr  = (String) redisTemplate.opsForValue().get(KEY);
        if(statusStr == null) {
            // 默认打烊
            statusStr = "0";
        }
        Integer status = Integer.parseInt(statusStr);

        log.info("获取到的店铺营业状态为：status={}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }
}

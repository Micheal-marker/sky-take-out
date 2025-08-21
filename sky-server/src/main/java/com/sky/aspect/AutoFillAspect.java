package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自动填充切面
 * 用于在实体类的插入和更新操作时自动填充公共字段
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 定义切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill))")
    public void autoFillPointCut() {}

    /**
     * 前置通知
     * 在执行被 @AutoFill 注解的方法之前，自动填充公共字段
     */
    @Before("autoFillPointCut()")
    public void autofill(JoinPoint joinPoint) {
        log.info("自动填充公共字段...");

        // 获取被拦截的方法上的操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature(); // 方法签名（用于唯一标识一个方法的信息组合）
        AutoFill autoFillAnnotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFillAnnotation.value();

        // 获取被拦截的方法的参数 -- 实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }

        Object entity = args[0]; // 假设第一个参数是实体对象

        // 准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();

        // 根据操作类型（INSERT 或 UPDATE），为对应的属性通过反射进行赋值
        if(operationType == OperationType.INSERT) {
            // 插入操作，4个公共字段：创建时间、更新时间、创建人、更新人
            try {
                // 设置创建时间
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                // 设置更新时间
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                // 设置创建人
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                // 设置更新人
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用方法进行赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, userId);
                setUpdateUser.invoke(entity, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            // 更新操作，2个公共字段：更新时间、更新人
            try {
                // 设置更新时间
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                // 设置更新人
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用方法进行赋值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

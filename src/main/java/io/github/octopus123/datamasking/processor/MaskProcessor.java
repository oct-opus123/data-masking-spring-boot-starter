package io.github.octopus123.datamasking.processor;

import io.github.octopus123.datamasking.annotation.Mask;
import io.github.octopus123.datamasking.factory.MaskStrategyFactory;
import io.github.octopus123.datamasking.stragety.MaskStrategy;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 通过反射获取 策略模式 执行策略
 */
public class MaskProcessor {
    private final MaskStrategyFactory maskStrategyFactory;

    public MaskProcessor(MaskStrategyFactory maskStrategyFactory) {
        this.maskStrategyFactory = maskStrategyFactory;
    }

    /**
     * 对对象中标记了 @Mask 的字段进行脱敏
     *
     * @param obj 待脱敏的对象
     */
    public void process(Object obj) {
        if (obj == null) {
            return;
        }
        // 处理集合类型
        if (obj instanceof List<?> list) {
            for (Object item : list) {
                process(item);
            }
            return;
        }
        // 不是普通业务对象，不处理
        if (!isNormalObject(obj)) {
            return;
        }
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            // 判断字段上是否存在 @Mask注解
            Mask mask = field.getAnnotation(Mask.class);
            if (mask != null) {
                // 执行Mask只能用String类型
                if (field.getType() != String.class) {
                    throw new IllegalArgumentException(
                            "@Mask 只能用于 String 类型字段: " + field.getName()
                    );
                }
                // 执行脱敏
                // 通过工厂模式根据策略类型找到对应的策略类
                MaskStrategy strategy = maskStrategyFactory.getStrategy(mask.type());

                // 执行策略方法
                if (strategy == null) {
                    continue;
                }
                try {
                    // 突破字段私有访问限制
                    field.setAccessible(true);
                    // 获取字段的原始值
                    Object value = field.get(obj);


                    if (value == null) {
                        continue;
                    }
                    // 执行脱敏
                    String result = strategy.mask(value.toString(), mask.character(), mask.left(), mask.right());

                    // 设置脱敏后的值
                    field.set(obj, result);


                } catch (IllegalAccessException e) {
                    throw new RuntimeException(
                            "字段脱敏失败: " + field.getName(), e
                    );
                }
                continue;
            }

            // 字段上没有@Mask时
            Object value;
            // 看一下是不是自定义的业务对象
            try {
                field.setAccessible(true);
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            // 为空则重新来
            if (value == null) {
                continue;
            }
            if (isNormalObject(value)) {
                process(value);
            }

        }
    }


    /**
     *
     * @param clazz 对象
     * @return
     */
    private boolean isSimpleType(Class<?> clazz) {
        return clazz == String.class
                || clazz == Boolean.class
                || Number.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || clazz == Character.class
                || clazz.getName().startsWith("java.");
    }

    /**
     * 是不是普通业务定义的对象
     *
     * @param value 对象
     * @return
     */
    private boolean isNormalObject(Object value) {
        if (value == null) {
            return false;
        }

        Class<?> clazz = value.getClass();
        // 判断是不是基本类型，是不是枚举类型，是不是其他类型或者java包下的类型 都不是那就是业务定义的普通对象，直接执行脱敏
        return !clazz.isPrimitive()
                && !clazz.isEnum()
                && !Collection.class.isAssignableFrom(clazz)
                && !isSimpleType(clazz);
    }

}

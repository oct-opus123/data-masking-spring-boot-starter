package io.github.octopus123.datamasking.processor;

import io.github.octopus123.datamasking.annotation.Mask;
import io.github.octopus123.datamasking.factory.MaskStrategyFactory;
import io.github.octopus123.datamasking.stragety.MaskStrategy;

import java.lang.reflect.Field;

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
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {

            // 判断字段上是否存在 @Mask注解
            Mask mask = field.getAnnotation(Mask.class);
            if (mask == null) {
                continue;
            }
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

        }
    }
}

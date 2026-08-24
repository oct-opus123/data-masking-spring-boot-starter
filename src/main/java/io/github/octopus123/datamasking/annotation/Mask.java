package io.github.octopus123.datamasking.annotation;


import io.github.octopus123.datamasking.enums.MaskType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解只在字段上有效
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mask {

    /**
     * 指定字段的脱敏类型
     *
     * @return 脱敏类型
     */
    MaskType type();

    /**
     * 脱敏字符
     */
    char character() default '*';

    /**
     * 左侧保留位数
     */
    int left() default 0;

    /**
     * 右侧保留位数
     */
    int right() default 0;
}

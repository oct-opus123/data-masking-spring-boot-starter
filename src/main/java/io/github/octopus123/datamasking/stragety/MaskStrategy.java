package io.github.octopus123.datamasking.stragety;


import io.github.octopus123.datamasking.enums.MaskType;

/**
 * 脱敏接口
 */
public interface MaskStrategy {


    /**
     * 数据脱敏
     *
     * @param str       原始字符串
     * @param character 脱敏符号
     * @param left      左侧保留位数
     * @param right     右侧保留位数
     * @return 脱敏后的数据
     */
    String mask(String str, char character, int left, int right);


    MaskType type();
}

package io.github.octopus123.datamasking.stragety;

import io.github.octopus123.datamasking.enums.MaskType;

/**
 * 邮箱策略模式
 */
public class EmalMaskStrategy implements MaskStrategy {


    /**
     *
     * @param str       原始字符串
     * @param character 脱敏符号
     * @param left      左侧保留位数
     * @param right     右侧保留位数
     * @return
     */
    @Override
    public String mask(String str, char character, int left, int right) {
        return "";
    }

    @Override
    public MaskType type() {
        return MaskType.EMAIL;
    }
}

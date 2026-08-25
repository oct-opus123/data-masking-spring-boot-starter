package io.github.octopus123.datamasking.stragety;

import io.github.octopus123.datamasking.enums.MaskType;

/**
 * 手机号策略模式
 */
public class PhoneMaskStrategy implements MaskStrategy {


    /**
     *
     * @param str       原始字符串
     * @param character 脱敏符号
     * @param left      左侧保留位数
     * @param right     右侧保留位数
     * @return 脱敏后的字符串
     */
    @Override
    public String mask(String str, char character, int left, int right) {

        // 如果参数上没有指定，那么默认手机号左侧保留3位，右侧保留4位
        if (left == 0 && right == 0) {
            left = 3;
            right = 4;
        }

        return maskByPosition(str, character, left, right);
    }

    @Override
    public MaskType type() {
        return MaskType.PHONE;
    }
}

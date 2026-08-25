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
        if (str == null || str.isEmpty()) {
            return str;
        }
        // 可能left 和right传递负数
        if (left < 0 || right < 0) {
            throw new IllegalArgumentException("脱敏参数错误：left 和 right 不能为负数");
        }
        int len = str.length();
        // 保留全部
        if (left + right >= len) {
            return str;
        }
        // 左侧保留
        return str.substring(0, left) +
                // 中间脱敏
                String.valueOf(character).repeat(Math.max(0, len - right - left)) +
                // 右侧保留
                str.substring(len - right, len);
    }

    @Override
    public MaskType type() {
        return MaskType.PHONE;
    }
}

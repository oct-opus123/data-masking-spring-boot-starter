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
        if (str == null) return "";
        // 找到@符号，@符号右边不做处理
        int atIndex = str.indexOf('@');
        // 不合法的邮箱不进行脱敏
        if (atIndex <= 0 || atIndex == str.length() - 1) {
            return str;
        }
        if (!canMask(str, atIndex, left, right)) {
            return str;
        }
        return str.substring(0, left) // 左侧处理
                        + String.valueOf(character).repeat(atIndex - left - right) // 中间处理
                        + str.substring(atIndex - right, atIndex)// 右侧处理和@符号左侧处理
                        + str.substring(atIndex);// @符号右侧处理


    }

    @Override
    public MaskType type() {
        return MaskType.EMAIL;
    }
}

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

    /**
     * 具体策略的类型
     *
     * @return
     */
    MaskType type();

    /**
     * 公共的方法处理边界问题
     *
     * @param str          原始字符串
     * @param targetLength 目标可脱敏数据长度
     * @param left         左侧保留位数
     * @param right        右侧保留位数
     * @return 根据所匹配的返回false，则对数据脱敏
     */
    default boolean canMask(String str, int targetLength, int left, int right) {

        if (str == null || str.isEmpty()) {
            return false;
        }

        if (left < 0 || right < 0) {
            throw new IllegalArgumentException(
                    "脱敏参数错误：left 和 right 不能为负数"
            );
        }
        // 左侧保留+右侧保留 小于目标脱敏数据长度，则可以进行脱敏，返回false
        return left + right < targetLength;
    }

    /**
     * 左右保留，中间脱敏的通用方法
     *
     * @param str       原始字符串
     * @param character 需要替换的目标字符串
     * @param left      左侧保留位数
     * @param right     右侧保留位数
     * @return
     */
    default String maskByPosition(
            String str,
            char character,
            int left,
            int right
    ) {
        int len = str == null ? 0 : str.length();

        if (!canMask(str, len, left, right)) {
            return str;
        }

        return str.substring(0, left)
                + String.valueOf(character).repeat(len - right - left)
                + str.substring(len - right);
    }


}

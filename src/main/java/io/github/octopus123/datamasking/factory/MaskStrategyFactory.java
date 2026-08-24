package io.github.octopus123.datamasking.factory;

import io.github.octopus123.datamasking.enums.MaskType;
import io.github.octopus123.datamasking.stragety.MaskStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据脱敏策略工厂
 * 根据对应的 策略类型 找到对应的策略类
 */
public class MaskStrategyFactory {

    private final Map<MaskType, MaskStrategy> strategies = new HashMap<>();


    /**
     *
     * @param strategies 每个实现了 MaskStrategy 接口的类集合
     */
    public MaskStrategyFactory(List<MaskStrategy> strategies) {
        strategies.forEach((i) -> {
            if (i == null) {
                return;
            }
            MaskType type = i.type();
            if (this.strategies.containsKey(type)) {
                throw new IllegalStateException("重复的脱敏策略类型：" + type);
            }
            this.strategies.put(type, i);
        });
    }

    /**
     *
     * @param maskType 策略类型
     * @return 返回对应的策略类
     */
    public MaskStrategy getStrategy(MaskType maskType) {
        return strategies.get(maskType);
    }
}

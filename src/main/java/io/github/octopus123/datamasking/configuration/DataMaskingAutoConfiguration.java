package io.github.octopus123.datamasking.configuration;

import io.github.octopus123.datamasking.aspect.MaskAspect;
import io.github.octopus123.datamasking.factory.MaskStrategyFactory;
import io.github.octopus123.datamasking.processor.MaskProcessor;
import io.github.octopus123.datamasking.stragety.MaskStrategy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * 数据脱敏自动配置
 */
@AutoConfiguration
@Import(MaskStrategyConfiguration.class)
public class DataMaskingAutoConfiguration {
    public DataMaskingAutoConfiguration() {
        System.out.println("====== MaskAutoConfiguration 加载了 ======");
    }

    @Bean
    public MaskStrategyFactory maskStrategyFactory(List<MaskStrategy> strategies) {
        return new MaskStrategyFactory(strategies);

    }

    @Bean
    public MaskProcessor maskProcessor(MaskStrategyFactory maskStrategyFactory) {
        return new MaskProcessor(maskStrategyFactory);
    }

    @Bean
    public MaskAspect maskAspect(MaskProcessor maskProcessor) {
        return new MaskAspect(maskProcessor);
    }
}

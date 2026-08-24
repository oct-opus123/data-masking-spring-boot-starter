package io.github.octopus123.datamasking.configuration;

import io.github.octopus123.datamasking.stragety.EmalMaskStrategy;
import io.github.octopus123.datamasking.stragety.IdCardMaskStrategy;
import io.github.octopus123.datamasking.stragety.PhoneMaskStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将策略类注册到容器中
 */
@Configuration
public class MaskStrategyConfiguration {

    @Bean
    @ConditionalOnMissingBean(PhoneMaskStrategy.class)
    public PhoneMaskStrategy phoneMaskStrategy() {
        return new PhoneMaskStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(PhoneMaskStrategy.class)
    public EmalMaskStrategy emalMaskStrategy() {
        return new EmalMaskStrategy();
    }

    @Bean
    @ConditionalOnMissingBean(PhoneMaskStrategy.class)
    public IdCardMaskStrategy idCardMaskStrategy() {
        return new IdCardMaskStrategy();
    }
}

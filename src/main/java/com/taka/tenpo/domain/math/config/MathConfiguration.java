package com.taka.tenpo.domain.math.config;

import com.taka.tenpo.domain.math.enums.OperationType;
import com.taka.tenpo.domain.math.strategy.MathStrategyManager;
import com.taka.tenpo.domain.math.strategy.OperationStrategy;
import com.taka.tenpo.domain.math.strategy.SumStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.taka.tenpo.domain.math.enums.OperationType.SUM;

@Configuration
public class MathConfiguration {

    @Bean
    public MathStrategyManager createMathStrategyManager() {
        Map<OperationType, OperationStrategy> strategies = new HashMap<>();
        strategies.put(SUM, this.createSumStrategy());
        var manager = new MathStrategyManager();
        manager.setMathStrategies(strategies);
        return manager;
    }

    @Bean
    public SumStrategy createSumStrategy() {
        return new SumStrategy();
    }
}

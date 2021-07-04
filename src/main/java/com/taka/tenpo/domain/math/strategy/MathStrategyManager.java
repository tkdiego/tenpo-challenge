package com.taka.tenpo.domain.math.strategy;

import com.taka.tenpo.domain.math.enums.OperationType;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class MathStrategyManager {

    @Setter
    private Map<OperationType, OperationStrategy> mathStrategies;

    public BigDecimal execOperation(OperationType operationType, BigDecimal firstValue, BigDecimal secondValue){
        return getStrategy(operationType).map(strategy -> strategy.execute(firstValue, secondValue)).orElseThrow(RuntimeException::new);
    }

    private Optional<OperationStrategy> getStrategy(OperationType operationType) {
        return ofNullable(mathStrategies.get(operationType));
    }
}

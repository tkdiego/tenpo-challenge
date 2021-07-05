package com.taka.tenpo.domain.math.strategy;

import com.taka.tenpo.domain.math.MathStrategySelectionErrorException;
import com.taka.tenpo.domain.math.enums.OperationType;
import lombok.Setter;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

public class MathStrategyManager {

    private static final Logger LOGGER = getLogger(MathStrategyManager.class);

    @Setter
    private Map<OperationType, OperationStrategy> mathStrategies;

    public BigDecimal execOperation(OperationType operationType, BigDecimal firstValue, BigDecimal secondValue) {
        return ofNullable(getStrategy(operationType)).map(strategy -> strategy.execute(firstValue, secondValue)).orElseThrow(() -> {
            String msg = format("Calculation strategy selection error. Strategy selected : %s", operationType);
            LOGGER.error(msg);
            throw new MathStrategySelectionErrorException(msg);
        });
    }

    private OperationStrategy getStrategy(OperationType operationType) {
        return mathStrategies.get(operationType);
    }
}

package com.taka.tenpo.domain.math.service;

import com.taka.tenpo.domain.math.enums.OperationType;
import com.taka.tenpo.domain.math.strategy.MathStrategyManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MathService {

    private final MathStrategyManager mathStrategyManager;

    public BigDecimal executeMathOperation(OperationType operationType, BigDecimal firstValue, BigDecimal secondValue) {
        return mathStrategyManager.execOperation(operationType, firstValue, secondValue);
    }

}

package com.taka.tenpo.domain.math.service;

import com.taka.tenpo.domain.math.enums.OperationType;
import com.taka.tenpo.domain.math.model.MathResponse;
import com.taka.tenpo.domain.math.strategy.MathStrategyManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class MathService implements IMathService {

    private final MathStrategyManager mathStrategyManager;

    @Override
    public MathResponse executeMathOperation(OperationType operationType, BigDecimal firstValue, BigDecimal secondValue) {
        BigDecimal result = mathStrategyManager.execOperation(operationType, firstValue, secondValue);
        return new MathResponse(result);
    }

}

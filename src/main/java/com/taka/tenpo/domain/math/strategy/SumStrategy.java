package com.taka.tenpo.domain.math.strategy;

import java.math.BigDecimal;

public class SumStrategy implements OperationStrategy {

    @Override
    public BigDecimal execute(BigDecimal firstValue, BigDecimal secondValue) {
        return firstValue.add(secondValue);
    }
}

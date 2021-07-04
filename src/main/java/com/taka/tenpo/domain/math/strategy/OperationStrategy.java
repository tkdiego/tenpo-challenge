package com.taka.tenpo.domain.math.strategy;

import java.math.BigDecimal;

public interface OperationStrategy {

    public BigDecimal execute(BigDecimal firstValue, BigDecimal secondValue);
}

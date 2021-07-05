package com.taka.tenpo.domain.math.service;

import com.taka.tenpo.domain.math.enums.OperationType;
import com.taka.tenpo.domain.math.model.MathResponse;

import java.math.BigDecimal;

public interface IMathService {

    MathResponse executeMathOperation(OperationType operationType, BigDecimal firstValue, BigDecimal secondValue);
}

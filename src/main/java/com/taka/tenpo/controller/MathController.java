package com.taka.tenpo.controller;

import com.taka.tenpo.domain.math.enums.OperationType;
import com.taka.tenpo.domain.math.model.MathResponse;
import com.taka.tenpo.domain.math.service.IMathService;
import com.taka.tenpo.domain.recorder.aspect.TrackingRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import static com.taka.tenpo.controller.URLConstants.FIRST_VALUE;
import static com.taka.tenpo.controller.URLConstants.MATH;
import static com.taka.tenpo.controller.URLConstants.SECOND_VALUE;
import static com.taka.tenpo.controller.URLConstants.SUM;
import static com.taka.tenpo.domain.recorder.enums.RequestType.MATH_SUM;


@RestController
@RequestMapping(value = MATH)
@AllArgsConstructor
public class MathController {

    private final IMathService mathService;

    @GetMapping(SUM)
    @TrackingRequest(requestType = MATH_SUM)
    public ResponseEntity<MathResponse> calculate(@RequestParam(FIRST_VALUE) BigDecimal firstValue,
                                                  @RequestParam(SECOND_VALUE) BigDecimal secondValue) {
        return ResponseEntity.ok(mathService.executeMathOperation(OperationType.SUM, firstValue, secondValue));
    }
}

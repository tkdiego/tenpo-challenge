package com.taka.tenpo.domain.math.strategy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;

@RunWith(MockitoJUnitRunner.class)
public class SumStrategyTest {

    @InjectMocks
    private SumStrategy sumStrategy;

    @Test
    public void sumTwoPositiveValueTest() {
        BigDecimal result = this.sumStrategy.execute(ONE, TEN);
        Assert.assertEquals(BigDecimal.valueOf(11), result);
    }

    @Test
    public void sumNegativeAndPositiveValueTest() {
        BigDecimal result = this.sumStrategy.execute(ONE.negate(), TEN);
        Assert.assertEquals(BigDecimal.valueOf(9), result);
    }

    @Test
    public void sumTwoNegativeValueTest() {
        BigDecimal result = this.sumStrategy.execute(ONE.negate(), TEN.negate());
        Assert.assertEquals(BigDecimal.valueOf(11).negate(), result);
    }


}

package com.taka.tenpo.domain;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.taka.tenpo.domain.security.model.TokenResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MathControllerTest extends AbstractControllerTest {


    private static final String RANDOM_USERNAME_1 = "MRandom1";
    private static final String RANDOM_USERNAME_2 = "MRandom2";
    private static final String RANDOM_USERNAME_3 = "MRandom3";

    private static final String RANDOM_PASS_1 = "Mpass1";
    private static final String RANDOM_PASS_2 = "Mpass2";
    private static final String RANDOM_PASS_3 = "Mpass3";

    @Test
    public void sumOperationSuccessfulTest() throws Exception {
        signInProcess(RANDOM_USERNAME_1, RANDOM_PASS_1, RANDOM_PASS_1);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_1, RANDOM_PASS_1));
        ResultActions mathActions = mathProcess(ONE, ONE, token.getKey(), token.getValue()).andExpect(status().isOk());
        assertEquals(BigDecimal.valueOf(2), getResult(mathActions));
    }

    @Test
    public void sumOperationSuccessfulWithNegativeValueTest() throws Exception {
        signInProcess(RANDOM_USERNAME_2, RANDOM_PASS_2, RANDOM_PASS_2);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2));
        ResultActions mathActions = mathProcess(ONE.negate(), ONE, token.getKey(), token.getValue()).andExpect(status().isOk());
        assertEquals(ZERO, getResult(mathActions));
    }

    @Test
    public void sumOperationUnsuccessfulWhenSomeValueIsNullTest() throws Exception {
        signInProcess(RANDOM_USERNAME_3, RANDOM_PASS_3, RANDOM_PASS_3);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_3, RANDOM_PASS_3));
        mathProcess(null, ONE, token.getKey(), token.getValue()).andExpect(status().isBadRequest());
    }

    private BigDecimal getResult(ResultActions mathActions) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.readValue(mathActions.andReturn()
                .getResponse().getContentAsString(), BigDecimal.class);
    }
}

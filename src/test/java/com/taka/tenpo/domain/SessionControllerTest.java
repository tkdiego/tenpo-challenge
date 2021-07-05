package com.taka.tenpo.domain;


import com.taka.tenpo.domain.security.model.TokenResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;

import static java.math.BigDecimal.ONE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionControllerTest extends AbstractControllerTest {

    private static final String RANDOM_USERNAME_1 = "SRandom1";
    private static final String RANDOM_USERNAME_2 = "SRandom2";
    private static final String RANDOM_USERNAME_3 = "SRandom3";
    private static final String RANDOM_USERNAME_4 = "SRandom4";
    private static final String RANDOM_USERNAME_5 = "SRandom5";
    private static final String RANDOM_USERNAME_6 = "SRandom6";

    private static final String RANDOM_PASS_1 = "Spass1";
    private static final String RANDOM_PASS_2 = "Spass2";
    private static final String RANDOM_PASS_3 = "Spass3";
    private static final String RANDOM_PASS_4 = "Spass4";
    private static final String RANDOM_PASS_5 = "Spass5";
    private static final String RANDOM_PASS_6 = "Spass6";

    @Test
    public void signInSuccessfulTest() throws Exception {
        signInProcess(RANDOM_USERNAME_1, RANDOM_PASS_1, RANDOM_PASS_1).andExpect(status().isOk());
    }

    @Test
    public void signInWithoutConfirmPassTest() throws Exception {
        ResultActions action = signInProcess(RANDOM_USERNAME_2, RANDOM_PASS_2, null);
        action.andExpect(status().isBadRequest());
        verifyExistsApiResponse(action);
    }

    @Test
    public void loginSuccessfulTest() throws Exception {
        signInProcess(RANDOM_USERNAME_2, RANDOM_PASS_2, RANDOM_PASS_2).andExpect(status().isOk());
        ResultActions action = loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2);
        action.andExpect(status().isOk());
        verifyExistsTokenResponse(action);
    }

    @Test
    public void logoutSuccessfulTest() throws Exception {
        signInProcess(RANDOM_USERNAME_3, RANDOM_PASS_3, RANDOM_PASS_3).andExpect(status().isOk());
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_3, RANDOM_PASS_3));
        ResultActions logoutAction = logoutProcess(token.getKey(), token.getValue());
        logoutAction.andExpect(status().isOk());
        verifyExistsApiResponse(logoutAction);
    }

    @Test
    public void mathOperationWithLoginTest() throws Exception {
        signInProcess(RANDOM_USERNAME_4, RANDOM_PASS_4, RANDOM_PASS_4);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_4, RANDOM_PASS_4));
        mathProcess(ONE, ONE, token.getKey(), token.getValue()).andExpect(status().isOk());
    }

    @Test
    public void mathOperationWithoutSessionTest() throws Exception {
        signInProcess(RANDOM_USERNAME_4, RANDOM_PASS_4, RANDOM_PASS_4);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_4, RANDOM_PASS_4));
        logoutProcess(token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue()).andExpect(status().isUnauthorized());
    }

    @Test
    public void signInUnsuccessfulWithSameUsernameTest() throws Exception {
        signInProcess(RANDOM_USERNAME_5, RANDOM_PASS_5, RANDOM_PASS_5).andExpect(status().isOk());
        ResultActions signInAction = signInProcess(RANDOM_USERNAME_5, RANDOM_PASS_1, RANDOM_PASS_1).andExpect(status().isBadRequest());
        verifyExistsApiResponse(signInAction);
    }

    @Test
    public void deprecatedTokenAndNewestTokenTest() throws Exception {
        signInProcess(RANDOM_USERNAME_6, RANDOM_PASS_6, RANDOM_PASS_6).andExpect(status().isOk());
        TokenResponse deprecatedToken = getTokenResponse(loginProcess(RANDOM_USERNAME_6, RANDOM_PASS_6));
        Thread.sleep(1000);
        TokenResponse newestToken = getTokenResponse(loginProcess(RANDOM_USERNAME_6, RANDOM_PASS_6));
        mathProcess(ONE, ONE, deprecatedToken.getKey(), deprecatedToken.getValue()).andExpect(status().isUnauthorized());
        mathProcess(ONE, ONE, newestToken.getKey(), newestToken.getValue()).andExpect(status().isOk());
    }

}



package com.taka.tenpo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taka.tenpo.domain.security.model.LoginRequest;
import com.taka.tenpo.domain.security.model.SignInRequest;
import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.model.ApiResponse;
import lombok.Getter;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static com.taka.tenpo.controller.URLConstants.API;
import static com.taka.tenpo.controller.URLConstants.AUTH;
import static com.taka.tenpo.controller.URLConstants.FIRST_VALUE;
import static com.taka.tenpo.controller.URLConstants.LOGIN;
import static com.taka.tenpo.controller.URLConstants.LOGOUT;
import static com.taka.tenpo.controller.URLConstants.MATH;
import static com.taka.tenpo.controller.URLConstants.SECOND_VALUE;
import static com.taka.tenpo.controller.URLConstants.SIGN_IN;
import static com.taka.tenpo.controller.URLConstants.SUM;
import static com.taka.tenpo.controller.URLConstants.TRACKING_HISTORY;
import static com.taka.tenpo.domain.security.util.ParameterConstant.TOKEN_BEARER;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public abstract class AbstractControllerTest {

    protected static final String AUTH_SIGN_IN_URI = AUTH + SIGN_IN;
    protected static final String AUTH_LOGIN_URI = AUTH + LOGIN;
    protected static final String AUTH_LOGOUT_URI = AUTH + LOGOUT;
    protected static final String MATH_URI = MATH + SUM;
    protected static final String HISTORY_URI = API + TRACKING_HISTORY;
    protected static final String AUTHORIZATION_HEADER = "Authorization";
    protected static final String HEADER_VALUE_FORMAT = "%s %s";
    protected final ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext context;
    @Getter
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    protected ResultActions signInProcess(String username, String password, String confirmPassword) throws Exception {
        SignInRequest request = new SignInRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);

        return mockMvc.perform(MockMvcRequestBuilders.post(AUTH_SIGN_IN_URI)
                .content(mapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON));
    }

    protected ResultActions loginProcess(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        return mockMvc.perform(MockMvcRequestBuilders.post(AUTH_LOGIN_URI)
                .content(mapper.writeValueAsString(loginRequest))
                .contentType(APPLICATION_JSON));
    }

    protected ResultActions logoutProcess(String tokenKey, String tokenValue) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(AUTH_LOGOUT_URI)
                .header(AUTHORIZATION_HEADER, format(HEADER_VALUE_FORMAT, tokenKey, tokenValue)));
    }

    protected ResultActions mathProcess(BigDecimal firstValue, BigDecimal secondValue, String tokenKey, String tokenValue) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(MATH_URI).param(FIRST_VALUE, String.valueOf(firstValue))
                .param(SECOND_VALUE, String.valueOf(secondValue))
                .header(AUTHORIZATION_HEADER, format(HEADER_VALUE_FORMAT, tokenKey, tokenValue)));
    }

    protected TokenResponse getTokenResponse(ResultActions actions) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.readValue(actions.andReturn()
                .getResponse().getContentAsString(), TokenResponse.class);
    }

    protected void verifyExistsApiResponse(ResultActions action) throws UnsupportedEncodingException, JsonProcessingException {
        ApiResponse apiResponse = mapper.readValue(action.andReturn().getResponse().getContentAsString(), ApiResponse.class);
        assertNotNull(apiResponse);
        assertNotNull(apiResponse.getStatus());
        assertNotNull(apiResponse.getMessage());
    }

    protected void verifyExistsTokenResponse(ResultActions action) throws UnsupportedEncodingException, JsonProcessingException {
        TokenResponse token = mapper.readValue(action.andReturn().getResponse().getContentAsString(), TokenResponse.class);
        assertNotNull(token.getKey());
        assertNotNull(token.getValue());
        assertEquals(TOKEN_BEARER, token.getKey());
    }
}

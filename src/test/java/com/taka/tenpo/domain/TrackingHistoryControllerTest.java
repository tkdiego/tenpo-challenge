package com.taka.tenpo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.taka.tenpo.domain.recorder.model.PaginationResponse;
import com.taka.tenpo.domain.recorder.model.RequestType;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;
import com.taka.tenpo.domain.recorder.repository.TrackingRecorderRepository;
import com.taka.tenpo.domain.security.model.TokenResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static com.taka.tenpo.controller.URLConstants.REQUEST_TYPE;
import static com.taka.tenpo.domain.recorder.model.RequestType.LOGOUT;
import static java.lang.String.format;
import static java.math.BigDecimal.ONE;
import static java.math.RoundingMode.UP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrackingHistoryControllerTest extends AbstractControllerTest {

    private static final String RANDOM_USERNAME_1 = "MRandom1";
    private static final String RANDOM_USERNAME_2 = "MRandom2";

    private static final String RANDOM_PASS_1 = "Mpass1";
    private static final String RANDOM_PASS_2 = "Mpass2";
    @Autowired
    TrackingRecorderRepository trackingRecorderRepository;

    @Test
    public void getTotalElementsTest() throws Exception {
        signInProcess(RANDOM_USERNAME_1, RANDOM_PASS_1, RANDOM_PASS_1);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_1, RANDOM_PASS_1));
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        logoutProcess(token.getKey(), token.getValue());

        token = getTokenResponse(loginProcess(RANDOM_USERNAME_1, RANDOM_PASS_1));
        ResultActions historyActions = historyProcess(token.getKey(), token.getValue()).andExpect(status().isOk());
        PaginationResponse<TrackingRecorder> result = getResult(historyActions);
        verifyNonNullResponse(result);
        assertEquals(8, result.getTotalItems());
    }

    @Test
    public void getLimitAndPagesTest() throws Exception {
        signInProcess(RANDOM_USERNAME_2, RANDOM_PASS_2, RANDOM_PASS_2);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2));
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        logoutProcess(token.getKey(), token.getValue());

        token = getTokenResponse(loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2));
        ResultActions historyActions = historyProcess(1, 3, token.getKey(), token.getValue()).andExpect(status().isOk());
        PaginationResponse<TrackingRecorder> result = getResult(historyActions);
        verifyNonNullResponse(result);
        assertEquals(3, result.getItems().size());
        BigDecimal expectedPages = BigDecimal.valueOf(result.getTotalItems()).divide(BigDecimal.valueOf(3), UP);
        assertEquals(expectedPages, BigDecimal.valueOf(result.getPages()));
        assertEquals(1, result.getCurrentPage());
    }

    @Test
    public void getFilterTest() throws Exception {
        trackingRecorderRepository.deleteAll();
        signInProcess(RANDOM_USERNAME_2, RANDOM_PASS_2, RANDOM_PASS_2);
        TokenResponse token = getTokenResponse(loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2));
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        mathProcess(ONE, ONE, token.getKey(), token.getValue());
        logoutProcess(token.getKey(), token.getValue());

        token = getTokenResponse(loginProcess(RANDOM_USERNAME_2, RANDOM_PASS_2));
        ResultActions historyActions = historyProcess(LOGOUT, 0, 3, token.getKey(), token.getValue()).andExpect(status().isOk());
        PaginationResponse<TrackingRecorder> result = getResult(historyActions);
        verifyNonNullResponse(result);
        assertEquals(1, result.getItems().size());
        BigDecimal expectedPages = BigDecimal.valueOf(result.getTotalItems()).divide(BigDecimal.valueOf(3), UP);
        assertEquals(expectedPages, BigDecimal.valueOf(result.getPages()));
        assertEquals(0, result.getCurrentPage());
    }

    private ResultActions historyProcess(RequestType requestType, Integer page, Integer limit, String tokenKey, String tokenValue) throws Exception {
        return getMockMvc().perform(MockMvcRequestBuilders.get(HISTORY_URI)
                .param(REQUEST_TYPE, requestType != null ? requestType.name() : null)
                .param("page", page != null ? page.toString() : null)
                .param("limit", limit != null ? limit.toString() : null)
                .header(AUTHORIZATION_HEADER, format(HEADER_VALUE_FORMAT, tokenKey, tokenValue)));
    }

    private ResultActions historyProcess(Integer page, Integer limit, String tokenKey, String tokenValue) throws Exception {
        return getMockMvc().perform(MockMvcRequestBuilders.get(HISTORY_URI)
                .param("page", page != null ? page.toString() : null)
                .param("limit", limit != null ? limit.toString() : null)
                .header(AUTHORIZATION_HEADER, format(HEADER_VALUE_FORMAT, tokenKey, tokenValue)));
    }

    private ResultActions historyProcess(String tokenKey, String tokenValue) throws Exception {
        return getMockMvc().perform(MockMvcRequestBuilders.get(HISTORY_URI)
                .header(AUTHORIZATION_HEADER, format(HEADER_VALUE_FORMAT, tokenKey, tokenValue)));
    }

    private PaginationResponse<TrackingRecorder> getResult(ResultActions mathActions) throws UnsupportedEncodingException, JsonProcessingException {
        return mapper.readValue(mathActions.andReturn()
                .getResponse().getContentAsString(), PaginationResponse.class);
    }

    private void verifyNonNullResponse(PaginationResponse<TrackingRecorder> result) {
        assertNotNull(result);
        assertNotNull(result.getItems());
        assertNotNull(result.getCurrentPage());
        assertNotNull(result.getPages());
        assertNotNull(result.getTotalItems());
    }
}

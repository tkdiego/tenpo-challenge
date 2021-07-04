package com.taka.tenpo.domain.recorder.aspect;


import com.taka.tenpo.domain.recorder.service.RecorderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TrackingRecorderProcessTest {

    private final String MATH_URI = "/api/math/sum?first_value=1&second_value=2";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Mock
    private RecorderService recorderService;

    @Before
    public void setup() {

        doNothing().when(recorderService).recordTracking(any());
        doNothing().when(recorderService).recordTracking(any());

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }


    @Test
    public void trackingRecorderAppliedTest() throws Exception {
        mockMvc.perform(get(MATH_URI))
                .andExpect(status().isOk());
        verify(recorderService, times(1)).recordTracking(any());
    }

}


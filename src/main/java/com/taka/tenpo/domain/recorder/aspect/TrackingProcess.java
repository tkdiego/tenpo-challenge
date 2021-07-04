package com.taka.tenpo.domain.recorder.aspect;


import com.taka.tenpo.domain.recorder.model.RequestStatus;
import com.taka.tenpo.domain.recorder.model.TrackingRecorder;
import com.taka.tenpo.domain.recorder.service.RecorderService;
import com.taka.tenpo.domain.security.service.JwtService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.taka.tenpo.domain.recorder.model.RequestStatus.FAILURE;
import static com.taka.tenpo.domain.recorder.model.RequestStatus.OK;
import static com.taka.tenpo.domain.security.util.AuthenticationResolver.getUsername;

@Aspect
@Component
@AllArgsConstructor
public class TrackingProcess {

    private RecorderService recorderService;

    private JwtService jwtService;

    @AfterReturning(value = "@annotation(trackingRequest)")
    public void recordRequest(JoinPoint joinPoint, TrackingRequest trackingRequest) {
        recordRequest(trackingRequest, OK);
    }

    @AfterThrowing(value = "@annotation(trackingRequest)")
    public void recordFailedRequest(JoinPoint joinPoint, TrackingRequest trackingRequest) {
        recordRequest(trackingRequest, FAILURE);
    }

    private void recordRequest(TrackingRequest trackingRequest, RequestStatus requestStatus) {
        TrackingRecorder trackingRecorder = new TrackingRecorder();
        trackingRecorder.setRequestType(trackingRequest.requestType());
        trackingRecorder.setUsername(getUsername());
        trackingRecorder.setRequestStatus(requestStatus);
        recorderService.recordTracking(trackingRecorder);
    }

}


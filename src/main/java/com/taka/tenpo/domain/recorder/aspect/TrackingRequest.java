package com.taka.tenpo.domain.recorder.aspect;

import com.taka.tenpo.domain.recorder.model.RequestType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(METHOD)
@Retention(RUNTIME)
public @interface TrackingRequest {

    RequestType requestType();
}

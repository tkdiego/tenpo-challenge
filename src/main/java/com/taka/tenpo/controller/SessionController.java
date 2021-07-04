package com.taka.tenpo.controller;

import com.taka.tenpo.domain.recorder.aspect.TrackingRequest;
import com.taka.tenpo.domain.recorder.model.RequestType;
import com.taka.tenpo.domain.security.model.LoginRequest;
import com.taka.tenpo.domain.security.model.LogoutRequest;
import com.taka.tenpo.domain.security.model.SignInRequest;
import com.taka.tenpo.domain.security.model.TokenResponse;
import com.taka.tenpo.domain.security.service.SessionService;
import com.taka.tenpo.model.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.taka.tenpo.controller.URLConstants.AUTH;
import static com.taka.tenpo.controller.URLConstants.LOGIN;
import static com.taka.tenpo.controller.URLConstants.LOGOUT;
import static com.taka.tenpo.controller.URLConstants.LOGOUT_PARAMETER;
import static com.taka.tenpo.controller.URLConstants.SIGN_IN;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = AUTH)
@AllArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping(SIGN_IN)
    @TrackingRequest(requestType = RequestType.SIGN_IN)
    public ResponseEntity<ApiResponse> singIn(@RequestBody @Valid SignInRequest signInRequest) {
        sessionService.signIn(signInRequest);
        return ResponseEntity.ok(new ApiResponse(OK.value(), "The user was created."));
    }

    @PostMapping(LOGIN)
    @TrackingRequest(requestType = RequestType.LOGIN)
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse token = sessionService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @GetMapping(LOGOUT)
    @TrackingRequest(requestType = RequestType.LOGOUT)
    public ResponseEntity<ApiResponse> login(@RequestHeader(LOGOUT_PARAMETER) String token) {
        sessionService.logout(new LogoutRequest(token));
        return ResponseEntity.ok(new ApiResponse(OK.value(), "Logout success."));
    }

}

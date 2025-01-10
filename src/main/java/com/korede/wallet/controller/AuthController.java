package com.korede.wallet.controller;

import com.korede.wallet.exception.HttpBaseResponse;
import com.korede.wallet.model.request.LoginRequestDto;
import com.korede.wallet.model.request.UserRegistrationRequestDto;
import com.korede.wallet.model.response.LoginResponse;
import com.korede.wallet.model.response.RegisterResponse;
import com.korede.wallet.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<HttpBaseResponse<RegisterResponse>> register(@Valid @RequestBody UserRegistrationRequestDto userDto) {
        RegisterResponse registrationResponse = authService.register(userDto);

        HttpBaseResponse<RegisterResponse> response = HttpBaseResponse.<RegisterResponse>builder()
                .statusCode(registrationResponse.getMessage().equals("error") ? HttpStatus.BAD_REQUEST : HttpStatus.CREATED)
                .message(registrationResponse.getMessage())
                .data(registrationResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<HttpBaseResponse<LoginResponse>> login(@Valid @RequestBody LoginRequestDto loginDto) {

        LoginResponse loginResponse = authService.login(loginDto);
        HttpBaseResponse<LoginResponse> response = HttpBaseResponse.<LoginResponse>builder()
                .statusCode(HttpStatus.OK)
                .message("Login successful")
                .data(loginResponse)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

}

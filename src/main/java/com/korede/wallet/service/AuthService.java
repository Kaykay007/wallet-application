package com.korede.wallet.service;

import com.korede.wallet.model.request.LoginRequestDto;
import com.korede.wallet.model.request.UserRegistrationRequestDto;
import com.korede.wallet.model.response.LoginResponse;
import com.korede.wallet.model.response.RegisterResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface AuthService {
    RegisterResponse register(UserRegistrationRequestDto userDto);
    LoginResponse login(LoginRequestDto loginDto);

}
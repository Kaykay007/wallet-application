package com.korede.wallet.service.impl;

import com.korede.wallet.entity.Account;
import com.korede.wallet.entity.User;
import com.korede.wallet.exception.ResourceNotFoundException;
import com.korede.wallet.exception.WalletException;
import com.korede.wallet.model.enums.CurrencyType;
import com.korede.wallet.model.request.LoginRequestDto;
import com.korede.wallet.model.request.UserRegistrationRequestDto;
import com.korede.wallet.model.response.LoginResponse;
import com.korede.wallet.model.response.RegisterResponse;
import com.korede.wallet.repository.AccountRepository;
import com.korede.wallet.repository.UserRepository;
import com.korede.wallet.service.AuthService;
import com.korede.wallet.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AuthServiceImpl(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository, AccountRepository accountRepository) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }



    @Override
    public RegisterResponse register(UserRegistrationRequestDto userDto) {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new WalletException("Username already exists");
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setHashedPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());

        User savedUser = userRepository.save(user);

        // Create an associated account for the new user and link the account to the user
        Account account = Account.builder()
                .user(savedUser)
                .balance(BigDecimal.ZERO)
                .currencyType(CurrencyType.USD)
                .build();

        Account savedAccount = accountRepository.save(account);

        return new RegisterResponse("User registered successfully", savedUser.getUsername(), savedAccount.getAccountNumber());
    }


    @Override
    public LoginResponse login(LoginRequestDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getHashedPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return new LoginResponse(token);
    }



//    @Override
//public LoginResponse login(LoginRequestDto loginDto) {
//    // Use UserDetailsService to load user details
//    UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
//
//    // Check if the password matches the stored hashed password
//    if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
//        throw new BadCredentialsException("Invalid credentials");
//    }
//
//    // Generate JWT token for the authenticated user
//    String token = jwtUtil.generateToken(userDetails.getUsername());
//
//        log.info("Generated token: {}. Expires at: {}", token, jwtUtil.extractAllClaims(token).getExpiration());
//    return new LoginResponse(token);
//}
}
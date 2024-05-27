package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.ConfirmationTokenEntity;
import com.SWD.Order_Dish.entity.JwtTokenEntity;
import com.SWD.Order_Dish.entity.AccountEntity;
import com.SWD.Order_Dish.enums.EmailTemplateName;
import com.SWD.Order_Dish.enums.ResponseMessageEnum;
import com.SWD.Order_Dish.enums.Role;
import com.SWD.Order_Dish.enums.TokenType;
import com.SWD.Order_Dish.model.authentication.AuthenticationRequest;
import com.SWD.Order_Dish.model.authentication.AuthenticationResponse;
import com.SWD.Order_Dish.model.authentication.RegisterRequest;
import com.SWD.Order_Dish.model.authentication.RegisterResponse;
import com.SWD.Order_Dish.repository.ConfirmationTokenRepository;
import com.SWD.Order_Dish.repository.JwtTokenRepository;
import com.SWD.Order_Dish.repository.AccountRepository;
import com.SWD.Order_Dish.util.DateProcess;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements LogoutHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final AccountRepository accountRepository;
    private final JwtTokenRepository jwtTokenRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        LOGGER.info("Authenticate user");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        AccountEntity account = accountRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        invalidateAllAccountTokens(account);
        saveAccountToken(account, jwtToken, refreshToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public RegisterResponse register(RegisterRequest request) throws MessagingException, ParseException {
        if((accountRepository.findAccountEntityByEmail(request.getEmail())) != null) {
            LOGGER.warn("Email has been used: {}", request.getEmail());
            return RegisterResponse.builder()
                    .responseMessage(ResponseMessageEnum.DUPLICATED_EMAIL.getDetail())
                    .build();
        }
        if((accountRepository.findAccountEntityByPhoneNumber(request.getPhoneNumber())) != null){
            LOGGER.warn("Phone number has been used: {}", request.getPhoneNumber());
            return RegisterResponse.builder()
                    .responseMessage(ResponseMessageEnum.DUPLICATED_PHONE_NUMBER.getDetail())
                    .build();
        }
        AccountEntity account = AccountEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getDisplayName())
                .role(request.getRole() == Role.MANAGER ? Role.MANAGER : Role.STAFF)
                .isEnable(request.getRole() == Role.MANAGER)
                .isUnlocked(true)
                .isAvailable(true)
                .birthday(DateProcess.convertToSimpleDate(request.getBirthday()))
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();
        AccountEntity accountBeCreated = accountRepository.save(account);
        String jwtToken = jwtService.generateToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);
        saveAccountToken(accountBeCreated, jwtToken, refreshToken);
        sendValidationEmail(accountBeCreated);
        return RegisterResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .responseMessage(ResponseMessageEnum.ACCOUNT_CREATED_SUCCESS.getDetail())
                .build();
    }

    private void sendValidationEmail(AccountEntity accountBeCreated) throws MessagingException {
        String generatedToken = saveConfirmationToken(accountBeCreated, 6, 86400L, TokenType.CONFIRMATION_TOKEN);
        emailService.sendEmail(
                accountBeCreated.getEmail(),
                accountBeCreated.getFullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                generatedToken,
                "Account Activation");
    }

    public void reSendValidationEmail(String email) throws MessagingException {
        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not exists"));
        sendValidationEmail(account);
    }

    private String saveConfirmationToken(AccountEntity account, Integer length, Long lifetime, TokenType type) {
        String generatedToken = generateCode(length);
        ConfirmationTokenEntity tokenEntity = ConfirmationTokenEntity.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusSeconds(lifetime))
                .accountEntity(account)
                .tokenType(type)
                .build();
        confirmationTokenRepository.save(tokenEntity);
        return generatedToken;
    }

    private String generateCode(Integer length) {
        String characters = "0123456789";
        StringBuilder code = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }
        return code.toString();
    }

    private void saveAccountToken(AccountEntity account, String jwtToken) {
        JwtTokenEntity accessToken = JwtTokenEntity.builder()
                .accountEntity(account)
                .token(jwtToken)
                .tokenType(TokenType.ACCESS_TOKEN)
                .isRevoked(false)
                .isExpired(false)
                .build();
        jwtTokenRepository.save(accessToken);
    }

    private void saveAccountToken(AccountEntity account, String jwtToken, String refreshToken) {
        saveAccountToken(account, jwtToken);
        JwtTokenEntity refreshTokenEntity = JwtTokenEntity.builder()
                .accountEntity(account)
                .token(refreshToken)
                .tokenType(TokenType.REFRESH_TOKEN)
                .isRevoked(false)
                .isExpired(false)
                .build();
        jwtTokenRepository.save(refreshTokenEntity);
    }

    private void invalidateAllAccountTokens(AccountEntity account) {
        List<JwtTokenEntity> currentValidTokens = jwtTokenRepository
                .findAllByAccountEntity_UserIdAndIsExpiredFalseAndIsRevokedFalse(account.getUserId());
        if (currentValidTokens.isEmpty()) {
            return;
        }
        // TODO: set all current tokens of input Account to be invalid
        currentValidTokens.forEach(token -> {
            token.setIsExpired(true);
            token.setIsRevoked(true);
        });
        jwtTokenRepository.saveAll(currentValidTokens);
    }

    private void invalidateAllAccessTokens(AccountEntity account) {
        List<JwtTokenEntity> currentAccessTokens = jwtTokenRepository
                .findAllByAccountEntity_UserIdAndTokenTypeAndIsExpiredFalseAndIsRevokedFalse(
                        account.getUserId(), TokenType.ACCESS_TOKEN);
        if (currentAccessTokens.isEmpty()) {
            return;
        }
        currentAccessTokens.forEach(token -> {
            token.setIsExpired(true);
            token.setIsRevoked(true);
        });
        jwtTokenRepository.saveAll(currentAccessTokens);
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authenticationHeader = request.getHeader("Authorization");
        final String jwt;
        // ? if authenticationHeader is null or does not start with "Bearer ", jump to the next filter
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authenticationHeader.replace("Bearer ", "");
        JwtTokenEntity currentStoredJwtToken = jwtTokenRepository.findByToken(jwt).orElse(null);
        if (currentStoredJwtToken != null) {
            currentStoredJwtToken.setIsRevoked(true);
            currentStoredJwtToken.setIsExpired(true);
            jwtTokenRepository.save(currentStoredJwtToken);

            // Revoke refresh token
            List<JwtTokenEntity> refreshTokens = jwtTokenRepository.findAllByAccountEntity_UserIdAndTokenType(
                    currentStoredJwtToken.getAccountEntity().getUserId(), TokenType.REFRESH_TOKEN);
            refreshTokens.forEach(refreshToken -> {
                refreshToken.setIsRevoked(true);
                refreshToken.setIsExpired(true);
            });
            jwtTokenRepository.saveAll(refreshTokens);
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authenticationHeader = request.getHeader("Authorization");
        final String refreshToken;
        // ? if authenticationHeader is null or does not start with "Bearer ", jump to
        // the next filter
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authenticationHeader.replace("Bearer ", "");
        // todo EXTRACT EMAIL FROM JWT TOKEN
        final String userEmail = jwtService.extractUserEmail(refreshToken);
        // ? if userEmail is not null or user is not yet authenticated (not log in yet),
        // get user from database
        if (userEmail != null) {
            AccountEntity account = accountRepository.findByEmail(userEmail).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, account)) {
                String newJwtToken = jwtService.generateToken(account);
                invalidateAllAccessTokens(account);
                saveAccountToken(account, newJwtToken);
                AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                        .accessToken(newJwtToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authenticationResponse);
            }
        }
    }

    public void activateAccount(String confirmToken) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenRepository.findByTokenAndTokenType(confirmToken, TokenType.CONFIRMATION_TOKEN)
                .orElseThrow(() -> new RuntimeException("Invalid validation code"))
                ;
        if (LocalDateTime.now().isAfter(confirmationTokenEntity.getExpiredAt())) {
            throw new RuntimeException("Validation code expired");
        }
        AccountEntity account = confirmationTokenEntity.getAccountEntity();
        account.setIsEnable(true);
        accountRepository.save(account);
        confirmationTokenEntity.setValidatedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationTokenEntity);
    }

    public void sendResetPasswordEmail(String email) throws MessagingException {
        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not exists"));
        String generatedToken = saveConfirmationToken(account, 8, 600L, TokenType.PASSWORD_RESET_TOKEN);
        emailService.sendEmail(
                account.getEmail(),
                account.getFullName(),
                EmailTemplateName.RESET_PASSWORD,
                activationUrl,
                generatedToken,
                "Password Reset");
    }

    public void resetPassword(String resetToken, String email, String newPassword) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenRepository.findByTokenAndTokenType(resetToken, TokenType.PASSWORD_RESET_TOKEN)
                .orElseThrow(() -> new RuntimeException("Invalid reset code"));
        if (LocalDateTime.now().isAfter(confirmationTokenEntity.getExpiredAt())) {
            throw new RuntimeException("Reset code expired");
        }
        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not exists"));
        account.setPassword(passwordEncoder.encode(newPassword));
        confirmationTokenEntity.setValidatedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationTokenEntity);
        accountRepository.save(account);
    }
}

package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.authentication.AuthenticationRequest;
import com.SWD.Order_Dish.model.authentication.RegisterRequest;
import com.SWD.Order_Dish.model.authentication.RegisterResponse;
import com.SWD.Order_Dish.service.AuthenticationService;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterRequest request) throws MessagingException, ParseException {
        RegisterResponse response = authenticationService.register(request);
        return ResponseUtil.getObject(response,
                HttpStatus.CREATED,
                response.getResponseMessage()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseUtil.getObject(authenticationService.authenticate(request),
                HttpStatus.OK,
                "Login successful"
        );
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/activate-account")
    public void activate(@RequestParam String confirmToken) {
        authenticationService.activateAccount(confirmToken);
    }

    @PostMapping("/resend-validation-email")
    public ResponseEntity<ResponseDTO> sendValidationEmail(@RequestParam String email) throws MessagingException {
        authenticationService.reSendValidationEmail(email);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Validation email re-sent successfully"
        );
    }

    @GetMapping("/send-reset-password-email")
    public ResponseEntity<ResponseDTO> forgotPassword(@RequestParam String email) throws MessagingException {
        authenticationService.sendResetPasswordEmail(email);
        return ResponseUtil.getObject(null, HttpStatus.OK, "Reset password email sent successfully");
    }

    @PatchMapping("/reset-password")
    public ResponseEntity<ResponseDTO> resetPassword(@RequestParam String resetCode, @RequestParam String email, @RequestParam String newPassword) {
        authenticationService.resetPassword(resetCode, email, newPassword);
        return ResponseUtil.getObject(null, HttpStatus.OK, "Password reset successfully");
    }

}

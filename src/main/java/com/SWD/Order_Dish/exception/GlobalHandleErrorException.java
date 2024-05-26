package com.SWD.Order_Dish.exception;

import com.SWD.Order_Dish.enums.ResponseMessageEnum;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.messaging.MessagingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandleErrorException {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalHandleErrorException.class);

    public ResponseEntity<Object> handleValidationErrors(BindingResult result) {
        if (!result.hasErrors()) {
            return ResponseEntity.ok("Validation passed");
        }
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach((error) -> errors.put(error.getField(), error.getDefaultMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put("message", ResponseMessageEnum.INVALID_REQUEST.getDetail());
        response.put("fieldErrors", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put("message", ResponseMessageEnum.INVALID_REQUEST.getDetail());
        response.put("fieldErrors", errors);
        LOGGER.error("Constraint violation at : {} ",errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .toList();
        return ResponseUtil.error(String.valueOf(errorMessages), "Bad request", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Bad request", HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ParseException.class)
    public ResponseEntity<?> handleParseException(ParseException ex) {
        return ResponseUtil.error(ex.getMessage(),  ResponseMessageEnum.INVALID_REQUEST.getDetail(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMailException(MailException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMailException(MessagingException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Messaging service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(LockedException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Locked", HttpStatus.LOCKED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Disabled", HttpStatus.PRECONDITION_REQUIRED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        String errorMessage = ex.getMessage();
        return ResponseUtil.error(errorMessage, "Bad credentials", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = ex.getMostSpecificCause().getMessage();
        if (errorMessage.contains("duplicate key value violates unique constraint")) {
            String duplicateKey = extractDuplicateKey(errorMessage);
            String message = duplicateKey + " already exists";
            return ResponseUtil.error(message, "Data Integrity Violation", HttpStatus.CONFLICT);
        } else {
            return ResponseUtil.error(errorMessage, "Data Integrity Violation", HttpStatus.CONFLICT);
        }
    }

    private String extractDuplicateKey(String errorMessage) {
        int startIndex = errorMessage.indexOf("(");
        int endIndex = errorMessage.indexOf(")");
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return errorMessage.substring(startIndex + 1, endIndex);
        }
        return "";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }




}

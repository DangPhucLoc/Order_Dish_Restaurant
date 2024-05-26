package com.SWD.Order_Dish.model.authentication;

import com.SWD.Order_Dish.enums.Role;
import com.SWD.Order_Dish.validator.ValidPhoneNumber;
import com.SWD.Order_Dish.validator.ValidSimpleDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Size(max = 100, message = "can not over than {max} characters")
    private String displayName;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @Email(message = "has wrong format")
    private String email;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String password;


    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @ValidSimpleDate(message = "has wrong format")
    private String birthday;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    @ValidPhoneNumber(message = "has wrong format")
    private String phoneNumber;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String address;

    private Role role;
}
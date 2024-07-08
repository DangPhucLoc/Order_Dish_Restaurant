package com.SWD.Order_Dish.model.account;

import com.SWD.Order_Dish.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    private String userId;

    private String imageURL;

    @NotNull(message = "Full name cannot be null")
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @NotNull(message = "Birthday cannot be null")
    private String birthday;

    @NotNull(message = "Phone number cannot be null")
    @NotBlank(message = "Phone number cannot be blank")
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotNull(message = "Role cannot be null")
    private Role role;

    @NotNull(message = "Availability status cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Enable status cannot be null")
    private Boolean isEnable;

    @NotNull(message = "Unlock status cannot be null")
    private Boolean isUnlocked;
}

package com.SWD.Order_Dish.model.account;

import com.SWD.Order_Dish.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private String userId;

    private String email;

    private String imageURL;

    private String fullName;

    private LocalDateTime birthday;

    private String phoneNumber;

    private String address;

    private Role role;

    private Boolean isAvailable;

    private Boolean isEnable;

    private Boolean isUnlocked;

    private Date createdDate;

    private Date updatedDate;

    private String modifiedBy;

    private String createdBy;
}

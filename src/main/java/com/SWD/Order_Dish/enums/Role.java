package com.SWD.Order_Dish.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum Role {
    STAFF(
            Set.of(
                    Permission.STAFF_READ,
                    Permission.STAFF_CREATE,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_DELETE
            )
    ),
    MANAGER(
            Set.of(
                    Permission.MANAGER_READ,
                    Permission.MANAGER_CREATE,
                    Permission.MANAGER_UPDATE,
                    Permission.MANAGER_DELETE,
                    Permission.STAFF_READ,
                    Permission.STAFF_CREATE,
                    Permission.STAFF_UPDATE,
                    Permission.STAFF_DELETE
            )
    ),
    CASHIER(
            Set.of(
                    Permission.MANAGER_READ,
                    Permission.STAFF_UPDATE
            )
    ),
    CHEF(
            Set.of(
                    Permission.MANAGER_READ
            )
    );
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}

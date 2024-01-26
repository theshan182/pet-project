package com.theshan.product_management.utils;

import com.theshan.product_management.dto.user.UserRegistrationRequest;
import com.theshan.product_management.entity.Role;
import com.theshan.product_management.service.RoleService;
import com.theshan.product_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        this.createRole(roleService);
        this.createUser(userService);
    }

    private void createUser(UserService userService) {
        UserRegistrationRequest userOne = UserRegistrationRequest.userRegistrationRequestBuilder()
                .firstName("John")
                .lastName("Doe")
                .email("john@gmaill.com")
                .password("123")
                .build();

        UserRegistrationRequest userTwo = UserRegistrationRequest.userRegistrationRequestBuilder()
                .firstName("Bill")
                .lastName("Gates")
                .email("bill@gmaill.com")
                .password("123")
                .build();

        UserRegistrationRequest userThree = UserRegistrationRequest.userRegistrationRequestBuilder()
                .firstName("Kushan")
                .lastName("Weerakokdy")
                .email("kay@gmaill.com")
                .password("123")
                .build();

        userService.registerUser(userOne);
        userService.registerUser(userTwo);
        userService.registerUser(userThree);
    }

    private void createRole(RoleService roleService) {
        roleService.createRole(Role.roleBuilder()
                .name("ADMIN")
                .createdAt(new Date())
                .build());

        roleService.createRole(Role.roleBuilder()
                .name("MODERATOR")
                .createdAt(new Date())
                .build());

        roleService.createRole(Role.roleBuilder()
                .name("USER")
                .createdAt(new Date())
                .build());
    }
}

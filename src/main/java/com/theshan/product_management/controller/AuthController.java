package com.theshan.product_management.controller;

import com.theshan.product_management.dto.Response;
import com.theshan.product_management.dto.user.UserCreationRequest;
import com.theshan.product_management.dto.user.UserRegistrationRequest;
import com.theshan.product_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        Response response = this.userService.registerUser(userRegistrationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<Response> create(@RequestBody UserCreationRequest userCreationRequest) {
        Response response = this.userService.createUser(userCreationRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

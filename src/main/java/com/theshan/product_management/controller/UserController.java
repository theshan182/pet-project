package com.theshan.product_management.controller;

import com.theshan.product_management.dto.Response;
import com.theshan.product_management.dto.user.UserUpdateRequest;
import com.theshan.product_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Response> findAll() {
        Response response = this.userService.findAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response> findById(@PathVariable("userId") int userId) {
        Response response = this.userService.findUserById(userId);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/active")
    public ResponseEntity<Response> findAllActive() {
        Response response = this.userService.findAllActiveUsers();
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/disable")
    public ResponseEntity<Response> findAllInactive() {
        Response response = this.userService.findAllInactiveUsers();
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PatchMapping("/active/{userId}")
    public ResponseEntity<Response> active(@PathVariable("userId") int userId) {
        Response response = this.userService.disableUserById(userId);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PatchMapping("/disable/{userId}")
    public ResponseEntity<Response> disable(@PathVariable("userId") int userId) {
        Response response = this.userService.disableUserById(userId);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<Response> update(@PathVariable("userId") int userId, @RequestBody UserUpdateRequest userUpdateRequest) {
        Response response = this.userService.updateUserByUserUsingUserId(userId, userUpdateRequest);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }
}

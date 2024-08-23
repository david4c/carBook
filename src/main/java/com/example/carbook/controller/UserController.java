package com.example.carbook.controller;

import com.example.carbook.model.dto.UserRequest;
import com.example.carbook.model.dto.UserResponse;
import com.example.carbook.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@RequestBody @Valid final UserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @PutMapping("{id}")
    public UserResponse updateUser(@PathVariable final Long id, @RequestBody @Valid final UserRequest userRequest) {
        return userService.updateUser(id, userRequest);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable final Long id) {
        userService.deleteById(id);
    }
}

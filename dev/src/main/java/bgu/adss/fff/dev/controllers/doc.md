# Controllers Package Documentation

## Overview

The "controllers" package houses objects responsible for exposing the system's API endpoints. These objects, annotated with `@RestController`, handle incoming HTTP requests and return appropriate responses.

## Purpose

`TODO`

## Example

Consider a UserController responsible for managing user-related operations:

```java
package com.example.controllers;

import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody CreateUserRequest request) {
        UserDTO userDTO = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
```
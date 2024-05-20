# Contracts Package Documentation

## Overview

The "contracts" package contains Java records that define data contracts for communication within the application. These records facilitate the translation of domain objects and requests to and from JSON format.

## Purpose

### 1. Data Transfer Objects (DTOs)

DTOs encapsulate data exchanged between client and server in a standardized manner, promoting consistency and interoperability.

### 2. JSON Serialization/Deserialization

`TODO`

## Example Usage

Consider a `User` domain object. We define a corresponding DTO, `UserDTO`, in the contracts package:

```java
package com.example.contracts;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDTO(
    @JsonProperty("id") Long id,
    @JsonProperty("username") String username,
    @JsonProperty("email") String email
) {}
```

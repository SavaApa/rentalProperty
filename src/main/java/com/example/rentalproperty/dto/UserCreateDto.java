package com.example.rentalproperty.dto;

import lombok.Data;
import lombok.Value;

@Value
public class UserCreateDto {
    String firstName;
    String lastName;

    String email;
    String password;
}

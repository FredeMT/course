package com.ead.course.dtos;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.UUID;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private UserStatus userStatus;
    private UserType userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;

}

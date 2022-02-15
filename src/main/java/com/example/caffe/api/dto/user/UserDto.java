package com.example.caffe.api.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserDto {

    private Long userId;

    private String firstName;

    private String lastName;

    private String jmbg;

    private String username;

    private String role;
}

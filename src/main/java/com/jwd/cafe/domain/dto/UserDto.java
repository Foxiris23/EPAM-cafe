package com.jwd.cafe.domain.dto;

import com.jwd.cafe.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {
    private Long id;
    private Role role;
}
package com.example.tfhbackend.mapper;

import com.example.tfhbackend.dto.UserDTO;
import com.example.tfhbackend.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements Mapper<User, UserDTO> {
    @Override
    public UserDTO map(User entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .phone(entity.getPhone())
                .email(entity.getEmail())
                .role(entity.getRole().name())
                .build();
    }
}
